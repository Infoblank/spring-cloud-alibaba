package com.ztt.cloudprovider.test;

public class Father {

    static String fatherName = "FatherName0";
    private String  pubKey = "FatherKey";

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    void pubKeyChanged(){
        System.out.println("Father.pubKeyChanged");
    }
    static {
        System.out.println("fatherName的初始值："+fatherName);
        fatherName = "FatherName";
        System.out.println("Father的static{}块执行...."+fatherName);
    }

    {
        System.out.println("Father的{}块执行...."+fatherName);
    }

    Father() {
        System.out.println("Father的构造函数执行...."+fatherName);
    }
}
