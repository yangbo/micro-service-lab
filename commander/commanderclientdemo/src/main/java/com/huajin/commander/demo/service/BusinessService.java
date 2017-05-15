package com.huajin.commander.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huajin.commander.client.ReliableEventManagerClientImpl;
import com.huajin.commander.demo.domain.OrderEntity;
import com.huajin.commander.demo.repository.OrderRepository;

@Service
public class BusinessService {
	
	@Autowired
	private ReliableEventManagerClientImpl eventManager;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Transactional
	public void createOrder(Long itemId, Integer amount) {
		// 1. 创建订单
		OrderEntity order = new OrderEntity();
		order.setItemId(itemId);
		order.setAmount(amount);
		orderRepository.save(order);
		
		// 2. 保存订单创建事件
		eventManager.createEventForEntity(order);
		
		throw new IllegalArgumentException("test");
	}
}
