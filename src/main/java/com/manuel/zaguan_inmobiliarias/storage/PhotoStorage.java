package com.manuel.zaguan_inmobiliarias.storage;


import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorage {
    String store(MultipartFile file);

    void delete(String url);
}
