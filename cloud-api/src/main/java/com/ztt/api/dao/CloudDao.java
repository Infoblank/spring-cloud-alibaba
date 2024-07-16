package com.ztt.api.dao;

public interface CloudDao {

    /**
     * 提供查询上传文件的原始名称和minio名称的对应。
     *
     * @param fileName 文件名称
     * @return 查询到的文件名称
     */
    String queryFileName(String fileName);
}
