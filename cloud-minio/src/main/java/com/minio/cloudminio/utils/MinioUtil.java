package com.minio.cloudminio.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.minio.cloudminio.config.fileconfig.UploadProperties;
import com.ztt.common.util.SpringApplicationContextHolder;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MinioUtil {
    public static MinioClient minioClient;
    public static UploadProperties uploadProperties;


    static {
        // SpringApplicationContextHolder :common模块提供的公共获取bean的工具类
        minioClient = SpringApplicationContextHolder.getBean(MinioClient.class);
        uploadProperties = SpringApplicationContextHolder.getBean(UploadProperties.class);
    }

    public static void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public static String uploadFile(MultipartFile file, boolean isCompress) throws Exception {
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            return null;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(uploadProperties.getBucketName());
        //文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int beginIndex = originalFilename.lastIndexOf(".");
        String fileName = StringUtil.getSoleString() + originalFilename.substring(beginIndex);
        //开始上传
        if (file.getSize() > 0 && isCompress) {
            log.info("file压缩前大小:{}", file.getSize());
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            FileItem fileItem = fileItemFactory.createItem(fileName, uploadProperties.getFileContentType(), true, fileName);
            OutputStream outputStream = fileItem.getOutputStream();
            Thumbnails.of(file.getInputStream()).scale(uploadProperties.getCompressionRatio())
                    .outputFormat(originalFilename.substring(beginIndex + 1))
                    .outputQuality(uploadProperties.getOutputQuality())
                    .toOutputStream(outputStream);
            file = new CommonsMultipartFile(fileItem);
            log.info("file压缩后大小:{}", file.getSize());
        }
        minioClient.putObject(
                PutObjectArgs.builder().bucket(uploadProperties.getBucketName()).object(fileName).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return File.separator + uploadProperties.getBucketName() + File.separator + fileName;
    }

    /**
     * 上传文件,默认情况下是压缩的,当文件不能被压缩时可以设置
     *
     * @param file 文件
     * @return 路径
     * @throws Exception 异常
     */
    public static String uploadFile(MultipartFile file) throws Exception {
        return uploadFile(file, true);
    }

    /**
     * 获取全部bucket
     *
     * @return list
     */
    public static List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    public static Optional<Bucket> getBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, ErrorResponseException, XmlParserException, ServerException {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 获取⽂件外链
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    public static String getObjectURL(String bucketName, String objectName, Integer expires) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires).build());
    }

    /**
     * 获取⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @return ⼆进制流
     */
    public static InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public static InputStream getObject(String objectName) throws Exception {
        return getObject(uploadProperties.getBucketName(), objectName);
    }

    /**
     * 上传⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param stream     ⽂件流
     * @throws Exception https://docs.minio.io/cn/java-minioClient-api-reference.html#putObject
     */
    public static void putObject(String bucketName, String objectName, InputStream stream) throws
            Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).contentType(objectName.substring(objectName.lastIndexOf("."))).build());
    }

    /**
     * 上传⽂件
     *
     * @param bucketName  bucket名称
     * @param objectName  ⽂件名称
     * @param stream      ⽂件流
     * @param size        ⼤⼩
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-minioClient-api-reference.html#putObject
     */
    public static void putObject(String bucketName, String objectName, InputStream stream, long
            size, String contextType) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, size, -1).contentType(contextType).build());
    }

    /**
     * 获取⽂件信息
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception https://docs.minio.io/cn/java-minioClient-api-reference.html#statObject
     */
    public static StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }


    /**
     * 删除⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception <a href="https://docs.minio.io/cn/java-minioClient-apireference.html#removeObject"</a>
     */
    public static void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /***
     * 上传视频
     * @param file
     * @return
     * @throws Exception
     */
    public static String uploadVideo(MultipartFile file) throws Exception {
        //判断存储桶是否存在  不存在则创建
        createBucket(uploadProperties.getBucketName());
        //文件名
        String originalFilename = file.getOriginalFilename();
        //新的文件名 = 时间戳_随机数.后缀名
        assert originalFilename != null;
        String fileName = NanoIdUtils.randomNanoId() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //开始上传
        log.info("file大小:{}", file.getSize());
        minioClient.putObject(
                PutObjectArgs.builder().bucket(uploadProperties.getBucketName()).object(fileName).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(uploadProperties.getVideoContentType())
                        .build());
        return File.separatorChar + uploadProperties.getBucketName() + File.separatorChar + fileName;
    }

}
