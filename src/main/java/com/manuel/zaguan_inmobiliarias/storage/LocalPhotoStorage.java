package com.manuel.zaguan_inmobiliarias.storage;

import com.manuel.zaguan_inmobiliarias.exception.property.photo.InvalidPhotoException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PhotoStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Component
public class LocalPhotoStorage implements PhotoStorage {

    private final Path root;
    private final String publicPath;
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "webp");


    @Override
    public String store(MultipartFile file) {
        //1. El archivo tiene que venir con contenido
        if (file == null || file.isEmpty()) {
            throw new InvalidPhotoException("The file is empty");
        }

        //2 y 3. extension permitida
        String extension = extensionOf(file.getOriginalFilename());
        if (!ALLOWED.contains(extension)) {
            throw new InvalidPhotoException("Invalid format: " + extension);
        }

        //4 Nombre nuevo, sin relacion con el original
        String fileName = UUID.randomUUID() + "." + extension;

        //5 Escribir en disco
        try (InputStream input = file.getInputStream()) {
            Files.createDirectories(root); //crea uploads/photos
            Files.copy(input, root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);//pega la carpeta con el nombre del archivo
        } catch (IOException e) {
            throw new PhotoStorageException("File could not be saved", e);
        }

        //6 La URL que se guarda en la base

        return publicPath + "/" + fileName;

    }

    @Override
    public void delete(String url) {
        //Si la URL no es la nuestra no se hace nada
        if (url == null || !url.startsWith(publicPath + "/")) {
            return;
        }

        //Nos quedamos con lo que viene despues de "/photos/"
        String fileName = url.substring(publicPath.length() + 1);
        Path target = root.resolve(fileName).normalize();

        //Que no se haya salido de la carpeta
        if (!target.startsWith(root)) {
            throw new PhotoStorageException("Invalid path: " + url);
        }

        //Borrar, ignora si ya no esta
        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new PhotoStorageException("File could not be deleted", e);
        }
    }

    public LocalPhotoStorage(@Value("${app.photos.dir}") String dir,
                             @Value("${app.photos.public-path}") String publicPath) {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        this.publicPath = publicPath;
    }


    private String extensionOf(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        int dot = originalFilename.lastIndexOf('.');
        return dot < 0 ? "" : originalFilename.substring(dot + 1).toLowerCase();
    }
}
