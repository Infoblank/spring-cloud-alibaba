package com.ztt.cloudprovider.test;

import java.util.*;

public class MainMapTest {

    static String val;
    String var;
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
        MainMapTest.testOptional();
        MainMapTest mapTest = new MainMapTest();
        mapTest.var ="111222";
        MainMapTest.val = "111";
        System.out.println(MainMapTest.val);
        System.out.println(mapTest.var);

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
