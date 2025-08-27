package com.meclist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String caminhoUpload = Paths.get("C:/Users/usuário/OneDrive/Documentos/GitHub/Beckend/uploads/")
                                    .toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(caminhoUpload);
    }
}
