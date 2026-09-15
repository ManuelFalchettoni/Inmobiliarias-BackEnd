package com.manuel.zaguan_inmobiliarias.storage;


import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorage {
    //Devuelve el objectKey con el que quedo guardado el archivo
    String store(MultipartFile file);

    void delete(String objectKey);

    String urlOf(String objectKey);
}
