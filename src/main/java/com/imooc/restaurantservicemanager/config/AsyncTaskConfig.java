package com.imooc.restaurantservicemanager.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 17:08
 **/
@Configuration
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(10);
        threadPoolExecutor.setMaxPoolSize(100);
        threadPoolExecutor.setQueueCapacity(10);
        //线程池是否是任务全部完成才关闭
        threadPoolExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //空闲线程存活时间
        threadPoolExecutor.setKeepAliveSeconds(60);
        //线程的前缀名
        threadPoolExecutor.setThreadNamePrefix("Rabbit-Async-");
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
