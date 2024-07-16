package com.ztt.common.util;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TranslateDocumentsUtil {

    private static final Logger logger = LoggerFactory.getLogger(TranslateDocumentsUtil.class);

    private static final String YOUDAO_URL_UPLOAD = "https://openapi.youdao.com/file_trans/upload";

    private static final String YOUDAO_URL_QUERY = "https://openapi.youdao.com/file_trans/query";

    private static final String YOUDAO_URL_DOWNLOAD = "https://openapi.youdao.com/file_trans/download";

    private static final String APP_KEY = "6e4ec153ea474906";

    private static final String APP_SECRET = "HRQfjmMYhtVfg6OswRDRr0hqM6xB8rko";

    public static void main(String[] args) throws IOException {
        upload();
        query();
        download();
    }

    public static void upload() throws IOException {
        Map<String, String> params = new HashMap<>();
        String q = loadAsBase64("/Users/zhangtingting/myfile/干货资料/分布式/[分布式Java应用基础与实践].林昊.扫描版.pdf");
        String salt = String.valueOf(System.nanoTime());
        String curtime = String.valueOf(System.nanoTime() / 1000);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("q", q);
        params.put("fileName", "[分布式Java应用基础与实践].林昊.扫描版.pdf");
        params.put("fileType", "pdf");
        params.put("langFrom", "简体中文");
        params.put("langTo", "英语");
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        params.put("curtime", curtime);
        params.put("sign", sign);
        params.put("docType", "json");
        params.put("signType", "v3");
        String result = requestForHttp(YOUDAO_URL_UPLOAD, params);
        System.out.println(result);
    }

    public static void query() throws IOException {
        Map<String, String> params = new HashMap<>();
        String flownumber = "文件流水号";
        String salt = String.valueOf(System.nanoTime());
        String curtime = String.valueOf(System.nanoTime() / 1000);
        String signStr = APP_KEY + truncate(flownumber) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("flownumber", flownumber);
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        params.put("curtime", curtime);
        params.put("sign", sign);
        params.put("docType", "json");
        params.put("signType", "v3");
        String result = requestForHttp(YOUDAO_URL_QUERY, params);
        /** 处理结果 */
        System.out.println(result);
    }

    public static void download() throws IOException {
        Map<String, String> params = new HashMap<>();
        String flownumber = "文件流水号";
        String salt = String.valueOf(System.nanoTime());
        String curtime = String.valueOf(System.nanoTime() / 1000);
        String signStr = APP_KEY + truncate(flownumber) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("flownumber", flownumber);
        params.put("downloadFileType", "文件下载类型");
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        params.put("curtime", curtime);
        params.put("sign", sign);
        params.put("docType", "json");
        params.put("signType", "v3");
        String result = requestForHttp(YOUDAO_URL_DOWNLOAD, params);
        /** 处理结果 */
        System.out.println(result);
    }

    public static String requestForHttp(String url, Map<String, String> params) throws IOException {
        String result;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> en : params.entrySet()) {
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
        }
        return result;
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String loadAsBase64(String imgFile) {//将文件转化为字节数组字符串，并对其进行Base64编码处理

        File file = new File(imgFile);
        if (!file.exists()) {
            logger.error("文件不存在");
            return null;
        }
        byte[] data = null;
        //读取文件字节数组
        try (InputStream in = new FileInputStream(imgFile)) {
            data = new byte[in.available()];
            int read = in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        return Base64.getEncoder().encodeToString(data);//返回Base64编码过的字节数组字符串
    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
