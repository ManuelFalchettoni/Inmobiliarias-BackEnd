package com.manuel.zaguan_inmobiliarias.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Path root;
    private final String publicPath;

    public WebConfig(@Value("${app.photos.dir}") String dir,
                             @Value("${app.photos.public-path}") String publicPath) {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        this.publicPath = publicPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String location = root.toUri().toString();
        if (!location.endsWith("/")){
            location = location + "/";
        }

        registry.addResourceHandler(publicPath + "/**")
                .addResourceLocations(location);
    }
}
