package com.mm.qbot.utils;
import com.alibaba.fastjson.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BilibiliApi {
    static RestTemplate restTemplate = new RestTemplate();

    private static List<String> cookies=new ArrayList<>(){{
        add("SESSDATA=eb841f08%2C1650348175%2C6f67c*a1");
        add("buvid3=5506ADCE-2B87-4DB4-89DC-D49C45C5E6C0148828infoc");
        add("bili_jct=1d33dcf29ffcee20fd4cf2364264e95b");
    }};

    private static MultiValueMap<String, String> comHeaders = new LinkedMultiValueMap<>(){
        {
            add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            add("referer","https://www.bilibili.com/");
        }
    };


    //根据登录情况刷新cookie
    public boolean reNewCookie(){
        return false;
    }

    //根据情况刷新cookie
    public boolean reNewHeader(){
        return false;
    }


    public static JSONObject getNewDynamicNum(Integer typeList,Integer offset){
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String dynamicSvr = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/web_cyclic_num?type_list={type_list}&offset={offset}";


        Map<String, Integer> paramMaps = new HashMap<>();
        paramMaps.put("type_list", typeList);
        paramMaps.put("offset", offset);


        ResponseEntity<String> res=restTemplate.exchange(dynamicSvr, HttpMethod.GET,httpEntity,String.class,paramMaps);
        return JSONObject.parseObject(res.getBody());
    }

    public  static JSONObject getNewDynamic(Integer uid,Integer typeList,Integer currentDynamicId, String from,String platform ){
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid={uid}&type_list={type_list}" +
                "&current_dynamic_id={current_dynamic_id}&from={from}&platform={platform}";


        Map<String, Object> paramMaps = new HashMap<>();
        paramMaps.put("uid", uid);
        paramMaps.put("type_list", typeList);
        paramMaps.put("current_dynamic_id", currentDynamicId);
        paramMaps.put("from", from);
        paramMaps.put("platform", platform);

        ResponseEntity<String> res=restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class,paramMaps);
        return JSONObject.parseObject(res.getBody());
    }



    public static JSONObject serachUser(String keyword,String searchType ){
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String url="https://api.bilibili.com/x/web-interface/search/type?keyword={keyword}&search_type={search_type}";


        Map<String, Object> paramMaps = new HashMap<>();
        paramMaps.put("keyword", keyword);
        paramMaps.put("search_type", searchType);

        ResponseEntity<String> res=restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class,paramMaps);
        return JSONObject.parseObject(res.getBody());
    }




    public static  JSONObject getVideoByBid(String bid){
        String url="https://api.bilibili.com/x/web-interface/view?bvid={bvid}";
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);


        Map<String, Object> paramMaps = new HashMap<>();
        paramMaps.put("bvid", bid);



        ResponseEntity<String> res=restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class,paramMaps);
        return JSONObject.parseObject(res.getBody());

    }


    //获取短链接
    public static  JSONObject getShortLink(String url){
        url="https://b23.tv/"+url;
        HttpHeaders headers=new HttpHeaders();
        headers.add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
//        headers.put(HttpHeaders.COOKIE,cookies);
        headers.add("accpet","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.add("cache-control","no-cache");
        headers.add("cookie","__guid=227137742.4151832539650290700.1620915833928.5356" +
                "pragma: no-cache");
        headers.add("sec-fetch-dest","document");
        headers.add("sec-fetch-mode","navigate");
        headers.add("sec-fetch-site","none");
        headers.add("sec-fetch-user","?1");
        headers.add("upgrade-insecure-requests","1");

        JSONObject result = new JSONObject();

       HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);




       ResponseEntity<String> res=restTemplate.postForEntity(url,httpEntity,String.class);
        System.out.println(res.toString());
        return JSONObject.parseObject(res.getBody());

    }







}
