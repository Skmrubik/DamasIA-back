package com.example.DamasIA.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Asegúrate de que esta configuración no haya eliminado las rutas por defecto
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}