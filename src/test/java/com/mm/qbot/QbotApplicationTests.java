package com.mm.qbot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.utlis.QrcodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.Map;

class QbotApplicationTests {
    private  RestTemplate restTemplate=new RestTemplate();
    @Test
    void bilibili() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("referer","https://www.bilibili.com/");
        headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        String islogin="https://account.bilibili.com/home/userInfo";
        String url="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResponseEntity<String> QrresponseEntity = restTemplate.getForEntity(url,httpEntity ,String.class);


        String user = QrresponseEntity.getBody();
        assert user != null;

       JSONObject json = JSONObject.parseObject(user);
        json.getInteger("coded");
        String data = json.getJSONObject("data").getString("url");
        try {
            QrcodeUtils.createQrcode(data, Path.of("1.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEntity<String> isloginRe = restTemplate.getForEntity(islogin, String.class);
        System.out.println(isloginRe);


//        System.out.print(json.toJSONString());


    }

}
