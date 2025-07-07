package com.example.stockboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /charts/** => /app/charts/
        registry.addResourceHandler("/charts/**")
                .addResourceLocations("file:/app/charts/")
                .setCachePeriod(3600)
                .resourceChain(true);

        // /img/** => /app/static/img/ 
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:/app/static/img/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}
