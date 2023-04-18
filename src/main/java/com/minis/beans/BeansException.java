package com.minis.beans;

public class BeansException extends Exception {
    public BeansException(String msg) {
        super(msg); // 直接调用父类（Exception）处理并抛出异常
        // 后续我们可以根据实际情况对这个类进行拓展
    }
}
