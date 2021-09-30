package com.nacos.cloudalibabanacos;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//@SpringBootTest Mustache
class CloudAlibabaNacosApplicationTests {

    @Test
    void contextLoads() {
        Set<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        strings.add("31");

        HashSet<String> objects = new HashSet<>();
//        objects.add("1");
        objects.add("200");

        boolean removeAll = strings.removeAll(objects);
        System.out.println("删除"+removeAll);

        System.out.println(Arrays.toString(strings.toArray()));

    }

}
