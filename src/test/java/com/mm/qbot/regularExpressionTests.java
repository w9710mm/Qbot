package com.mm.qbot;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WWM
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description: 测试正则表达式
 * @date 2021/10/23 15:22
 */
public class regularExpressionTests {

    @Test
    void Testbvid(){
        String line = "woc，puredye小团体聊天记录曝光了 http://t.cn/A6xQ424X\n" +
                "恐怖如斯\n";
        String pattern = "(b23\\.tv/|t\\.cn/)([a-zA-Z0-9]{1,8})";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(2) );

        } else {
            System.out.println("NO MATCH");
        }
    }
    @Test
    void ShortID1(){
        String line = "https://b23.tv/0aYOXj?share_medium=android&amp;share_source=qq&amp;bbid=XY771FD75454244367C646EF7BE117FC8E5AF&amp;ts=1636172649863";
        String pattern = "b23\\.tv\\\\/([a-zA-Z0-9]{6})";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        System.out.println(line);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(1) );

        } else {
            System.out.println("NO MATCH");
        }
    }
    @Test
    void ShortID2(){
        String line = "https://b23.tv/XRT4wU";
        String pattern = "b23\\.tv/([a-zA-Z0-9]{6})";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        System.out.println(line);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.group(1) );

        } else {
            System.out.println("NO MATCH");
        }
    }

    @Test
    void dynamicId(){
        String line = "[https://m.bilibili.com/dynamic/585186330557240439?share_medium=android&share_plat=android&share_session_id=7b857e0d-d64d-4bc8-ac4d-d11b1d4568a6&share_source=COPY&share_tag=s_i&timestamp=1635087786&unique_k=DBCvoG]";
        String line2 = "[https://www.bilibili.com/video/BV1Vu411o72y?p=1&share_medium=android&share_plat=android&share_session_id=987bfc0b-71f1-4857-b3fc-60be7af884fb&share_source=COPY&share_tag=s_i&timestamp=1635087654&unique_k=7vtUI8]";

        String pattern = "(dynamic|video)(/)([a-zA-Z0-9]+)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        System.out.println(line2);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line2);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(1) );

        } else {
            System.out.println("NO MATCH");
        }
    }


    @Test
    void test订阅(){

        String pattern = "(\\b群订阅动态 )(\\S+)";
        String line="群订阅动态 嘉然   ";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        System.out.println(line);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(2) );

        } else {
            System.out.println("NO MATCH");
        }
    }


    @Test
    void testnum(){

        String pattern = "(等等 )([0-9]+)";
        String line="等等 01   ";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        System.out.println(line);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + Integer.valueOf(m.group(2)) );

        } else {
            System.out.println("NO MATCH");
        }
    }

    @Test
    void testdym(){

        String pattern = "t\\.bilibili\\.com/([0-9]+)";
        String line="https://t.bilibili.com/587736570472853324";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        System.out.println(line);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(1) );

        } else {
            System.out.println("NO MATCH");
        }
    }



}
