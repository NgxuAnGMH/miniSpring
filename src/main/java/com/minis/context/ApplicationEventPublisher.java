package com.minis.context;

/**
 * 预留的发布事件机制
 */
public interface ApplicationEventPublisher {
	void publishEvent(ApplicationEvent event);
}
