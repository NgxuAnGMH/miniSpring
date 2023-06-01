package com.minis.context;

import java.util.EventObject;

/**
 * 可以看出，ApplicationEvent 继承了 Java 工具包内的 *EventObject*，
 * 我们是在 ==Java 的事件监听==的基础上进行了简单的封装。
 * 虽然目前还没有任何实现，但这为我们后续使用==观察者模式==解耦代码提供了入口。
 */
public class ApplicationEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	protected String msg = null;

	public ApplicationEvent(Object arg0) {
		super(arg0);
		this.msg = arg0.toString();
	}

}
