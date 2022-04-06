package com.sa.cloudsatoken;

import com.alibaba.fastjson.JSONObject;

// @SpringBootTest
class CloudSaTokenApplicationTests {

    static void JSONObjectParse() {
        ReturnsResult map = JSONObject.parseObject("{head:{msgId:'null', senderId:'null', serviceName:'null', zone:'ahw'}, " +
                "data:[{no:'1'," +
                " searchAddress:'张江高科1', flag:'0', prov:'浙江省', provCode:'8370000', city:'杭州市', cityCode:'8371700', " +
                "area:'萧山区', areaCode:'8371700', town:'城厢街道', vill:'', road:'百尺溇路156号', unit:'3单元', " +
                "community:'百尺溇公寓', building:'111幢', layer:'3楼', number:'301室', " +
                "detail_addr:'杭州市萧山区城厢街道百尺溇路156号百尺溇公寓111幢3单元3层301室 ', lng:'', lat:'', telephone:'13376515174', " +
                "contact:'张三', email:'zhangsan@189.cn', username:'zhangsan'}, {no:'2', searchAddress:'张江高科1', " +
                "flag:'0', prov:'浙江省', provCode:'8370000', city:'台州市', cityCode:'8371700', area:'萧山区', areaCode:'2', " +
                "town:'城厢街道', vill:'', road:'百尺溇路156号', unit:'3单元', community:'百尺溇公寓', building:'111幢', layer:'3楼', " +
                "number:'302室', detail_addr:'杭州市萧山区城厢街道百尺溇路156号百尺溇公寓111幢3单元3层302室 ', lng:'', lat:'', " +
                "telephone:'13376515174', contact:'张三', email:'zhangsan@189.cn', username:'zhangsan'}], " +
                "orderRequest:{orderNo:'1234567890', orderTime:'2019-07-02 11:35:14.014'}}", ReturnsResult.class);
        System.out.println("head = " + map);
    }

    public static void main(String[] args) {
        //JSONObjectParse();
        //System.out.println("returnNumber() = " + returnNumber(7));
        javaOperator();
    }

    /**
     * @param number 入参
     * @return 返回入参>=的2次幂数
     */
    public static int returnNumber(int number) {
        int cap = number - 1;
        cap |= cap >>> 1;
        cap |= cap >>> 2;
        cap |= cap >>> 4;
        cap |= cap >>> 8;
        cap |= cap >>> 16;
        return (cap < 0) ? 1 : (cap >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : cap + 1;
    }

    public static void javaOperator() {
        int aIndex = 60; // 0011 1100
        int bindIndex = 13; // 0000 1101
        /*
          & : 如果相对应位都是1，则结果为1，否则为0
           0011 1100
         & 0000 1101
           0000 1100 = 12
         */
        System.out.println("aIndex & bindIndex = " + (aIndex & bindIndex));

        /*
        ^ : 如果相对应位值相同，则结果为0，否则为1
           0011 1100
         ^ 0000 1101
           0011 0001  = 49
         */
        System.out.println("aIndex ^ bindIndex = " + (aIndex ^ bindIndex));

        /*
        | : 如果相对应位都是 0，则结果为 0，否则为 1
            0011 1100
         |  0000 1101
            0011 1101  = 61
         */
        System.out.println("aIndex | bindIndex = " + (aIndex | bindIndex));

        /*
         ~ : 按位取反运算符翻转操作数的每一位，即0变成1，1变成0
        ~ 0011 1100
          1100 0011  = -61

         */
        System.out.println("(~aIndex) = " + (~aIndex));
        System.out.println("(~bindIndex) = " + (~bindIndex));

        /*
         << : 按位左移运算符。左操作数按位左移右操作数指定的位数(左移多少位相当于乘上2的多少次方)
         0011 1100 << 2  = 1111 0000 = 240
         0000 1101 << 2  = 0011 0100 = 52
         */
        System.out.println("(aIndex << 2) = " + (aIndex << 2));
        System.out.println("(bindIndex << 2) = " + (bindIndex << 2));

        /*
         >> : 按位右移运算符。左操作数按位右移右操作数指定的位数(左移多少位相当于除上2的多少次方)
         0011 1101 >> 2  = 0000 1111 = 15
         0000 1101 >> 2  = 0000 0011 = 3
         */
        System.out.println("(aIndex >> 2) = " + (aIndex >> 2));
        System.out.println("(bindIndex >> 2) = " + (bindIndex >> 2));

       /*
        >>> : 按位右移补零操作符。左操作数的值按右操作数指定的位数右移，移动得到的空位以零填充
         0011 1101  >>> 2 = 0000 1111 = 15
        */
        System.out.println("(aIndex>>>2) = " + (aIndex >>> 2));
        System.out.println("(bindIndex>>>2) = " + (bindIndex >>> 2));
    }
}
