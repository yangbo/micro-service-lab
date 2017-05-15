package com.huajin.commander.manager.exception;

/**
 * 当用 eventId 找不到对应的可靠事件时抛出。
 * 
 * @author bo.yang
 */
public class EventNotFoundException extends ReliableEventException {

	private static final long serialVersionUID = -8176989100867041002L;

	public EventNotFoundException() {
	}

	public EventNotFoundException(Long eventId) {
		super(String.format("No event entity exists for eventId '%d'.", eventId));
	}
	
	public EventNotFoundException(String message) {
		super(message);
	}

	public EventNotFoundException(Throwable cause) {
		super(cause);
	}

	public EventNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
