package com.lcy.pattern.singletom.seriable;

import java.io.Serializable;

public class Seriable implements Serializable {

    public final static Seriable INSTANCE = new Seriable();
    private Seriable(){}

    public static Seriable getInstance() {
        return INSTANCE;
    }

    //添加这个方法解决序列化造成线程不安全问题
    //序列化过程中自动执行该方法
    private Object readResolve() {
        return INSTANCE;
    }

}
