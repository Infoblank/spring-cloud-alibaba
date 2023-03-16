package com.ztt.cloudgateway.utils;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPUtils {


    private static final Logger logger = LoggerFactory.getLogger(GZIPUtils.class);

    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";


    public static final int byteLength = 1024;

    /**
     * 字符串压缩为GZIP字节数组
     */
    public static byte[] compress(String str) {
        return compress(str, GZIP_ENCODE_UTF_8);
    }

    /**
     * 字符串压缩为GZIP字节数组
     */
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            logger.error("gzip compress error.", e);
        }
        return out.toByteArray();
    }

    /**
     * GZIP解压缩
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[byteLength];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            logger.error("gzip uncompress error.", e);
        }
        return null;
    }

    /**
     * 解压
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }

    /**
     * 解压
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[byteLength];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            logger.error("gzip uncompress to string error.", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "%5B%7B%22lastUpdateTime%22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221";
        System.out.println("原长度：" + str.length());
        System.out.println("压缩后字符串：" + Arrays.toString(GZIPUtils.compress(str)).length());
        System.out.println("解压缩后字符串：" + StringUtils.newStringUtf8(GZIPUtils.uncompress(GZIPUtils.compress(str))));
        System.out.println("解压缩后字符串：" + GZIPUtils.uncompressToString(GZIPUtils.compress(str)));
    }

}


