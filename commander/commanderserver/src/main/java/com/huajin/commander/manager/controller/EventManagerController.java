package com.huajin.commander.manager.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huajin.commander.manager.domain.GlobalEventEntity;
import com.huajin.commander.manager.domain.ReliableEvent;
import com.huajin.commander.manager.repository.GlobalEventRepository;
import com.huajin.commander.manager.service.EventManagerService;

@RestController
@RequestMapping("/v1")
public class EventManagerController {

	@Autowired
	private EventManagerService eventManagerService;
	
	@Autowired
	private GlobalEventRepository eventRepository;
	
	/**
	 * 通知远程的事件管理器创建"可靠事件"。
	 * 
	 * @param event 在远程事件管理器上创建的<b>可靠事件</b>
	 */
	@RequestMapping(path="/event", method=RequestMethod.POST)
	public ReliableEvent createRemoteEvent(@RequestBody ReliableEvent reliableEvent){
		return eventManagerService.createReliableEvent(reliableEvent);
	}
	
	/**
	 * 向远程的事件管理器"确认"指定事件的发送
	 * 
	 * @param eventId 要确认发送的事件的id
	 */
	@RequestMapping(path="/sendevent/{id}", method=RequestMethod.GET)
	public ReliableEvent confirmSend(@PathVariable("id") Long eventId){
		return eventManagerService.confirmSend(eventId);
	}
	
	/**
	 * 向远程的事件管理器"取消"指定事件的发送
	 * 
	 * @param eventId 要取消发送的事件的id
	 */
	@RequestMapping(path="/cancelevent/{id}", method=RequestMethod.GET)
	public ReliableEvent cancelSend(@PathVariable("id") Long eventId){
		return eventManagerService.cancelSend(eventId);
	}
	
	/**
	 * 列出所有的“全局可靠事件”。
	 * 
	 * @param reliableEvent
	 * @return
	 */
	@RequestMapping(path="/events", method=RequestMethod.GET)
	public List<GlobalEventEntity> events(ReliableEvent reliableEvent){
		LinkedList<GlobalEventEntity> allEvents = new LinkedList<GlobalEventEntity>();
		Iterable<GlobalEventEntity> result = eventRepository.findAll();
		result.forEach(entity -> allEvents.add(entity));
		return allEvents;
	}
	
}
