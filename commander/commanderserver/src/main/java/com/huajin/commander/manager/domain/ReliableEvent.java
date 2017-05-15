package com.huajin.commander.manager.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 可靠事件
 * 
 * @author bo.yang
 */
public class ReliableEvent implements Serializable {
	
	private static final long serialVersionUID = -4705738481012090877L;
	
	private Long eventId;
	private ReliableEventType type;
	private ReliableEventStatus status;
	private Object params;
	private String source;
	private Date createTime;
	
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public ReliableEventType getType() {
		return type;
	}
	public void setType(ReliableEventType type) {
		this.type = type;
	}
	public ReliableEventStatus getStatus() {
		return status;
	}
	public void setStatus(ReliableEventStatus status) {
		this.status = status;
	}
	/**
	 * @return Map like '{entity: {...} }'
	 */
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
