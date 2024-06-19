package com.minio.cloudminio.dao;

import com.minio.cloudminio.dao.fallback.MinioDaoFallback;
import com.ztt.api.dao.CloudDao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cloud-dao", path = "/dao",fallback = MinioDaoFallback.class)
public interface MinioDao extends CloudDao {

    @Override
    @PostMapping("/queryFileName")
    String queryFileName(String fileName);
}
