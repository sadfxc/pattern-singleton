package com.lcy.pattern.singletom.test;

import com.lcy.pattern.singletom.hungry.Hungry;
import com.lcy.pattern.singletom.lazy.LazyOne;
import com.lcy.pattern.singletom.lazy.LazyThree;
import com.lcy.pattern.singletom.lazy.LazyTwo;
import com.lcy.pattern.singletom.seriable.Seriable;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreaSafeTest {

    public static void main(String[] args) throws InterruptedException {
        //饿汉式
        //hungryTest();
        //懒汉式(线程不安全)
//        lazyOneTest();
        //懒汉式(线程安全，但对性能有影响  synchronized)
//        lazyTwoTest();
        //懒汉式(线程安全，不影响性能；问题：内部类会反射侵入)
//        lazyThreeTest();
        //演示侵犯
//        violateTest();
        //单例：序列化不安全   设置方法readResolve后变成安全
        Serializable();
    }


    public static void Serializable() {

        Seriable s1 = null;
        Seriable s2 = Seriable.getInstance();
        try {
            FileOutputStream fos = new FileOutputStream("Seriable.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s2);
            oos.flush();
            oos.close();


            FileInputStream fis = new FileInputStream("Seriable.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            s1 = (Seriable)ois.readObject();
            ois.close();

            System.out.println(s1);
            System.out.println(s2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

  //演示侵犯
    public static void violateTest() {
        try {
            Class<?> clazz = LazyThree.class;
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            System.out.println(constructors);
            for(Constructor c : constructors) {
                //用反射强制访问
                c.setAccessible(true);
                Object o = c.newInstance();
                System.out.println("进入"+o);
            }


        } catch (Exception e) {

        }

    }

    public static void lazyThreeTest() throws InterruptedException {
        int count = 100;
        ExecutorService service = Executors.newCachedThreadPool();
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i=0;i<100;i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    LazyThree lazyThree = LazyThree.getInstance();
                    System.out.println(System.currentTimeMillis() + ":" +lazyThree);
                    latch.countDown();
                }
            };
            service.execute(runnable);
        }

        latch.await();
        service.shutdown();
        System.out.println("完成");

    }



    public static void lazyTwoTest(){
        int count = 100;
        final CountDownLatch latch = new CountDownLatch(count);
        ExecutorService service = Executors.newCachedThreadPool();

        long start = System.currentTimeMillis();
        for(int i = 0 ; i< count ;i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    LazyTwo lazyTwo = LazyTwo.getInstance();
                    System.out.println(System.currentTimeMillis() + ":" +lazyTwo);
                    latch.countDown();
                }
            };
            service.execute(runnable);
        }
        long end = System.currentTimeMillis();
        try {
            latch.await();
            System.out.println("完成:"+(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

    public static void lazyOneTest(){
        int count = 100;
        final CountDownLatch latch = new CountDownLatch(count);
        ExecutorService service = Executors.newCachedThreadPool();
        long start = System.currentTimeMillis();
        for(int i = 0 ; i< count ;i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    LazyOne lazyOne = LazyOne.getInstance();
                    System.out.println(System.currentTimeMillis() + ":" +lazyOne);
                    latch.countDown();
                }
            };
            service.execute(runnable);
        }
        long end = System.currentTimeMillis();


        try {
            latch.await();
            System.out.println("完成:"+(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.shutdown();
    }


    public static void hungryTest(){
        int count = 100;
        final CountDownLatch latch = new CountDownLatch(count);
        final Set<Hungry> syncSet = Collections.synchronizedSet(new HashSet<Hungry>());

        for(int i = 0 ; i<count;i++) {
            new Thread() {
                @Override
                public void run() {
                    Hungry hungry = Hungry.getInstance();
                    System.out.println(System.currentTimeMillis() + ":" +hungry);
                    latch.countDown();
                }
            }.start();
        }

        try {
            latch.await();
            System.out.println("完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
