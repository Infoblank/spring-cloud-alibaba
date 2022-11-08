package com.minio.cloudminio.dao;

import com.ztt.api.dao.CloudDao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cloud-dao", path = "/dao")
public interface MinioDao extends CloudDao {

    @Override
    @PostMapping("/queryFileName")
    String queryFileName(String fileName);
}
