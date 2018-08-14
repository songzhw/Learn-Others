package com.billy.controller.core;

/**
 * @author billy.qi
 * @since 17/5/29 17:38
 */
public interface IServerMessageProcessor {

    /**
     * 监听连接状态
     * @param status 当前的连接状态
     */
    void onStatus(ConnectionStatus status);

    /**
     * 新消息
     * @param message 消息内容
     */
    void onMessage(String message);

    /**
     * 监听指定key的消息
     */
    String getDebugKey();
}
