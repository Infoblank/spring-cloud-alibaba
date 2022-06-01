package com.minio.cloudminio.contorller;

import cn.hutool.core.io.IoUtil;
import com.minio.cloudminio.utils.MinioUtil;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Optional;

@RestController
@RequestMapping("minio")
public class MinioController {

    private MinioUtil minioUtil;

    @Autowired
    private void setMinioUtil(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    @RequestMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile multipartFile) throws Exception {
        return minioUtil.uploadFile(multipartFile);
    }

    @RequestMapping("download")
    public void downloadFile(HttpServletResponse response) throws Exception {
        InputStream object = minioUtil.getObject("zhang-bucket", "0SLXD5HECUm0oPJdyeMEM.jpg");
        OutputStream stream = response.getOutputStream();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + "测试.jpg" + ";filename*=utf-8''" + URLEncoder.encode("测试.jpg", "UTF-8"));
        //response.setHeader("Content-Disposition", "attachment;fileName=测试.jpg");
        IoUtil.copy(object, stream, IoUtil.DEFAULT_BUFFER_SIZE);
        IoUtil.close(object);
        IoUtil.close(stream);
    }

    @RequestMapping("getBucket")
    public Optional<Bucket> getBucket(@RequestParam(value = "bucketName") String bucketName) throws Exception {
        return minioUtil.getBucket(bucketName);
    }
}
