package com.lcy.pattern.singletom.hungry;

public class Hungry {

    private Hungry(){}

    //java加载顺序：先静态、后动态；先属性、后方法；先上后下
    private static final Hungry hungry = new Hungry();


    public static Hungry getInstance() {
        return hungry;
    }

}
