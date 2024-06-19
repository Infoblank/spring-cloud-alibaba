package com.minio.cloudminio.dao.fallback;

import com.minio.cloudminio.dao.MinioDao;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class MinioDaoFallback implements MinioDao {
	@Override
	@ResponseBody
	public String queryFileName(String fileName) {
		return "CLOUD";
	}
}
