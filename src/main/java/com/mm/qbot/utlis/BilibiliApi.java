package com.mm.qbot.utlis;
import com.alibaba.fastjson.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BilibiliApi {
    RestTemplate restTemplate = new RestTemplate();

    private List<String> cookies=new ArrayList<>(){{
        add("SESSDATA=eb841f08%2C1650348175%2C6f67c*a1");
        add("buvid3=5506ADCE-2B87-4DB4-89DC-D49C45C5E6C0148828infoc");
        add("bili_jct=1d33dcf29ffcee20fd4cf2364264e95b");
    }};

    private   MultiValueMap<String, String> comHeaders = new LinkedMultiValueMap<>(){
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


    public JSONObject getNewDynamicNum(Integer typeList,Integer offset){
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

    public JSONObject getNewDynamic(Integer uid,Integer typeList,Integer currentDynamicId, String from,String platform ){
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



    public JSONObject serachUser(String keyword,String searchType ){
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














}
