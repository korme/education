package korme.xyz.education.common.config;


import korme.xyz.education.common.interceptor.LoginURLInterceptor;

import korme.xyz.education.mapper.UserMapper;
import org.apache.catalina.SessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

@Configuration
public class SessionConfiguration implements  WebMvcConfigurer {
    @Bean
    public LoginURLInterceptor userInterceptor() {
        return new LoginURLInterceptor();
    }




    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration loginRegistry = registry.addInterceptor(userInterceptor());
        // 拦截路径
        loginRegistry.addPathPatterns("/**");
        // 排除路径
        //loginRegistry.excludePathPatterns("/");
        loginRegistry.excludePathPatterns("/user/login");
        loginRegistry.excludePathPatterns("/user/loginError");
        loginRegistry.excludePathPatterns("/user/changePassWord");
        //todo:用于测试
        //loginRegistry.excludePathPatterns("/test");

        // 排除资源请求
        loginRegistry.excludePathPatterns("/css/login/*.css");
        loginRegistry.excludePathPatterns("/js/login/**/*.js");
        loginRegistry.excludePathPatterns("/image/login/*.png");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }

}
