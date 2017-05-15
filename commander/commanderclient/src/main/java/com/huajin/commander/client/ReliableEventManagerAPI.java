package com.huajin.commander.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huajin.commander.client.domain.ReliableEvent;

@FeignClient(name="reliable-event-manager")
public interface ReliableEventManagerAPI {

	/**
	 * 通知远程的事件管理器创建"可靠事件"。
	 * 
	 * @param event 在远程事件管理器上创建的<b>可靠事件</b>
	 */
	@RequestMapping(path="/v1/event", method=RequestMethod.POST)
	void createRemoteEvent(@RequestBody ReliableEvent event);
	
	/**
	 * 向远程的事件管理器"确认"指定事件的发送
	 * 
	 * @param eventId 要确认发送的事件的id
	 */
	@RequestMapping(path="/v1/sendevent/{id}", method=RequestMethod.GET)
	void confirmSend(@PathVariable("id") Long eventId);
	
	/**
	 * 向远程的事件管理器"取消"指定事件的发送
	 * 
	 * @param eventId 要取消发送的事件的id
	 */
	@RequestMapping(path="/v1/cancelevent/{id}", method=RequestMethod.GET)
	public void cancelSend(@PathVariable("id") Long eventId);

}
