package com.ztt.common.multipart;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
public class CloudMultipartFile implements Serializable {

	private MultipartFile multipartFile;

	private String fileName;

	public CloudMultipartFile(MultipartFile multipartFile, String fileName) {
		this.fileName = fileName;
		this.multipartFile = multipartFile;
	}
}
