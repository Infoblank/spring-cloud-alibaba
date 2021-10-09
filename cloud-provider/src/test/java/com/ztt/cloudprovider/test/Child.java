package com.ztt.cloudprovider.test;

public class Child extends Father {
    static String childName = "childName0";

    private String pubKey = "ChildKey";

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
    void pubKeyChanged(){
        System.out.println("Child.pubKeyChanged");
    }
    static {
        System.out.println("childName的初始值："+childName);
        childName = "childName";
        System.out.println("Child的static{}执行....."+childName);

    }

    {
        System.out.println("Child的{}执行....."+childName);
    }

    Child() {
        // super();
        System.out.println("Child的构造函数执行....."+childName);
    }
}
