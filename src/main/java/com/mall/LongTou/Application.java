package com.mall.LongTou;

import com.mall.LongTou.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 15:43
 * @Description:
 */
@SpringBootApplication

@EnableScheduling
public class Application {
        public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
        }

        @Bean
        public IdWorker getIdWork() {
                return new IdWorker();
        }

}
