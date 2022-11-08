package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.consumer.cloudconsumer.interfaceprovider.GitHubFeign;
import com.ztt.consumer.cloudconsumer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RequestMapping("/mvc")
@RestController
public class MvcController {


    @Autowired
    private TaskService taskService;

   /* @Autowired
    private FileSystemService fileSystemService;*/
    @Autowired
    private GitHubFeign gitHubFeign;


    @RequestMapping(value = "/test")
    public String mvcTest() {
        // 在多线程里面调用了feign的服务,无法回去到父请求对象,需要加上这一行代码 加在需要开始子线程任务开始的地方
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        for (int i = 0; i < 2; i++) {
            taskService.task();
        }
        log.info("MvcController.mvcTest()");
        return "mvcTest";
    }

    @PostMapping(value = "/hello")
    public String mvcHello() {
        log.info("MvcController.mvcHello()");
        return taskService.mvcHello();
    }

    @GetMapping(path = "/git/{query}")
    public Map<String, Object> search(@PathVariable String query) {
        Map<String, Object> stringObjectMap = gitHubFeign.searchRepositories(query);
        return stringObjectMap;
    }

    @GetMapping("/list")
    public void list() throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            strings.add("11111jsdnfkjsdnjk" + i);
        }
        String join = StringUtils.join(strings.toArray(), ",");
        log.info("{}", join);
        try (FileReader fileReader = new FileReader("");) {
            int read = fileReader.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*@GetMapping("/ftp")
    public String[] ftp() throws Exception {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("ftp.properties");
        String property = properties.getProperty("ftp.ip");
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();

        // fileSystemService.downloadFile("/chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test","C_5GR_PHUB_VIEW_20220822.txt");
        return fileSystemService.listNames("/chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test/", "20220821");
    }*/

    @PostMapping("/self")
    public String callSelf() {
        return "调用了Self方法...";
    }
}
