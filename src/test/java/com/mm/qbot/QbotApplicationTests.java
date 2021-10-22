package com.mm.qbot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.QrcodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QbotApplicationTests {

    RestTemplate restTemplate = new RestTemplate();

    @Test
    void Update() {
        Map<String,String> map = new HashMap();
        List<String> cookies=new ArrayList<>();
        cookies.add("bili_jct=1d33dcf29ffcee20fd4cf2364264e95b");
        cookies.add("buvid3=5506ADCE-2B87-4DB4-89DC-D49C45C5E6C0148828infoc");
        cookies.add("SESSDATA=eb841f08%2C1650348175%2C6f67c*a1");
        String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/web_cyclic_num?type_list=268435455";

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        headers.add("referer","https://www.bilibili.com/");
        headers.put(HttpHeaders.COOKIE,cookies);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> res=restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class);
        System.out.println((res.getBody()));
    }

    @Test
    void getNew() {
        Map<String,String> map = new HashMap();
        List<String> cookies=new ArrayList<>();
        cookies.add("bili_jct=ef3982d151ce01f59d8da231b34f5a0a");
        cookies.add("buvid3=5506ADCE-2B87-4DB4-89DC-D49C45C5E6C0148828infoc");
        cookies.add("SESSDATA=9c5addb4%2C1650361348%2Ca8187%2Aa1");
        String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid=1359202&type_list=268435455&current_dynamic_id=583955397224211538&from=weball&platform=web";

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        headers.add("referer","https://search.bilibili.com/upuser?keyword=%E7%AD%89%E7%AD%89&from_source=web_search");

        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> res=restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class);
        System.out.println((res.getBody()));
        JSONObject dyn=JSONObject.parseObject(res.getBody());

        JSONArray cards = dyn.getJSONObject("data").getJSONArray("cards");
        for (Object c:cards) {
            JSONObject card = (JSONObject) c;
            BilibiliStrategy.dynamicStrategy(card);
        }
        
    }


    @Test
    void serach() {
        Map<String,String> map = new HashMap();
        List<String> cookies=new ArrayList<>();
        cookies.add("bili_jct=1d33dcf29ffcee20fd4cf2364264e95b");
        cookies.add("buvid3=5506ADCE-2B87-4DB4-89DC-D49C45C5E6C0148828infoc");
        cookies.add("SESSDATA=eb841f08%2C1650348175%2C6f67c*a1");

        String url ="https://api.bilibili.com/x/web-interface/search/type?keyword=等等&search_type=bili_user";
        HttpHeaders headers = new HttpHeaders();

//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        headers.add("referer","https://search.bilibili.com/upuser?keyword=%E7%AD%89%E7%AD%89&from_source=web_search");
        ResponseEntity<String> res=restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class);
        System.out.println((res.getBody()));
    }


    @Test
    void bilibili() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("referer","https://www.bilibili.com/");
        headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        String islogin="https://account.bilibili.com/home/userInfo";
        String url="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResponseEntity<String> QrresponseEntity = restTemplate.exchange(url,HttpMethod.GET ,httpEntity,String.class);


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
