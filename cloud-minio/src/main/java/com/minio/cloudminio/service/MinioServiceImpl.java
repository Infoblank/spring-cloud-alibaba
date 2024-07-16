package com.minio.cloudminio.service;

import com.minio.cloudminio.dao.MinioDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class MinioServiceImpl implements MinioService {

    @Resource
    private MinioDao minioDao;

    @Override
    public String queryFileName(String fileName) {
        return minioDao.queryFileName(fileName);
    }
}
