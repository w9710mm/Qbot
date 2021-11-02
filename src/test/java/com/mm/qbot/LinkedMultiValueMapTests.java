package com.mm.qbot;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description:
 * @date 2021/11/2 23:06
 */
public class LinkedMultiValueMapTests {

    private LinkedMultiValueMap<String, String> map;

    @Before
    public void setUp() {
        map = new LinkedMultiValueMap<>();
    }

@Test
    public void add() {
        map.add("key", "value1");
        map.add("key", "value1");


        System.out.println(map.toString());
    }

}
