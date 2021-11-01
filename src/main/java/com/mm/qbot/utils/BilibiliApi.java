package com.mm.qbot.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.mikuac.shiro.common.utils.RegexUtils;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.enumeration.RelationActionEnum;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.PatternMatchUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BilibiliApi {
    static RestTemplate restTemplate = new RestTemplate();

    private static List<String> cookies=new ArrayList<>(){{
        add("SESSDATA=1772f4fa%2C1648774194%2Ce2e12%2Aa1");
        add("buvid3=972DD461-3A0C-4FEB-B265-CF4102362DC918533infoc");
        add("bili_jct=2d09cbe5436f446b068bc7f1f6d1be86");
    }};

    private  static  String csrf="2d09cbe5436f446b068bc7f1f6d1be86";
    private static MultiValueMap<String, String> comHeaders = new LinkedMultiValueMap<>(){
        {
            add("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            add("referer","https://www.bilibili.com/");
        }
    };




    //TODO根据登录情况刷新cookie
    public boolean reNewCookie(){
        return false;
    }

    //TODO根据情况刷新cookie
    public boolean reNewHeader(){
        return false;
    }


    public static JSONObject getNewDynamicNum(String typeList,String offset){
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String dynamicSvr = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/web_cyclic_num?type_list={type_list}&offset={offset}";


        Map<String, String> paramMaps = new HashMap<>();
        paramMaps.put("type_list", typeList);
        paramMaps.put("offset", offset);


        ResponseEntity<String> res=restTemplate.exchange(dynamicSvr, HttpMethod.GET,httpEntity,String.class,paramMaps);
        return JSONObject.parseObject(res.getBody());
    }

    public  static JSONObject getNewDynamic(String uid,String typeList,String currentDynamicId, String from,String platform ){
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid={uid}&type_list={type_list}" +
                "&current_dynamic_id={current_dynamic_id}&from={from}&platform={platform}";


        Map<String, String> paramMaps = new HashMap<>();
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
    public static  JSONObject getShortLink(String url) throws BilibiliException{
        url="https://b23.tv/"+url;
        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);



       ResponseEntity<String> res=restTemplate.postForEntity(url,httpEntity,String.class);

       if (res.getStatusCodeValue()==302) {
//        res.getHeaders().get("Location").ts;


           String regex="(dynamic|video|read/mobile)(/)([a-zA-Z0-9]+)";


           Pattern pattern = Pattern.compile(regex);
           Matcher matcher = pattern.matcher(res.getHeaders().get("Location").toString());
            String realUrl=null;
           switch (matcher.group(1)){
               case ("dynamic"):
                   realUrl=  String.format("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=%s", matcher.group(3));
               break;
               case ("video"):
                   return getVideoByBid(matcher.group(2));

               case ("read/mobile") :

                   //处理文章
                   break;

               default:
                   throw new BilibiliException("获取动态url错误：该资源已经失效");

           }

               res = restTemplate.exchange(realUrl, HttpMethod.GET, httpEntity, String.class);
               return JSONObject.parseObject(res.getBody());


       }else {
           throw new BilibiliException("获取动态url错误：不是正确的短链接");
       }
    }


    public  static JSONObject modifyRelation(Integer uid, RelationActionEnum action){

        String url="https://api.bilibili.com/x/relation/modify";

        HttpHeaders headers=new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        headers.put(HttpHeaders.COOKIE,cookies);



        MultiValueMap<String, String> body= new LinkedMultiValueMap< >();

        body.add("fid",String.valueOf(uid));
        body.add("act", String.valueOf(action.getId()));
        body.add("re_src", String.valueOf(11));
        body.add("csrf",csrf);
        body.add("csrf_token",csrf);


        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);


       return JSONObject.parseObject(exchange.getBody());
    }







}
