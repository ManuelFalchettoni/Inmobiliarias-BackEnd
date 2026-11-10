package com.manuel.zaguan_inmobiliarias.config.seed;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PhotoStorageException;
import com.manuel.zaguan_inmobiliarias.repository.property.photo.JpaPropertyPhotoRepository;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

//Fotos de muestra del DataSeeder. Salen de src/main/resources/seed-photos/<categoria>/,
//asi viajan con el repo y todas las compus quedan con las mismas.
//Categorias: exterior (fachadas), interior (ambientes de vivienda), office (oficinas y locales).
//Cada propiedad recibe su propia copia en MinIO: si compartieran el objectKey, borrar la foto
//de una propiedad la borraria tambien de las otras
@Slf4j
@Component
@RequiredArgsConstructor
public class SeedPhotos {

    private static final String LOCATION = "classpath*:seed-photos/*/*.*";

    private final PhotoStorage photoStorage;
    private final JpaPropertyPhotoRepository jpaPropertyPhotoRepository;

    private record SamplePhoto(String category, String name, String extension, byte[] content) {
    }

    private Map<String, List<SamplePhoto>> byCategory;

    //Si MinIO no responde se apaga en la primera falla: el resto del seed se carga sin fotos
    private boolean available = true;

    //Sube entre count-1 y count+1 fotos y las asocia a la propiedad, que ya tiene que estar guardada
    public void attach(Property property, int count, Random random) {
        if (!available || count <= 0) {
            return;
        }
        List<SamplePhoto> chosen = choose(property.getType(), count + random.nextInt(3) - 1, random);

        int position = 0;
        for (SamplePhoto sample : chosen) {
            String objectKey;
            try {
                objectKey = photoStorage.store(sample.content(), sample.extension(), contentTypeOf(sample.extension()));
            } catch (PhotoStorageException e) {
                log.warn("DataSeeder: MinIO no responde, el resto de los datos se carga sin fotos ({})", e.getMessage());
                available = false;
                return;
            }

            PropertyPhoto photo = new PropertyPhoto();
            photo.setObjectKey(objectKey);
            photo.setPhotoName(sample.name());
            photo.setPosition(position++);
            photo.setProperty(property);
            jpaPropertyPhotoRepository.save(photo);
        }
    }

    //Sube el logo de muestra src/main/resources/seed-logos/<fileName> y devuelve su objectKey.
    //Null si no esta el archivo o si MinIO no responde: la inmobiliaria queda sin logo
    public String storeLogo(String fileName) {
        if (!available) {
            return null;
        }
        try {
            Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:seed-logos/" + fileName);
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            return photoStorage.store(resource.getContentAsByteArray(), extension, contentTypeOf(extension));
        } catch (IOException e) {
            log.warn("DataSeeder: no se pudo leer el logo {} ({})", fileName, e.getMessage());
            return null;
        } catch (PhotoStorageException e) {
            log.warn("DataSeeder: MinIO no responde, el resto de los datos se carga sin fotos ({})", e.getMessage());
            available = false;
            return null;
        }
    }

    //La primera es la portada: fachada para casas y terrenos, ambiente para departamentos,
    //oficina para lo comercial. El resto sale de los ambientes del mismo tipo de propiedad
    private List<SamplePhoto> choose(PropertyType type, int count, Random random) {
        List<SamplePhoto> cover;
        List<SamplePhoto> rest;
        switch (type) {
            case OFFICE, COMMERCIAL_SPACE, WAREHOUSE -> {
                cover = photos("office");
                rest = photos("office");
            }
            case LAND, GARAGE -> {
                cover = photos("exterior");
                rest = photos("exterior");
                count = Math.min(count, 2);
            }
            case APARTMENT -> {
                cover = photos("interior");
                rest = photos("interior");
            }
            default -> {
                cover = photos("exterior");
                rest = photos("interior");
            }
        }

        List<SamplePhoto> chosen = new ArrayList<>();
        if (!cover.isEmpty()) {
            chosen.add(cover.get(random.nextInt(cover.size())));
        }
        List<SamplePhoto> shuffled = new ArrayList<>(rest);
        Collections.shuffle(shuffled, random);
        for (SamplePhoto sample : shuffled) {
            if (chosen.size() >= count) {
                break;
            }
            if (!chosen.contains(sample)) {
                chosen.add(sample);
            }
        }
        return chosen;
    }

    //Si falta una categoria usa todas las fotos, asi una carpeta vacia no deja propiedades sin fotos
    private List<SamplePhoto> photos(String category) {
        Map<String, List<SamplePhoto>> loaded = load();
        List<SamplePhoto> photos = loaded.getOrDefault(category, List.of());
        if (!photos.isEmpty()) {
            return photos;
        }
        return loaded.values().stream().flatMap(List::stream).toList();
    }

    //Se leen una sola vez y quedan en memoria: son pocos MB
    private Map<String, List<SamplePhoto>> load() {
        if (byCategory != null) {
            return byCategory;
        }
        List<SamplePhoto> all = new ArrayList<>();
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(LOCATION);
            for (Resource resource : resources) {
                String url = resource.getURL().toString();
                String[] parts = url.split("/");
                String name = parts[parts.length - 1];
                String category = parts[parts.length - 2];
                String extension = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
                all.add(new SamplePhoto(category, name, extension, resource.getContentAsByteArray()));
            }
        } catch (IOException e) {
            log.warn("DataSeeder: no se pudieron leer las fotos de muestra ({})", e.getMessage());
        }
        if (all.isEmpty()) {
            log.warn("DataSeeder: no hay fotos en resources/seed-photos, las propiedades quedan sin fotos");
            available = false;
        }
        //El orden de getResources depende del sistema de archivos: se ordena para que la
        //misma semilla elija las mismas fotos en todas las compus
        byCategory = all.stream()
                .sorted((a, b) -> a.name().compareTo(b.name()))
                .collect(Collectors.groupingBy(SamplePhoto::category));
        return byCategory;
    }

    private String contentTypeOf(String extension) {
        return switch (extension) {
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> "image/jpeg";
        };
    }
}
