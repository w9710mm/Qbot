package com.mm.qbot;

import com.mm.qbot.dto.User;
import com.mm.qbot.utils.DownLoadUtlis;
import org.junit.Test;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.LinkedHashSet;
import java.util.Set;

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
        Set<String> bids=new LinkedHashSet<>();

        bids.add("da");
        bids.add("dasdsa");
        bids.add("1");
        bids.add("dasdadsa");


        for (String a:bids) {
            System.out.println(a);
        }
        bids.remove("1");
        for (String a:bids) {
            System.out.println(a);
        }


    }
}
