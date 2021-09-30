package com.groovy.test

import java.time.LocalDateTime

class MainTest {
    static void main(String[] args) {
        // closure();
        // listCollection();
        //   mapsCollection();
        println LocalDateTime.now().format("yyyy-MM-dd HH:mm:ss")

    }

    static void closure() {
        def c3 = { int a, String b ->
            println a
            println b
            return a + b
        }
        // 闭包方法的参数用箭头定义，如果不特殊指定，则默认有一个it参数
        def c6 = {
            // this指向闭包外部的Object，指定义闭包的类
            // owner指向闭包外部的Object/Closure，指直接包含闭包的类或闭包


            println(this.getName()) // com.groovy.test.MainTest
            println(owner) // class com.groovy.test.MainTest
            return it + 1
        }

        def c7 = {
            String text, Closure cl ->
                println(this.getName())
                println(owner)
                println(delegate)
                cl.call()
                // println text
                // 闭包的默认返回值是最后一条执行语句的值(最后是print的话会返回null)。 不显示return的情况下。最后是闭包的调用也是同样的处理。
                /*text = text+'function'
                return 'c7'*/
        }
        /*println(c3.call(34, 'ztt'));
        println(c3(34, 'ztt'));
        println(c6.call(2));*/

        def c8 = c7.call('ztt', {
            println('4')
            def text = '100';
            text = text + '900'
        })

        println c8;
    }

    // 集合
    static void listCollection() {
        def list = [];
        list.add(1)
        list << 3;
        list.addAll([1, 2, 3])
        list.each {
            println "Item: $it"  // it是对应于当前元素的隐式参数
        }
        list.eachWithIndex { it, i -> //it是当前元素, i是索引位置
            println "$i: $it"
        }
        println(list.count(1))
        println list * 2
        println Collections.nCopies(2, [1, 4, 9])
        println Collections.nCopies(2, '0')
    }

    static void mapsCollection() {
        def map = [name: 'ztt', likes: 'cheese', id: 123]
        assert map.get('name') == 'ztt'
        assert map instanceof Map
        map.put('sex', '男')
        map.put('closure', {
            println 0
        })
        Closure cl = map.get('closure') as Closure
        cl.call()
        println map
        println '----------'
        def keyA = 'book'
        def map_1 = [(keyA): '12222']
        println map_1.getClass()
        def x = "xxx${map_1}"
        def x1 = 'xxx${map_1}'
        def x2 = '''xxx${map_1}'''
        println x2
        println x.hashCode() != x1.hashCode() ? x2.hashCode() : 1
    }
}
