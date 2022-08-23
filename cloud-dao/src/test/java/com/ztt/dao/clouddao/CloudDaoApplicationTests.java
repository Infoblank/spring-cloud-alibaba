package com.ztt.dao.clouddao;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


class CloudDaoApplicationTests {

    @Test
    void contextLoads() {
        String str = "13/1,11/2,15/0,SFU,11/1,3/2,1/0,14/0,SFU,4/2,2/0,13/2,16/2,7/1,10/2,6/1,14/2,6/1,4/2,13/0,11/2,6/0,16/1,4/0,3/2,2/0,12/0,2/2,18,11/1,12/2,5/1,3/1,5/1,7/2,15/1,16/0,12/2,14/1,16/1,18,SFU,SFU,4/1,4/0,9/1,15/2,7/1,4/1,17,3/0,5/0,1/2,3/2,8/0,9/1,15/1,3/1,2/1,15/0,15/2,10/2,4/2,SFU,5/0,3/0,6/0,8/1,10/1,4/1,18,16/2,1/1,12/0,7/0,6/2,3/1,16/2,16/0,1/0,1/1,4/0,8/2,14/1,9/0,1/0,15/0,7/2,14/2,10/1,8/0,8/0,2/2,10/0,13/0,14/0,9/2,2/2,11/0,SFU,12/1,11/1,9/2,8/1,10/0,10/0,11/2,11/0,SFU,9/0,2/1,12/1,11/0,7/0,7/2,13/1,17,8/2,12/2,SFU,17,SFU,13/2,13/0,5/2,SFU,13/2,1/2,8/1,10/1,2/1,16/0,12/0,6/2,8/2,5/2,9/1,SFU,10/2,5/0,14/0,5/2,6/1,14/2,16/1,15/1,7/1,1/2,2/0,9/0,13/1,3/0,7/0,9/2,12/1,1/1,6/2,SFU,6/0,5/1,15/2,14/1";
        String[] strings = str.split(",");
        List<String> list = Arrays.asList(strings);
        list.sort((o1, o2) -> {
            if (o1 == null && o2 != null) {
                return -1;
            }
            if (o1 != null && o2 == null) {
                return 1;
            }
            if (o1 == null) {
                return 0;
            }
            return o1.compareTo(o2);
        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("AA","");
        String aa = hashMap.get("AA");
        System.out.println("aa = " + aa.length());
        System.out.println("list = " + list);
        //list.sort(Comparator.naturalOrder());
    }

}
