package com.example.galaxia.Config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Sirve ./uploads en /media/** (con soporte para adelantar video y audio)
@Configuration
public class ConfigWeb implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ruta = Paths.get("uploads").toAbsolutePath().toUri().toString();
        if (!ruta.endsWith("/")) ruta += "/";
        registry.addResourceHandler("/media/**").addResourceLocations(ruta);
    }
}
