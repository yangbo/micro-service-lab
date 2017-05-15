package com.huajin.commander.client.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.huajin.commander.client.ReliableEventManagerClient;
import com.huajin.commander.client.transaction.EventTransactionSynchronization;

@Aspect
@Component
public class TransactionEventAspect {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private ReliableEventManagerClient reliableEventManagerClient;
	
	@Before("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void registerSynchronization(JoinPoint joinPoint){
		log.debug("Cut @Transactional with join point: {}", joinPoint);
		
		EventTransactionSynchronization sync = new EventTransactionSynchronization(transactionManager, reliableEventManagerClient);
		TransactionSynchronizationManager.registerSynchronization(sync);
		
		log.debug("Registered EventTransactionSynchronization with TransactionSynchronizationManager.");
	}
}
