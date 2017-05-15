package com.huajin.commander.manager.mq;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huajin.commander.manager.Constants;
import com.huajin.commander.manager.domain.GlobalEventEntity;
import com.huajin.commander.manager.domain.ReliableEvent;
import com.huajin.commander.manager.domain.ReliableEventStatus;
import com.huajin.commander.manager.repository.GlobalEventRepository;
import com.huajin.commander.manager.service.EventManagerService;


@Component
public class MessageListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GlobalEventRepository eventRepository;
	
	@Autowired
	private AmqpAdmin amqpAdmin;
	
	@Autowired
	private EventManagerService eventManagerService;
	
	/**
	 * 接收“可靠事件”已经被成功处理的消息。
	 * <p>
	 * 标记“可靠事件”的状态为“已成功处理”；并归档该事件（暂时未实现）。
	 * 
	 * @param event 已经被成功处理的可靠事件
	 */
	@RabbitListener(queues = Constants.PROCESSED_QUEUE_NAME)
	public void onReliableEventProcessed(ReliableEvent event) {
		GlobalEventEntity eventEntity = eventRepository.findOne(event.getEventId());
		if (eventEntity == null) {
			log.error("Can not find entity(id={}) when receiving 'reliable-event processed' message", 
					event.getEventId());
			return ;
		}
		eventEntity.setStatus(ReliableEventStatus.Processed.value());
		eventRepository.save(eventEntity);
	}
	
	/**
	 * 当查询可靠事件状态有响应时执行。
	 * <p>
	 * 后续有两种处理逻辑：
	 * <ol>
	 * <li> 确认发送
	 * <li> 取消发送
	 * </ol>
	 * 
	 * @param reliableEvent 查询返回的可靠事件
	 */
	@RabbitListener(queues = Constants.QUERY_EVENT_STATUS_RESPONSE_QUEUE_NAME)
	public void onReliableEventStatusQueryResponse(ReliableEvent reliableEvent) {
		log.debug("Received reliable-event.\n{}", reliableEvent);
		Long eventId = reliableEvent.getEventId();
		switch (reliableEvent.getStatus()) {
		case Sended:
			log.debug("Recovered and confirm-send reliable-event (eventId={})", eventId);
			eventManagerService.confirmSend(eventId);
			break;
		case Canceled:
			log.debug("Recovered and cancel-send reliable-event (eventId={})", eventId);
			eventManagerService.cancelSend(eventId);
			break;
		default:
			log.error("Processing reliable-event(id={}) should not reach here for status({}). Event:\n{}", 
					eventId, reliableEvent.getStatus(), reliableEvent);
			break;
		}
	}

	/**
	 * 配置用到的队列.
	 */
	@PostConstruct
	public void configQueue() {
		Queue queue = new Queue(Constants.PROCESSED_QUEUE_NAME, true);	// durable queue
		String declaredQueue = amqpAdmin.declareQueue(queue);
		if (declaredQueue == null) {
			log.error("Declare queue {} failed.", Constants.PROCESSED_QUEUE_NAME);
		}
		queue = new Queue(Constants.QUERY_EVENT_STATUS_RESPONSE_QUEUE_NAME, true);	// durable queue
		declaredQueue = amqpAdmin.declareQueue(queue);
		if (declaredQueue == null) {
			log.error("Declare queue {} failed.", Constants.QUERY_EVENT_STATUS_RESPONSE_QUEUE_NAME);
		}
		// configure exchange for querying event status
		boolean autoDelete = false;
		boolean durable = true;
		Exchange exchange = new FanoutExchange(Constants.QUERY_EVENT_STATUS_EXCHANGE, durable, autoDelete);
		amqpAdmin.declareExchange(exchange);
	}
}
