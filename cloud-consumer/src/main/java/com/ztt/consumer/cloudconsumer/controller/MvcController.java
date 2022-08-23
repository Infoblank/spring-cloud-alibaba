package com.ztt.consumer.cloudconsumer.controller;

import com.ztt.common.ftp.FileSystemService;
import com.ztt.consumer.cloudconsumer.interfaceprovider.GitHubFeign;
import com.ztt.consumer.cloudconsumer.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
@RequestMapping("/mvc")
@RestController
public class MvcController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private GitHubFeign gitHubFeign;

    @RequestMapping(value = "/test")
    public String mvcTest() {
        for (int i = 0; i < 2; i++) {
            taskService.task();
        }
        log.info("MvcController.mvcTest()");
        return "mvcTest";
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
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(fileReader);
        }
    }

    @Autowired
    private FileSystemService fileSystemService;

    @GetMapping("/ftp")
    public String[] ftp() throws Exception {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("ftp.properties");
        String property = properties.getProperty("ftp.ip");
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();

        // fileSystemService.downloadFile("/chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test","C_5GR_PHUB_VIEW_20220822.txt");
        return fileSystemService.listNames("/chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test/", "20220821");
    }
}
