package com.ztt.cloudprovider.test;

import java.util.*;
// 父亲 孩子
public class MainMapTest {
    /**
     *  new对象时的执行关系：static里面的代码只会执行一次。
     *      父类的static块->子类的static块->父类的{}块->父类的构造函数->子类的{}块->子类构造函数
     */
    static String val = "static val";
    String var;

    {
        System.out.println("执行了{}块");
        System.out.println(val);
    }

    static {
        System.out.println(val);
        val = "kong";
        System.out.println("执行了static{}块");
        System.out.println(val);
    }

    //@Test
    static void testMap() {
        long start = System.currentTimeMillis();
        List<String> animals = Arrays.asList("dog", "cat", "fish", "dog");
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String animal : animals) {
            hashMap.compute(animal, (k, v) -> (v == null) ? 1 : ++v);
        }
        hashMap.compute("animal", (k, v) -> v);
        long end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start));
        System.out.println(hashMap);
    }

    public static void main(String[] args) {
        // MainMapTest.testMap();
        /*MainMapTest.testOptional();
        MainMapTest mapTest = new MainMapTest();
        mapTest.var = "111222";
        MainMapTest.val = "111";
        System.out.println(MainMapTest.val);
        System.out.println(mapTest.var);*/
        // Ignore AOP infrastructure such as scoped proxies. Wrapper
        Child child = new Child();
        child.pubKeyChanged();
        System.out.println(child.getPubKey());
        Father father1 = new Father();
        father1.pubKeyChanged();
        System.out.println(father1.getPubKey());
        Father father = new Child();
        father.pubKeyChanged();
        System.out.println(father.getPubKey());
    }


    static void testOptional() {
        Optional<Integer> list = Optional.ofNullable(MainMapTest.getString("1a"));
        System.out.println(list);
    }

    static Integer getString(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
