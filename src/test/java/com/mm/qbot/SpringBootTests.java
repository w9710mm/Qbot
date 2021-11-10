package com.mm.qbot;

import com.mm.qbot.utils.DownLoadUtlis;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SpringBootTests{
    @Test
    public void downloadPic(){
        DownLoadUtlis.downloadPic("https://i1.hdslb.com/bfs/face/a57ad8498a08779d2a476abe14069bf8e17a5599.jpg@96w_96h_100Q_1c.jpg");
    }
@Test
public  void test1() {
        //获取文件的原始名称
        String originalFilename = "tim.g (1).jpg";//timg (1).jpg
        //获取最后一个.的位置
        int lastIndexOf = originalFilename.lastIndexOf(".");
        //获取文件的后缀名 .jpg
        String suffix = originalFilename.substring(0,lastIndexOf);

        System.out.println("suffix = " + suffix);
    }


    @Test
    public  void test2() {

        String url="http://stu.zstu.edu.cn/webroot/decision/view/report";


          List<String> cookies=new ArrayList<>(){{
            add("UM_distinctid=17b817a1f1c9c-02e174970f3e4-3e604809-1fa400-17b817a1f1d838");
            add("fine_remember_login=-1");
            add("fine_auth_token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmYW5ydWFuIiwiaWF0IjoxNjM2MTYxNjM1LCJleHAiOjE2MzYxNjUyMzUsInN1YiI6IjIwMjEzMDUwNDE2NyIsImRlc2NyaXB0aW9uIjoiWzczOGJdWzczOGJdWzViNWZdKDIwMjEzMDUwNDE2NykiLCJqdGkiOiJqd3QifQ.ax8IaPzB2K8tyGReEJ7j_UuiDezvI8HzRyAHcmOTPxU");

          }};


        MultiValueMap<String, String> comHeaders = new LinkedMultiValueMap<>();
        comHeaders.add("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmYW5ydWFuIiwiaWF0IjoxNjM2MTYxNjM1LCJleHAiOjE2MzYxNjUyMzUsInN1YiI6IjIwMjEzMDUwNDE2NyIsImRlc2NyaXB0aW9uIjoiWzczOGJdWzczOGJdWzViNWZdKDIwMjEzMDUwNDE2NykiLCJqdGkiOiJqd3QifQ.ax8IaPzB2K8tyGReEJ7j_UuiDezvI8HzRyAHcmOTPxU");
        comHeaders.add("P3P","CP=CAO PSA OUR");
        comHeaders.add("sessionID","980a96f7-e96b-4391-ab63-4b2a44776458");
        comHeaders.add("Origin","http://stu.zstu.edu.cn");
        comHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");

        comHeaders.add("X-Requested-With","XMLHttpRequest");



        HttpHeaders headers=new HttpHeaders();

        headers.put(HttpHeaders.COOKIE,cookies);
        headers.addAll(comHeaders);

        MultiValueMap<String, String> body= new LinkedMultiValueMap< >(5);

        body.add("op","fr_write");

        body.add("cmd","write_verify");

        body.add("reportXML","<?xml version=\"1.0\" encoding=\"UTF-8\" ?><WorkBook><Version>6.5</Version><Report class=\"com.fr.report.WorkSheet\" name=\"0\"><CellElementList><C c=\"5\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6c5f][5e72][533a][5d][5d]></O></C><C c=\"4\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][676d][5dde][5e02][5d][5d]></O></C><C c=\"5\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6c5f][5e72][533a][5d][5d]></O></C><C c=\"3\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6d59][6c5f][7701][5d][5d]></O></C><C c=\"5\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6c5f][5e72][533a][5d][5d]></O></C><C c=\"5\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6c5f][5e72][533a][5d][5d]></O></C><C c=\"4\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][676d][5dde][5e02][5d][5d]></O></C><C c=\"5\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6c5f][5e72][533a][5d][5d]></O></C><C c=\"5\" r=\"6\"><O t=\"S\"><![5b]CDATA[5b][6c5f][5e72][533a][5d][5d]></O></C><C c=\"3\" r=\"7\"><O t=\"S\"><![5b]CDATA[5b][6d59][6c5f][7406][5de5][5927][5b66][5d][5d]></O></C></CellElementList></Report></WorkBook>");

        body.add("reportIndex","0");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);



        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        System.out.println(exchange.getBody());

    }



    }

