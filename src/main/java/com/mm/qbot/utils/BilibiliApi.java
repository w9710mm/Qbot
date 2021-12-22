package com.mm.qbot.utils;
import com.alibaba.fastjson.JSONObject;


import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.enumeration.RelationActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.json.Json;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class BilibiliApi {
    private static RestTemplate restTemplate = new RestTemplate();

    private static List<String> cookies = new ArrayList<>() {{
        add("SESSDATA=9268e547%2C1655728675%2C3f174%2Ac1");
        add("buvid3=972DD461-3A0C-4FEB-B265-CF4102362DC918533infoc");
        add("bili_jct=f410d9ad7c7166593db1a8078d728891");
    }};

    private static String csrf = "f410d9ad7c7166593db1a8078d728891";
    private static MultiValueMap<String, String> comHeaders = new LinkedMultiValueMap<>() {
        {
            add("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            add("referer", "https://www.bilibili.com/");
        }
    };


    //TODO根据登录情况刷新cookie
    public boolean reNewCookie() {
        return false;
    }

    //TODO根据情况刷新cookie
    public boolean reNewHeader() {
        return false;
    }


    public static JSONObject getNewDynamicNum(String typeList, String offset) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String dynamicSvr = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/web_cyclic_num?type_list={type_list}&offset={offset}";


        Map<String, String> paramMaps = new HashMap<>();
        paramMaps.put("type_list", typeList);
        paramMaps.put("offset", offset);
        ResponseEntity<String> res;
        JSONObject jsonObject;
        try {
            res = restTemplate.exchange(dynamicSvr, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return jsonObject;

    }

    public static JSONObject getNewDynamic(String uid, String typeList, String currentDynamicId, String from, String platform) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String url = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid={uid}&type_list={type_list}" +
                "&current_dynamic_id={current_dynamic_id}&from={from}&platform={platform}";


        Map<String, String> paramMaps = new HashMap<>(5);
        paramMaps.put("uid", uid);
        paramMaps.put("type_list", typeList);
        paramMaps.put("current_dynamic_id", currentDynamicId);
        paramMaps.put("from", from);
        paramMaps.put("platform", platform);

        ResponseEntity<String> res ;


        JSONObject jsonObject;
        try {
            res =  restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }


    public static JSONObject serachUser(String keyword, String searchType) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        String url = "https://api.bilibili.com/x/web-interface/search/type?keyword={keyword}&search_type={search_type}";


        Map<String, Object> paramMaps = new HashMap<>(2);
        paramMaps.put("keyword", keyword);
        paramMaps.put("search_type", searchType);



        ResponseEntity<String> res ;

        JSONObject jsonObject=new JSONObject();
        try {
            res =  restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(res.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static JSONObject getVideoByBid(String bid) {
        String url = "https://api.bilibili.com/x/web-interface/view?bvid={bvid}";
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);


        Map<String, Object> paramMaps = new HashMap<>(1);
        paramMaps.put("bvid", bid);



        ResponseEntity<String> res ;

        JSONObject jsonObject=new JSONObject();
        try {
            res =  restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;

    }


    //获取短链接
    public static JSONObject getShortLink(String url) throws BilibiliException {
        url = "https://b23.tv/" + url;
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);


        ResponseEntity<String> res = restTemplate.postForEntity(url, httpEntity, String.class);

        if (res.getStatusCodeValue() == 302) {
//        res.getHeaders().get("Location").ts;


            String regex = "(dynamic|video|read/mobile)(/)([a-zA-Z0-9]+)";


            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(res.getHeaders().get("Location").toString());
            String realUrl = null;
            switch (matcher.group(1)) {
                case ("dynamic"):
                    realUrl = String.format("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=%s", matcher.group(3));
                    break;
                case ("video"):
                    return getVideoByBid(matcher.group(2));

                case ("read/mobile"):

                    //处理文章
                    break;

                default:
                    throw new BilibiliException("获取动态url错误：该资源已经失效");

            }

            res = restTemplate.exchange(realUrl, HttpMethod.GET, httpEntity, String.class);
            return JSONObject.parseObject(res.getBody());


        } else {
            log.error(url);
            throw new BilibiliException("获取动态url错误：不是正确的短链接"+url);
        }
    }


    public static JSONObject getDynamicCard(String id) {
        String url = String.format("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=%s", id);

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> res ;

        JSONObject jsonObject=new JSONObject();
        try {
            res =  restTemplate.postForEntity(url, httpEntity, String.class);
            jsonObject=JSONObject.parseObject(res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;

    }

    public static String getRealLink(String url) {

        url = "https://b23.tv/" + url;
        String realUrl = null;
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> res = restTemplate.postForEntity(url, httpEntity, String.class);
        if (res.getStatusCodeValue() == 302) {
            realUrl = res.getHeaders().get("Location").toString();
        }
        return realUrl;
    }


    public static JSONObject modifyRelation(Long uid, RelationActionEnum action) {

        String url = "https://api.bilibili.com/x/relation/modify";

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
//        headers.put(HttpHeaders.COOKIE,cookies);
        headers.put(HttpHeaders.COOKIE, cookies);


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(5);

        body.add("fid", String.valueOf(uid));
        body.add("act", String.valueOf(action.getId()));
        body.add("re_src", String.valueOf(11));
        body.add("csrf", csrf);
        body.add("csrf_token", csrf);


        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> exchange;
        JSONObject jsonObject=new JSONObject();
        try {
            exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            jsonObject=JSONObject.parseObject(exchange.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }


        return jsonObject;
    }


    public static JSONObject getSpaceDynamic(Long hostUid, Long offsetDynamicId, NeedTopEnum needTopEnum) {
        String url = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?host_uid=" +
                "{hostUid}&offset_dynamic_id={offsetDynamicId}&need_top={needTopEnum}&platform=web";

        HttpHeaders headers = new HttpHeaders();

        headers.addAll(comHeaders);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);

        Map<String, Object> paramMaps = new HashMap<>(3);
        paramMaps.put("hostUid", hostUid);
        paramMaps.put("offsetDynamicId", offsetDynamicId);
        paramMaps.put("needTopEnum", needTopEnum.getId());

        ResponseEntity<String> exchange ;


        JSONObject jsonObject=new JSONObject();
        try {
            exchange =   restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(exchange.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;

    }

    public static JSONObject getSpaceVideo(String hostUid) {
        String url = "https://api.bilibili.com/x/space/arc/search?mid={hostUid}&ps=30&tid=0&pn=1&keyword=&order=pubdate&jsonp=jsonp";

        HttpHeaders headers = new HttpHeaders();

        headers.addAll(comHeaders);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);

        Map<String, Object> paramMaps = new HashMap<>(1);
        paramMaps.put("hostUid", hostUid);


        ResponseEntity<String> exchange ;


        JSONObject jsonObject=new JSONObject();
        try {
            exchange =    restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(exchange.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;

    }


    public static JSONObject getUserInfo(Long uid) {

        String url = "https://api.bilibili.com/x/space/acc/info?mid={uid}";

        HttpHeaders headers = new HttpHeaders();

        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE, cookies);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);

        Map<String, Object> paramMaps = new HashMap<>(1);

        paramMaps.put("uid", uid);

        ResponseEntity<String> exchange ;

        JSONObject jsonObject=new JSONObject();
        try {
            exchange =   restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(exchange.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;
    }

    public static JSONObject getUpStat(String uid){
        String url = "https://api.bilibili.com/x/space/upstat?mid={uid}";

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
        headers.put(HttpHeaders.COOKIE, cookies);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);
        Map<String, Object> paramMaps = new HashMap<>(1);
        paramMaps.put("uid", uid);
        ResponseEntity<String> exchange ;
        JSONObject jsonObject=new JSONObject();
        try {
            exchange =   restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(exchange.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;
    }


    public static JSONObject getRelationStat(String uid){
        String url = "https://api.bilibili.com/x/relation/stat?vmid={uid}";

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(comHeaders);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);
        Map<String, Object> paramMaps = new HashMap<>(1);
        paramMaps.put("uid", uid);
        ResponseEntity<String> exchange ;
        JSONObject jsonObject=new JSONObject();
        try {
            exchange =   restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMaps);
            jsonObject=JSONObject.parseObject(exchange.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;
    }





}