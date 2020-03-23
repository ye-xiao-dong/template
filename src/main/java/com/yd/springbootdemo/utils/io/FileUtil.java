package com.yd.springbootdemo.utils.io;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;

public class FileUtil {
    /**
     * 将整个文件的内容读入一个字符串变量
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String read(String filePath) throws IOException {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        String str;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        while (null != (str = br.readLine())) {
            sb.append(str + lineSeparator);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 将字符串内容写入文件(覆盖写入)
     *
     * @param filePath
     * @param content
     */
    public static void write(String filePath, String content) {
        System.out.println("file = " + filePath);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.write(content);
        pw.flush();
        pw.close();
    }

    /**
     * 将字符串内容写入文件(追加写入)
     *
     * @param filePath
     * @param content
     */
    public static void append(String filePath, String content) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(filePath, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.write(content);
        pw.flush();
        pw.close();
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void unlink(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 上传文件
     * @param filePath 文件的本地路径
     * @param fileFlag 上传文件的参数标识，一般为"file"
     * @param url   上传接口的地址
     * @param extraParams   额外附加的参数
     * @return
     * @throws Exception
     */
    public static boolean uploadFile(String filePath, String fileFlag, String url, HashMap<String, Object> extraParams) throws Exception {

        File tempFile = new File(filePath);
        FileSystemResource resource = new FileSystemResource(tempFile);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        for (HashMap.Entry<String, Object> entity : extraParams.entrySet()) {
            param.add(entity.getKey(), entity.getValue());
        }
        param.add(fileFlag, resource);

        RestTemplate rest = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.POST, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new Exception(responseEntity.getBody());
        }
        return true;
    }

    /**
     * 上传数据到云存储
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static boolean upload(String url, MultiValueMap<String, Object> param) throws Exception {
        RestTemplate rest = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.POST, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new Exception(responseEntity.getBody());
        }
        return true;
    }

}
