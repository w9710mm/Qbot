package com.mm.qbot.utils;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.bean.UserAgentList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description:
 * @date 2021/11/10 9:02
 */
public class WeiBoApi {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private final static List<String> USER_AGENT_LIST = UserAgentList.getInstance().getUaList();
    private final static Random RANDOM =new Random();
    public static JSONObject getNewDynamic(String uid,int page){
        String url=String.format("https://m.weibo.cn/api/container/getIndex?containerid=107603%s&page=%s",uid, page);
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent",USER_AGENT_LIST.get(RANDOM.nextInt(USER_AGENT_LIST.size())));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> res = REST_TEMPLATE.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(res.getBody());
    }
}
