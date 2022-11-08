package com.minio.cloudminio.contorller;

import cn.hutool.core.io.IoUtil;
import com.minio.cloudminio.service.MinioService;
import com.minio.cloudminio.utils.MinioUtil;
import io.minio.messages.Bucket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Resource
    private MinioService minioService;

    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile multipartFile) throws Exception {
        return MinioUtil.uploadFile(multipartFile);
    }

    @RequestMapping("/download")
    public void downloadFile(HttpServletResponse response, String fileName) throws Exception {
        // 将传递来的名字和minio上的名字对应起来, 调用dao的接口来查询数据
        String queryFileName = minioService.queryFileName(fileName);
        if (Objects.nonNull(queryFileName)) {
            InputStream object = MinioUtil.getObject(queryFileName);
            OutputStream stream = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            IoUtil.copy(object, stream, IoUtil.DEFAULT_BUFFER_SIZE);
            IoUtil.close(object);
            IoUtil.close(stream);
        }
    }

    @RequestMapping("getBucket")
    public Optional<Bucket> getBucket(@RequestParam(value = "bucketName") String bucketName) throws Exception {
        return MinioUtil.getBucket(bucketName);
    }
}
