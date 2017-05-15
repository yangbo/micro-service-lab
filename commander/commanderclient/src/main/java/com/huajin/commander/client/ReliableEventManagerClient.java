package com.huajin.commander.client;

import com.huajin.commander.client.domain.ReliableEvent;

/**
 * 可靠事件管理器的客户端API。
 * 
 * <p>主要功能是
 * <ol>
 * <li> 记录事件到本地事件表: {@link #createLocalEventForEntity(Object)}
 * <li> 通知事件管理器创建可靠事件: {@link #createRemoteEvent(ReliableEvent)}
 * <li> 确认提交事件: {@link #confirmSend()}
 * <li> 取消事件发送: {@link #cancelSend()}
 * </ol>
 * @author bo.yang
 */
public interface ReliableEventManagerClient {
	
	/**
	 * 创建"实体变动"对应的可靠事件。
	 * <p>记录事件到本地事件表并调用远程的事件管理器创建该事件，
	 * 为发布“实体变动”事件做好准备，
	 * 实际发布动作的时间要在“所属事务”提交后才会发生，如果事务回滚则会自动取消发布。
	 * 
	 * <p>TODO: 支持嵌套事务
	 * 
	 * @param entity 变动的实体，因为该实体变动而产生这个事件。
	 */
	ReliableEvent createEventForEntity(Object entity);
	
	/**
	 * 向远程的事件管理器"确认"指定事件的发送。
	 */
	void confirmSend();
	
	/**
	 * 取消当前线程创建的可靠事件。
	 * <p>会向远程的事件管理器"取消"当前线程最新创建的可靠事件。
	 */
	public void cancelSend();
	
}
