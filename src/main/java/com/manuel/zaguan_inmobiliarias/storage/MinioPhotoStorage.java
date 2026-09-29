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

    //El bucket se prepara en la primera subida, no en el constructor: si MinIO esta caido,
    //el bean igual se crea y la API arranca. Antes fallaba el bean y no levantaba nada,
    //ni siquiera los endpoints que no usan fotos
    private volatile boolean bucketReady = false;

    public MinioPhotoStorage(MinioClient minioClient,
                             @Value("${minio.url}") String url,
                             @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        //Asi queda la URL de cada foto: http://localhost:9000/photos/<uuid>.jpg
        this.publicUrl = url + "/" + bucket;
    }

    @Override
    public String store(MultipartFile file) {
        //1. El archivo tiene que venir con contenido
        if (file == null || file.isEmpty()) {
            throw new InvalidPhotoException("The file is empty");
        }

        //2. Extension permitida
        String extension = extensionOf(file.getOriginalFilename());
        if (!ALLOWED.contains(extension)) {
            throw new InvalidPhotoException("Invalid format: " + extension);
        }

        //3. El content-type tiene que ser de imagen: la extension la elige el cliente y se
        //puede renombrar cualquier archivo a .jpg. El bucket es de lectura publica, asi que lo
        //que entra se sirve tal cual
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidPhotoException("Invalid content type: " + contentType);
        }

        //4 Nombre nuevo, sin relacion con el original
        String fileName = UUID.randomUUID() + "." + extension;

        //5 Con MinIO caido esto tira PhotoStorageException y el endpoint responde 500
        ensureBucket();

        //6 Subir a MinIO. El -1 es el tamaño de las partes: con el tamaño del archivo MinIO lo calcula solo
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

        //7 El objectKey que se guarda en la base
        return fileName;
    }

    @Override
    public void delete(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return;
        }

        //Borrar. MinIO no da error si el archivo ya no esta
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build());
        } catch (MinioException e) {
            throw new PhotoStorageException("File could not be deleted", e);
        }
    }

    @Override
    public String urlOf(String objectKey) {
        return publicUrl + "/" + objectKey;
    }

    //Se ejecuta una sola vez: despues de la primera subida bucketReady queda en true.
    //volatile para que el valor se vea igual desde todos los hilos de Tomcat
    private void ensureBucket() {
        if (bucketReady) {
            return;
        }
        createBucketIfNotExists();
        bucketReady = true;
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
