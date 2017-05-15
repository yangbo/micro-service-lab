package com.huajin.commander.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.huajin.commander.client.domain.Constants;
import com.huajin.commander.client.domain.LocalEventEntity;
import com.huajin.commander.client.domain.ReliableEvent;
import com.huajin.commander.client.domain.ReliableEventStatus;
import com.huajin.commander.client.domain.ReliableEventType;
import com.huajin.commander.client.repository.LocalEventRepository;
import com.huajin.commander.client.repository.RepositoryUtils;
import com.huajin.commander.client.service.IdService;

/**
 * 可靠事件管理器的客户端API实现。
 * 
 * <p>主要功能是
 * <ol>
 * <li> 记录事件到本地事件表 {@link #createLocalEventForEntity(Object)}
 * <li> 通知事件管理器创建事件
 * <li> 确认提交事件
 * <li> 取消事件发送
 * </ol>
 * 参考 {@link ReliableEventManagerClient}
 * 
 * @author bo.yang
 */
@Component
public class ReliableEventManagerClientImpl implements ReliableEventManagerClient {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LocalEventRepository eventRepository;
	
	@Autowired
	private ReliableEventManagerAPI eventManagerAPI;
	
	@Autowired
	private IdService idService;	// for Event Id
	
	// === for composing service id like 'hostname:port:application-name'
	@Value("${spring.application.name:}")
	private String applicationName;	// for service id
	
	@Value("${eureka.instance.hostname:}")
	private String hostname; 		// for service id
	
	@Value("${server.port:}")
	private String port;			// for service id
	// === end of composing service id

	private ThreadLocal<LocalEventEntity> threadCreatedEvent = new ThreadLocal<>();
	
	
	/**
	 * 记录事件到本地事件表
	 * <p>
	 * 保存并准备发布“实体变动”事件，
	 * 实际发布动作的时间要在“所属事务”提交后才会发生，如果事务回滚则会自动取消发布。
	 * 
	 * @param entity 变动的实体，因为该实体变动而产生这个事件
	 */
	private ReliableEvent createLocalEventForEntity(Object entity) {
		log.debug("Create local reliable-event for entity ({})", entity.getClass().getName());
		ReliableEvent reliableEvent = new ReliableEvent();
		reliableEvent.setCreateTime(new Date());
		reliableEvent.setEventId(idService.nextId());
		
		Map<String, Object> params = new HashMap<>();
		params.put(Constants.PARAMS_KEY_ENTITY, entity);
		reliableEvent.setParams(params);
		
		reliableEvent.setSource(getServiceId());
		reliableEvent.setStatus(ReliableEventStatus.Created);	// 表示需要发送给“事件管理器”。
		reliableEvent.setType(ReliableEventType.EntityChanged);
		// 保存实体对象到数据库
		LocalEventEntity localEvent = RepositoryUtils.getEntityFromReliableEvent(reliableEvent);
		eventRepository.save(localEvent);
		threadCreatedEvent.set(localEvent);
		return reliableEvent;
	}
	
	/**
	 * 产生事件的服务id，通常取当前服务的 Eureka Service Id
	 * @return
	 */
	private String getServiceId() {
		String host = this.hostname;
		if (StringUtils.isBlank(this.hostname)){
			try {
				host = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				log.warn("Can not get localhost name, use 'localhost' as hostname.");
				host = "localhost";
			}
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(host).append(":").append(port).append(":").append(applicationName);
		return buffer.toString();
	}

	private void createRemoteEvent(ReliableEvent event) {
		log.debug("Create remote reliable-event (eventId={})", event.getEventId());
		eventManagerAPI.createRemoteEvent(event);
	}

	/**
	 * 从线程本地变量中获取最近创建的可靠事件实体，然后调用事件管理器REST接口。
	 */
	@Override
	public void confirmSend() {
		LocalEventEntity threadCreatedEvent = this.threadCreatedEvent.get();
		if (threadCreatedEvent == null) {
			log.debug("Confirm send (commit) while there is no reliable-event.");
			return;
		}
		log.debug("Confirm send (commit) reliable-event (eventId={})", threadCreatedEvent.getEventId());
		// 更新状态
		threadCreatedEvent.setStatus(ReliableEventStatus.Sended.value());
		eventRepository.save(threadCreatedEvent);
		// 调用远程事件管理器
		eventManagerAPI.confirmSend(threadCreatedEvent.getEventId());
		this.threadCreatedEvent.remove();
	}

	@Override
	public void cancelSend() {
		LocalEventEntity threadCreatedEvent = this.threadCreatedEvent.get();
		if (threadCreatedEvent == null) {
			log.debug("Cancel send (rollback) while there is no reliable-event.");
			return;
		}
		log.debug("Cancel send (rollback) reliable-event (eventId={})", threadCreatedEvent.getEventId());
		// 更新状态
		threadCreatedEvent.setStatus(ReliableEventStatus.Canceled.value());
		eventRepository.save(threadCreatedEvent);
		// 调用远程事件管理器
		eventManagerAPI.cancelSend(threadCreatedEvent.getEventId());
		this.threadCreatedEvent.remove();
	}

	@Override
	public ReliableEvent createEventForEntity(Object entity) {
		log.debug("Create reliable-event for entity ({})", entity.getClass().getName());

		ReliableEvent reliableEvent = createLocalEventForEntity(entity);
		createRemoteEvent(reliableEvent);
		return reliableEvent;
	}
	
}
