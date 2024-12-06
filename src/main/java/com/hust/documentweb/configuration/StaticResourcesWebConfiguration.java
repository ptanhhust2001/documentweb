package com.hust.documentweb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String basePath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**")
                .addResourceLocations(basePath);
    }

}
