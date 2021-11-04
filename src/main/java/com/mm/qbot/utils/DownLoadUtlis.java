package com.mm.qbot.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownLoadUtlis {

    public static String downloadPic(String url){

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);

        byte[] body = responseEntity.getBody();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("F:1.jpg");

            fileOutputStream.write(body);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭流
        return null;

    }
}
