package com.ztt.cloudgateway;

import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String regEx = ".*[\\/\"]*";
        Pattern p = Pattern.compile(regEx);
        var s = "/东方福邸一期8幢地下室机房光分菜合适的/";
        System.out.println("nm= " + p.matcher(s).matches());
    }
}
