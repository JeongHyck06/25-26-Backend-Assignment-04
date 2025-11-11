package com.gdg.jwtexample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${api.prefix:/api}")
    private String apiPrefix;

    public void configurePathMatching(PathMatchConfigurer configurer) {
        // @RestController가 붙은 모든 컨트롤러에 접두사 적용
        configurer.addPathPrefix(apiPrefix, c -> c.isAnnotationPresent(RestController.class));
    }
}
