package com.manuel.zaguan_inmobiliarias.storage;


import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorage {
    //Devuelve el objectKey con el que quedo guardado el archivo
    String store(MultipartFile file);

    //Para el DataSeeder, que sube las fotos de muestra sin pasar por un request.
    //No valida nada: los archivos salen del propio repo
    String store(byte[] content, String extension, String contentType);

    void delete(String objectKey);

    String urlOf(String objectKey);
}
