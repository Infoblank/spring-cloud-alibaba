package com.minio.cloudminio.utils;

import cn.hutool.core.io.file.FileNameUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.minio.cloudminio.config.fileconfig.UploadProperties;
import com.minio.cloudminio.config.minioconfig.CloudMinioProperties;
import com.ztt.common.multipart.CloudMultipartFile;
import com.ztt.common.util.SpringApplicationContextHolder;
import com.ztt.constant.CommonConstant;
import io.minio.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MinioUtil {
	public static MinioClient minioClient;
	public static UploadProperties uploadProperties;

	public static CloudMinioProperties cloudMinioProperties;

	// 文件上传后的路径

	public static HashMap<String, Object> hashMap = new HashMap<>(3);


	static {
		// SpringApplicationContextHolder :common模块提供的公共获取bean的工具类
		minioClient = SpringApplicationContextHolder.getBean(MinioClient.class);
		uploadProperties = SpringApplicationContextHolder.getBean(UploadProperties.class);
		cloudMinioProperties = SpringApplicationContextHolder.getBean(CloudMinioProperties.class);
	}


	public static HashMap<String, Object> uploadFile(MultipartFile file, boolean isCompress) throws Exception {
		//判断文件是否为空
		if (Objects.isNull(file) || 0 == file.getSize()) {
			throw new Exception("文件大小为0,请重新选择文件!");
		}

		CloudMultipartFile cloudMultipartFile = compressedFiles(file, isCompress);
		file = cloudMultipartFile.getMultipartFile();
		minioClient.putObject(
				PutObjectArgs.builder().bucket(cloudMinioProperties.getBucketName()).object(cloudMultipartFile.getFileName()).stream(
								file.getInputStream(), file.getSize(), -1)
						.contentType(file.getContentType())
						.build());
		hashMap.put(CommonConstant.FILE_PATH, File.separator + cloudMinioProperties.getBucketName() + File.separator + file.getName());
		return hashMap;
	}


	public static CloudMultipartFile compressedFiles(MultipartFile file, boolean isCompress) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String extName = FileNameUtil.extName(originalFilename);
		assert extName != null;
		String fileName = CloudStringUtil.getSoleString() + "." + extName;
		if (isCompress) {
			FileItemFactory fileItemFactory = new DiskFileItemFactory();
			FileItem fileItem = fileItemFactory.createItem(fileName, uploadProperties.getFileContentType(), true, fileName);
			OutputStream outputStream = fileItem.getOutputStream();
			Thumbnails.of(file.getInputStream()).scale(uploadProperties.getCompressionRatio())
					.outputFormat(extName)
					.outputQuality(uploadProperties.getOutputQuality())
					.toOutputStream(outputStream);
			return new CloudMultipartFile(new CommonsMultipartFile(fileItem), fileName);
		}
		return new CloudMultipartFile(file, fileName);
	}


	/**
	 * 上传文件,默认情况下是压缩的,当文件不能被压缩时可以设置
	 *
	 * @param file 文件
	 * @return 路径
	 * @throws Exception 异常
	 */
	public static HashMap<String, Object> uploadFile(MultipartFile file) throws Exception {
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
	public static Optional<Bucket> getBucket(String bucketName) throws Exception {
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
		return getObject(cloudMinioProperties.getBucketName(), objectName);
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
	public static HashMap<String, Object> uploadVideo(MultipartFile file) throws Exception {
		//判断存储桶是否存在  不存在则创建
		//createBucket(uploadProperties.getBucketName());
		//文件名
		String originalFilename = file.getOriginalFilename();
		//新的文件名 = 时间戳_随机数.后缀名
		assert originalFilename != null;
		String fileName = NanoIdUtils.randomNanoId() + originalFilename.substring(originalFilename.lastIndexOf("."));
		//开始上传
		log.info("file大小:{}", file.getSize());
		minioClient.putObject(
				PutObjectArgs.builder().bucket(cloudMinioProperties.getBucketName()).object(fileName).stream(
								file.getInputStream(), file.getSize(), -1)
						.contentType(uploadProperties.getVideoContentType())
						.build());
		hashMap.put(CommonConstant.FILE_PATH, File.separator + cloudMinioProperties.getBucketName() + File.separator + fileName);
		return hashMap;
	}

}
