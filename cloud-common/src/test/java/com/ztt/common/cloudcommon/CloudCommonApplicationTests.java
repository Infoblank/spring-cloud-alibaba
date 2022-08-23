package com.ztt.common.cloudcommon;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

//@SpringBootTest
class CloudCommonApplicationTests {

    @Test
    void contextLoads() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<Integer> ints = new ArrayList<>();
        ArrayList strs = new ArrayList<String>();
        // new ArrayList<String>().add(89);
        strs.add(89);
        // java泛型检查的是引用而不是

    }

    public <T> String show(T one) {
        String name = one.getClass().getName();
        return name;
    }

}
