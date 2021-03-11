package ru.gurzhiy.crawler.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.gurzhiy.crawler.concurrent.Pair;

import java.util.concurrent.Future;

@Configuration
public class BasicConfiguration {


//    executor для поисковика
    @Bean("crawlerPool")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setCorePoolSize(3);
        executor.setThreadNamePrefix("CrawlerPool-");
        executor.initialize();
        return executor;
    }

    //    executor для асинхронной работы сервлета
    @Bean("dashboardControllerPool")
    public ThreadPoolTaskExecutor controllerTPTExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setCorePoolSize(2);
        executor.setThreadNamePrefix("ControllerPool-");
        executor.initialize();
        return executor;
    }


}
