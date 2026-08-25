# zaguan-inmobiliarias

Backend de la app de inmobiliarias. Spring Boot 4 + MySQL.

Para correrlo hace falta MySQL levantado. Los datos de conexión salen de variables de
entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`), con defaults para local.

```
./mvnw spring-boot:run
```

Por ahora está el CRUD de propiedades en `/api/properties`.

## Pendientes

- **Seguridad**: hoy la API está abierta (`SecurityConfig` con `permitAll`). Falta autenticación
  y que cada inmobiliaria solo pueda tocar sus propias propiedades.
- **Manejo de errores**: falta un `@RestControllerAdvice`. Los 400 de validación y los 404
  responden con formatos distintos.
- **Fotos**: la relación con `PropertyPhoto` está mapeada pero no hay endpoint para subirlas.
- **Enums en MySQL**: Hibernate los crea como columna `ENUM`, y con `ddl-auto=update` no la
  modifica. Agregar una constante nueva rompe los inserts. Pasar a varchar o migrar con Flyway.
- **Índices** para los campos que se filtran (`idAgency`, `type`).
- **Migraciones**: `ddl-auto=update` sirve para desarrollo, antes de producción va Flyway.
- **Entidad Publicación**: operación (venta/alquiler), precio y estado. Va aparte de `Property`.
  De paso, `idAgency` hoy es un `Long` suelto sin FK.
