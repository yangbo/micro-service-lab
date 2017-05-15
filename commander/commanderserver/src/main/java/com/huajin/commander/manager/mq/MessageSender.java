package com.huajin.commander.manager.mq;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huajin.commander.manager.Constants;
import com.huajin.commander.manager.domain.ReliableEvent;

/**
 * RabbitMQ 消息发送服务
 * 
 * @author bo.yang
 */
@Component
public class MessageSender {
	
    private final AmqpAdmin amqpAdmin;
    private final AmqpTemplate amqpTemplate;
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    public MessageSender(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.amqpTemplate = amqpTemplate;
    }

    @PostConstruct
    public void configQueueExchange(){
    	// 配置队列
        Queue queue = new Queue(Constants.RELIABLE_EVENT_QUEUE_NAME, true);	// durable queue
        this.amqpAdmin.declareQueue(queue);
        
        queue = new Queue(Constants.QUERY_EVENT_STATUS_QUEUE_NAME, true);	// durable queue
        this.amqpAdmin.declareQueue(queue);
    }
    
    /**
     * 发送可靠事件到消息代理。
     * 
     * @param reliableEvent 可靠事件
     */
    public void sendEvent(ReliableEvent reliableEvent) {
    	this.amqpTemplate.convertAndSend(Constants.RELIABLE_EVENT_QUEUE_NAME, reliableEvent);
    }

    /**
     * 发送“查询可靠事件状态消息”到消息代理。
     * 
     * @param eventId 查询的事件id
     */
	public void sendQueryStatusMessage(Long eventId) {
		if (eventId == null) {
			throw new IllegalArgumentException("Event Id to query is null!");
		}
		log.debug("Sending query status message for eventId={}", eventId);
		
		this.amqpTemplate.convertAndSend(
				Constants.QUERY_EVENT_STATUS_EXCHANGE, 
				Constants.QUERY_EVENT_STATUS_QUEUE_NAME, eventId);
	}

}
