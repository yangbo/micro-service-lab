package com.huajin.commander.client.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huajin.commander.client.domain.LocalEventEntity;
import com.huajin.commander.client.repository.LocalEventRepository;

/**
 * 查询本地“可靠事件”信息的 controller
 * 
 * @author bo.yang
 */
@RestController
@RequestMapping("/commander")
public class LocalEventController {

	@Autowired
	private LocalEventRepository localEventRepository;
	
	@GetMapping(path="/events")
	public List<LocalEventEntity> getAllEvents() {
		Iterable<LocalEventEntity> results = localEventRepository.findAll();
		List<LocalEventEntity> allEvents = new LinkedList<>();
		results.forEach(event->allEvents.add(event));
		return allEvents;
	}
}
