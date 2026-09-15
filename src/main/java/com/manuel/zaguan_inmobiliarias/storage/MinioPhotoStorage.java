package com.manuel.zaguan_inmobiliarias.storage;

import com.manuel.zaguan_inmobiliarias.exception.property.photo.InvalidPhotoException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PhotoStorageException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

@Component
public class MinioPhotoStorage implements PhotoStorage {

    private final MinioClient minioClient;
    private final String bucket;
    private final String publicUrl;
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "webp");

    public MinioPhotoStorage(MinioClient minioClient,
                             @Value("${minio.url}") String url,
                             @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        //Asi queda la URL de cada foto: http://localhost:9000/photos/<uuid>.jpg
        this.publicUrl = url + "/" + bucket;
        createBucketIfNotExists();
    }

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

        //5 Subir a MinIO. El -1 es el tamaño de las partes: con el tamaño del archivo MinIO lo calcula solo
        try (InputStream input = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(input, file.getSize(), -1L)
                    .contentType(file.getContentType())
                    .build());
        } catch (IOException | MinioException e) {
            throw new PhotoStorageException("File could not be saved", e);
        }

        //6 La URL que se guarda en la base
        return publicUrl + "/" + fileName;
    }

    @Override
    public void delete(String url) {
        //Si la URL no es la nuestra no se hace nada
        if (url == null || !url.startsWith(publicUrl + "/")) {
            return;
        }

        //Nos quedamos con lo que viene despues de ".../photos/"
        String fileName = url.substring(publicUrl.length() + 1);

        //Borrar. MinIO no da error si el archivo ya no esta
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build());
        } catch (MinioException e) {
            throw new PhotoStorageException("File could not be deleted", e);
        }
    }

    private void createBucketIfNotExists() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            //Lectura publica: cualquiera puede ver las fotos con la URL, pero solo el backend sube y borra
            String policy = """
                    {
                      "Version": "2012-10-17",
                      "Statement": [{
                        "Effect": "Allow",
                        "Principal": {"AWS": ["*"]},
                        "Action": ["s3:GetObject"],
                        "Resource": ["arn:aws:s3:::%s/*"]
                      }]
                    }
                    """.formatted(bucket);
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build());
        } catch (MinioException e) {
            throw new PhotoStorageException("Could not prepare the MinIO bucket: " + bucket, e);
        }
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        int dot = originalFilename.lastIndexOf('.');
        return dot < 0 ? "" : originalFilename.substring(dot + 1).toLowerCase();
    }
}
