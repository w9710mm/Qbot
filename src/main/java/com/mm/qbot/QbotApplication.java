package com.mm.qbot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author meme
 */
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@Configuration
@MapperScan("com.mm.qbot.dao.mapper")
@EnableAsync(proxyTargetClass = true)
public class QbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(QbotApplication.class, args);

    }

}
