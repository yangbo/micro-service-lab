package com.huajin.commander.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huajin.commander.manager.domain.GlobalEventEntity;
import com.huajin.commander.manager.domain.ReliableEvent;
import com.huajin.commander.manager.domain.ReliableEventStatus;
import com.huajin.commander.manager.exception.EventNotFoundException;
import com.huajin.commander.manager.mq.MessageSender;
import com.huajin.commander.manager.repository.GlobalEventRepository;
import com.huajin.commander.manager.repository.RepositoryUtils;

@Service
public class EventManagerService {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GlobalEventRepository globalEventRepository;
	
	@Autowired
	private MessageSender msgService;
	

	/**
	 * 创建"可靠事件"。
	 * 
	 * @param reliableEvent 在远程事件管理器上创建的<b>可靠事件</b>
	 * 
	 * @return 创建了的事件实体对象
	 */
	public ReliableEvent createReliableEvent(ReliableEvent reliableEvent){
		log.debug("Create reliable-event (eventId={})", reliableEvent.getEventId());
		
		GlobalEventEntity eventEntity = RepositoryUtils.getEntityFromReliableEvent(reliableEvent);
		globalEventRepository.save(eventEntity);
		return reliableEvent;
	}
	
	/**
	 * "确认"指定事件的发送
	 * 
	 * @param eventId 要确认发送的事件的id
	 * @return 成功发送到消息队列的“可靠事件”对象。
	 */
	public ReliableEvent confirmSend(Long eventId){
		log.debug("Confirm sending of reliable-event (eventId={})", eventId);
		
		GlobalEventEntity eventEntity = globalEventRepository.findByEventId(eventId);
		if (eventEntity == null){
			throw new EventNotFoundException(eventId);
		}
		eventEntity.setStatus(ReliableEventStatus.Sended.value());
		
		// 发送到消息队列
		ReliableEvent reliableEvent = RepositoryUtils.getReliableEventFromEntity(eventEntity);
		msgService.sendEvent(reliableEvent);
		
		// 保存全局事件的状态
		globalEventRepository.save(eventEntity);
		
		return reliableEvent;
	}
	
	/**
	 * 向远程的事件管理器"取消"指定事件的发送
	 * 
	 * @param eventId 要取消发送的事件的id
	 * @return 取消了的“可靠事件”对象; 如果事件对象不存在则返回异常。
	 */
	public ReliableEvent cancelSend(Long eventId) {
		log.debug("Cancel sending of reliable-event (eventId={})", eventId);
		// 更新全局事件的状态为取消发送
		GlobalEventEntity eventEntity = globalEventRepository.findByEventId(eventId);
		if (eventEntity == null) {
			throw new EventNotFoundException(eventId);
		}
		ReliableEvent reliableEvent = RepositoryUtils.getReliableEventFromEntity(eventEntity);
		eventEntity.setStatus(ReliableEventStatus.Canceled.value());
		globalEventRepository.save(eventEntity);
		return reliableEvent;
	}
}
