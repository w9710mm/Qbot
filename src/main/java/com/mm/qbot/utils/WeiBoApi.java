package com.mm.qbot.utils;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.dto.UserAgentList;
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
        USER_AGENT_LIST.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        USER_AGENT_LIST.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)");
        USER_AGENT_LIST.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1 QQBrowser/6.9.11079.201");
        USER_AGENT_LIST.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        USER_AGENT_LIST.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 QIHU 360SE");

        headers.add("user-agent",USER_AGENT_LIST.get(RANDOM.nextInt(USER_AGENT_LIST.size())));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> res = REST_TEMPLATE.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(res.getBody());
    }
}
