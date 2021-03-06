package com.lcy.pattern.singletom.lazy;

public class LazyTwo {

    private LazyTwo(){}

    private static LazyTwo lazy = null;

    public static synchronized LazyTwo getInstance() {
        if(lazy == null) {
            lazy = new LazyTwo();
        }
        return lazy;
    }


}
