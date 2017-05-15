package com.huajin.commander.client.mq;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huajin.commander.client.Constants;
import com.huajin.commander.client.domain.LocalEventEntity;
import com.huajin.commander.client.domain.ReliableEvent;
import com.huajin.commander.client.repository.LocalEventRepository;
import com.huajin.commander.client.repository.RepositoryUtils;

/**
 * 可靠消息 Commander 客户端消息侦听器。
 * 
 * @author bo.yang
 */
@Component
public class MessageListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LocalEventRepository localEventRepository;
	
	@Autowired
	private AmqpTemplate amqpTempalte;
	
	@Autowired
	private QueueNameBean queueNameBean;
	
	@Autowired
	private AmqpAdmin admin;
	
	/**
	 * 响应“可靠事件”状态查询消息。
	 * @param eventId 要查询的消息id
	 */
	@RabbitListener(queues="#{queueNameBean.queryEventStatusQueueName}")
	public void onQueryReliableEventStatus(Long eventId) {
		
		log.debug("Received query-event-status message(eventId={}) on queue '{}'.", 
				eventId, queueNameBean.getQueryEventStatusQueueName());
		
		LocalEventEntity eventEntity = localEventRepository.findByEventId(eventId);
		if (eventEntity == null) {
			log.debug("There is no event entity of eventId({})", eventId);
			return;
		}
		ReliableEvent reliableEvent = RepositoryUtils.getReliableEventFromEntity(eventEntity);
		amqpTempalte.convertAndSend(Constants.QUERY_EVENT_STATUS_RESPONSE_QUEUE_NAME, reliableEvent);
		
		log.debug("Sended response of query-event-status message. Entity is:\n{}", eventEntity);
	}
	
	/**
	 * 配置队列、交换器(fan-out)、绑定。
	 */
	@PostConstruct
	public void configuration() {
		boolean durable = true;
		boolean exclusive = true;
		boolean autoDelete = true;
		String queueName = queueNameBean.getQueryEventStatusQueueName();
		String exchangeName = Constants.QUERY_EVENT_STATUS_EXCHANGE;
		
		// configure queue
		Queue queue = new Queue(queueName, durable, exclusive, autoDelete);
		admin.declareQueue(queue);
		
		// configure exchange
		autoDelete = false;
		durable = true;
		Exchange exchange = new FanoutExchange(exchangeName, durable, autoDelete);
		admin.declareExchange(exchange);
		
		// binding exchange and queue
	    Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName).noargs();
	    admin.declareBinding(binding);
	}
}
