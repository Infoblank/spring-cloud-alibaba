class Example {
    static void main(String[] args) {
        //def x = 5;
        // println("hello word!" + x);
        // fun();

        //func();
        // openFile();
        collection()
        // 标识符被用来定义变量，函数或其他用户定义的变量。标识符以字母开头，美元或下划线。他们不能以数字开头
        // def是groovy当中用来定义标识符的关键字(变量)
        // groovy的数据类型：int,short,byte,long,float,double,char,Boolean,String。
    }

    static def fun() {
        int x = 5;
        long y = 100L;
        float a = 10.56f;
        double b = 10.5e40;
        BigInteger bi = 30g;
        BigDecimal bd = 3.5g;
        println("(int)x=" + x)
        println("(long)y=" + y)
        println("(float)a=" + a)
        println("(double)b=" + b)
        println("(BigInteger)bi=" + bi)
        println("(BigDecimal)" + bd)
    }

    static def func() {
        println(2 & 3)
        println(!6)
        def range = 0..10;
        println(range.get(2))
    }

    static def openFile() {
        File file = new File("D:\\workspace\\IdeaProjects\\springCloud\\spring-cloud-alibaba\\groovy-test\\src\\file\\test.txt");
        file.eachLine {
            line -> println "Line : $line"
        }
        // 将整个文件的内容作为字符串获取使用txt属性。
        println file.text;

        println "The File ${file.absolutePath} has ${file.length()} bytes"

        // 写入文件
        // File writerFile = new File('D:/', 'test.txt')

        // writerFile.withWriter('utf-8', { writer -> writer.writeLine('hello test file writer.') })

        def files = new File('D:/')
        // eachFileRecurse
        println "File? ${files.isFile()}"
        println "Directory? ${files.isDirectory()}"

    }

    static def collection() {
        def map = ['name': 'ztt', 'age': 24];
        println map.containsKey('name')

        def list = ['1','3',['c','v'],map];

        println list;
    }

    static  def regex(){
        def regex = ~'Groovy';
           /*
           closure  //闭包 robotics
           1,位于字符串的开始和结束：caret(^)和美元符号($)
           2,正则表达式的量词：(+)出现一次或多次，(*)出现零次或多次，(?)零次或一次
           3,元符号：{},匹配前一个字符特定数量的实例
           4,句号符号:(.)表示任何字符,也被描述成为通配符
           5,
           * */

    }
}