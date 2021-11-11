package com.mm.qbot.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.config
 * @Description:
 * @date 2021/11/11 22:17
 */

@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
