package com.zhang.cloudtest.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class StreamTest {
    public static List<Person> personList() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 4, "male", "New York"));
        personList.add(new Person("Jack", 7000, 5, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 7, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 9, "female", "New York"));
        personList.add(new Person("Owen", 9500, 21, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 45, "female", "New York"));
        return personList;
    }

    public static void main(String[] args) {
       // findOrForeachOrMatch();
       maxSalary();
        max_min_count();
    }
    /**
     * 遍历和匹配,foreach/find/match
     */
    public static void findOrForeachOrMatch() {
        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
        // 遍历输出符合条件的元素
        list.stream().filter(x -> x > 6).forEach(System.out::println);
        // 匹配第一个
        Optional<Integer> findFirst = list.stream().filter(x -> x > 6).findFirst();
        // 匹配任意（适用于并行流）
        Optional<Integer> findAny = list.parallelStream().filter(x -> x > 6).findAny();
        // 是否包含符合特定条件的元素
        boolean anyMatch = list.stream().anyMatch(x -> x < 6);
        // 断言
        Assert.isTrue(findAny.isPresent(), "不存在匹配");
        Assert.isTrue(findFirst.isPresent(), "不存在匹配");
        log.info("匹配第一个值：{}", findFirst.get());
        log.info("匹配任意一个值(parallel)：{}", findAny.get());
        log.info("是否存在大于6的值：{}", anyMatch);
    }

    public static void maxSalary(){
        List<Person> people = personList();
        List<String> collect = people.stream().filter(x -> x.getSalary() > 8000).map(Person::getName).collect(Collectors.toList());
        Optional<Person> max = people.stream().max(Comparator.comparingInt(Person::getSalary));
        Assert.isTrue(max.isPresent(), "不存在匹配");
        log.info("高于8000的员工:{}",collect);
        log.info("工资最高的员工:{}", max.get());
    }

    public static void max_min_count(){
        List<String> list = Arrays.asList("adn", "admit", "pot", "expand", "westbound");
        Optional<String> max = list.stream().max(Comparator.comparing(String::length));
        Optional<String> min = list.stream().min(Comparator.comparing(String::length));
        long count = list.stream().filter(x -> x.length() > 4).count();
        List<String> collect = list.stream().map(String::toUpperCase).collect(Collectors.toList());
        log.info("字符串最长的是：{}",max.get());
        log.info("字符串最短的是：{}",min.get());
        log.info("字符串长度大于4的个数：{}",count);
        log.info("全部转化为大写：{}",collect);
    }


}
