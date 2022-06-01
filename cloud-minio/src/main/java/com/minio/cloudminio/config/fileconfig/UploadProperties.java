package com.minio.cloudminio.config.fileconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传的基础配置,后面还有的再加
 */
@ConfigurationProperties(prefix = "upload")
@Setter
@Getter
@Component
public class UploadProperties {
    // 压缩比例
    private float compressionRatio = 1.0f;

    // 输出的质量,在0.0f-1.0f之间,默认为0.25
    private float outputQuality = 0.25f;

    // minio上传的桶名称,在minio服务的根目录,相当于数据库的名称
    private String bucketName = "zhang-bucket";

    private String fileContentType = "text/plain";

    private String videoContentType = "video/mp4";

}
