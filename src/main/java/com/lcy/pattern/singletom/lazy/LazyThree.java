package com.lcy.pattern.singletom.lazy;

public class LazyThree {


    //默认使用LazyThree的时候，会先初始化内部类
    //如果没有使用的话，内部类是不加载的

    //内部类会反射侵入，一般不用处理，需求要求则加上处理防止反射侵入
    private static boolean initialized = false;

    private LazyThree (){
        synchronized (LazyThree.class) {
            if( initialized == false) {
                initialized = !initialized;
            } else {
                throw new RuntimeException("单例已被侵犯");
            }
        }

    }

    public static final LazyThree getInstance() {
        return LazyHolder.LAZY;
    }

    private static class LazyHolder{
        private static final LazyThree LAZY = new LazyThree();
    }


}
