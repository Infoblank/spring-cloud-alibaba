package com.ztt.common.ftp;

import java.io.File;
import java.io.InputStream;

/**
 * @author jason.tang
 * @create 2019-03-07 13:33
 * @description
 */
public interface FileSystemService {

    boolean uploadFile(String targetPath, InputStream inputStream) throws Exception;

    boolean uploadFile(String targetPath, File file) throws Exception;

    /**
     *
     * @param targetPath /chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test/
     * @param fileName 文件的名称,来自listNames方法
     * @throws Exception 下载异常
     */
    File downloadFile(String targetPath,String fileName) throws Exception;

    boolean deleteFile(String targetPath) throws Exception;

    InputStream downloadFileInputStream(String targetPath) throws Exception;

    /**
     *
     * @param dirPath 目录 /chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test/
     * @param data 上一次文件目录的时间 20200820
     * @throws Exception 错误
     */
    String [] listNames(String dirPath,String data) throws Exception;
}
