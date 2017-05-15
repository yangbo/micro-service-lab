package com.huajin.commander.event;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.huajin.commander.client.domain.ReliableEventType;

public class ReliableEventTypeTest {

	@Test
	public void testValue() {
		ReliableEventType eventType = ReliableEventType.EntityChanged;
		assertThat(eventType.value(), equalTo(1));
		
		eventType = ReliableEventType.AtomAction;
		assertThat(eventType.value(), equalTo(2));
	}
	
	@Test
	public void testName() {
		ReliableEventType eventType = ReliableEventType.EntityChanged;
		assertThat(eventType.name(), equalTo("EntityChanged"));
	}
}
