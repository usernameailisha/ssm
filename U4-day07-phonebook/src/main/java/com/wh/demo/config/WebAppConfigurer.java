package com.wh.demo.config;

import com.wh.demo.interceptor.SysInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截配置--调用链
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    //用来配置那些请求的URL被拦截，哪些不拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] strings = {"/admin/login", "/*.html", "/image"};
        //添加拦截器，以SysInterceptor中为准
        registry.addInterceptor(new SysInterceptor())
                .addPathPatterns("/**").excludePathPatterns(strings);
    }
}
