package com.huajin.commander.manager.repository;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huajin.commander.manager.domain.GlobalEventEntity;
import com.huajin.commander.manager.domain.ReliableEvent;
import com.huajin.commander.manager.domain.ReliableEventStatus;
import com.huajin.commander.manager.domain.ReliableEventType;

/**
 * 数据访问工具类
 * 
 * @author bo.yang
 */
public class RepositoryUtils {

	/**
	 * 从可靠事件 ReliableEvent 生成实体对象 GlobalEventEntity.
	 * 
	 * @param reliableEvent 可靠事件对象
	 * @return 实体对象 GlobalEventEntity
	 */
	public static GlobalEventEntity getEntityFromReliableEvent(ReliableEvent reliableEvent) {
		GlobalEventEntity GlobalEventEntity = new GlobalEventEntity();
		GlobalEventEntity.setCreateTime(new Date());
		GlobalEventEntity.setLocalCreateTime(reliableEvent.getCreateTime());
		GlobalEventEntity.setEventType(reliableEvent.getType().value());
		GlobalEventEntity.setName(reliableEvent.getType().name());
		GlobalEventEntity.setEventId(reliableEvent.getEventId());
		Object paramsObject = reliableEvent.getParams();
		if (paramsObject != null) {
			String jsonString = JSON.toJSONString(paramsObject);
			GlobalEventEntity.setParams(jsonString);
		}
		GlobalEventEntity.setSource(reliableEvent.getSource());
		GlobalEventEntity.setStatus(reliableEvent.getStatus().value());
		return GlobalEventEntity;
	}

	/**
	 * 从全局事件实体得到一个"可靠事件"对象。
	 * 
	 * @param eventEntity 全局事件实体
	 * @return 可靠事件对象
	 */
	public static ReliableEvent getReliableEventFromEntity(GlobalEventEntity eventEntity) {
		ReliableEvent reliableEvent = new ReliableEvent();
		reliableEvent.setCreateTime(eventEntity.getLocalCreateTime());
		reliableEvent.setEventId(eventEntity.getEventId());
		Object params = JSONObject.parse(eventEntity.getParams());
		reliableEvent.setParams(params);
		reliableEvent.setSource(eventEntity.getSource());
		reliableEvent.setStatus(ReliableEventStatus.valueOf(eventEntity.getStatus()));
		reliableEvent.setType(ReliableEventType.valueOf(eventEntity.getEventType()));
		return reliableEvent;
	}

}
