package com.manuel.zaguan_inmobiliarias.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LocalPhotoStorage  implements PhotoStorage{

    private final Path root;
    private final String publicPath;


    @Override
    public String store(MultipartFile file) {
        return "";
    }

    @Override
    public void delete(String url) {

    }

    public LocalPhotoStorage(@Value("${app.photos.dir}") String dir,
                             @Value("${app.photos.public-path}") String publicPath) {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        this.publicPath = publicPath;
    }
}
