package com.mm.qbot.controller;

import org.springframework.scheduling.annotation.Async;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.controller
 * @Description:
 * @date 2021/10/30 12:00
 */
public class TT {

    @Async
    public void t()
    {
        System.out.println(Thread.currentThread().getName());
    }
}
