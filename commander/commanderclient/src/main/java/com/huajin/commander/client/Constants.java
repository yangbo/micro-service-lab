package com.huajin.commander.client;

/**
 * 常量定义类.
 * 
 * @author bo.yang
 */
public class Constants {

	/**
	 * 收发可靠消息的队列名
	 */
	public static final String RELIABLE_EVENT_QUEUE_NAME = "queue_reliable_event";
	
	/**
	 * 查询可靠事件状态的队列名
	 */
	public static final String QUERY_EVENT_STATUS_QUEUE_NAME = "queue_reliable_event_query_status";

	/**
	 * 可靠事件状态查询的响应消息队列名
	 */
	public static final String QUERY_EVENT_STATUS_RESPONSE_QUEUE_NAME = "queue_reliable_event_query_status_response";

	/**
	 * 发送可靠事件已经成功处理的队列
	 */
	public static final String PROCESSED_QUEUE_NAME = "queue_reliable_event_processed";

	/**
	 * 查询可靠事件状态的扇出交换器名
	 */
	public static final String QUERY_EVENT_STATUS_EXCHANGE = "exchange_reliable_event_query_status_fanout";
	
}
