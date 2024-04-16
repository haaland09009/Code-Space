package com.yyi.projectStudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 의미 : savePath에 있는 경로를 resourcePath에 적힌 이름으로 접근하겠다는 설정
    private String resourcePath = "/upload/**";
    private String savePath = "file:///C:/projectStudy_img/"; // 실제 파일 저장 경로

    // view에서 upload 경로로 접근하면 spring이 springboot_img 경로에서 해당 파일을 찾아주도록 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
}
