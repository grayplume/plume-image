package com.plume.image.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     // 配置静态资源处理规则
    //     // registry.addResourceHandler("/static/**") // 所有以"/static/"开头的请求
    //     //         .addResourceLocations("classpath:/static/"); // 从类路径的/static/目录下寻找资源
    //     // 可以根据需要添加更多资源处理规则
    // }
}
