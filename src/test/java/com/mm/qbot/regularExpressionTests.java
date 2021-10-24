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
        String line = "啊啊啊BV1Z3411C7yb111";
        String pattern = "[Bb][Vv][a-zA-Z0-9]{10}";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );

        } else {
            System.out.println("NO MATCH");
        }
    }
    @Test
    void ShortID1(){
        String line = "https:\\/\\/b23.tv\\/mONXO1";
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
        if (m.find( )) {
            System.out.println("Found value: " + m.group(1) );

        } else {
            System.out.println("NO MATCH");
        }
    }
}
