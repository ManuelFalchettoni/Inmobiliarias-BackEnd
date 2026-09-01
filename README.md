# zaguan-inmobiliarias

Backend de la app de inmobiliarias. Spring Boot 4 + MySQL, Java 21.

Para correrlo hace falta MySQL levantado. Los datos de conexión salen de variables de
entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`), con defaults para local.
La base se crea sola la primera vez y el esquema lo arma Hibernate con `ddl-auto=update`.

```
./mvnw spring-boot:run
```

## Cómo está organizado

Un paquete por entidad (`property`, `property.photo`, `agency`, `user`) y dentro de cada capa
—controller, service, mapper, repository, dto, entity— lo mismo. Los controllers están
separados por operación: uno para crear, otro para buscar, otro para borrar. Cada uno con su
service.

Las entidades nunca salen ni entran por la API: todo pasa por DTOs (`XRequest` para lo que
entra, `XResponse` para lo que sale) y por un mapper que traduce entre los dos.

Los errores los junta un `GlobalExceptionHandler` con `@RestControllerAdvice`, así que todas
las respuestas de error tienen el mismo formato: timestamp, status, error, mensaje y la ruta
que se llamó. Ahí caen los 404 de cada entidad, los errores de validación de `@Valid`
(con el detalle campo por campo) y los problemas de subida de fotos.

La API está abierta por ahora: `SecurityConfig` deja pasar todo, sin sesión.

---

## Propiedades

`/api/properties`

| | |
|---|---|
| `POST /api/properties` | crear |
| `GET /api/properties` | listar, paginado |
| `GET /api/properties/{id}` | traer una |
| `PUT /api/properties/{id}` | editar |
| `DELETE /api/properties/{id}` | dar de baja |
| `PATCH /api/properties/{id}/restore` | restaurar |

El borrado es lógico: `DELETE` no saca la fila, le pone `active` en false. Por eso está el
`restore`, que la vuelve a activar.

El listado acepta `idAgency` para filtrar por inmobiliaria y `active` para elegir qué ver.
`active` viene en true por defecto, así que quien no lo manda sigue viendo solo las vigentes;
con `active=false` salen las dadas de baja, que es la única forma de encontrarlas para
restaurarlas. Viene paginado (20 por página, ordenado por fecha de creación descendente) y se
puede cambiar con `page`, `size` y `sort`.

Al crear se valida que el `idAgency` exista de verdad; si no, devuelve 404 de inmobiliaria.

Campos: dirección, ubicación, tipo, año de construcción, ambientes, superficie, piso, estado
de la propiedad y ocupación. Los tres enums (`PropertyType`, `PropertyCondition`,
`PropertyOccupancy`) se guardan como texto en columnas varchar, no como el ENUM nativo de
MySQL, para poder agregar constantes nuevas sin romper nada.

Los `@Size` del request coinciden con el largo de las columnas, así la validación corta antes
de que MySQL rechace el insert.

## Fotos de propiedades

`/api/properties/{propertyId}/photos`

| | |
|---|---|
| `POST` | subir una o varias (multipart) |
| `GET` | listar las de una propiedad |
| `GET /{photoId}` | traer una |
| `DELETE /{photoId}` | borrar |

Los archivos se guardan en disco, en `uploads/photos`, y quedan servidos como estáticos bajo
`/photos/**`. En la base solo queda la URL. El nombre del archivo se reemplaza por un UUID,
sin ninguna relación con el original, y se aceptan jpg, jpeg, png y webp. Máximo 5MB por
archivo, 30MB por request y 20 fotos por propiedad.

Cada foto tiene una posición. La próxima se calcula a partir de la posición más alta que ya
existe, no contando cuántas hay, para no repetir una que quedó libre por un borrado.

La subida y el borrado cuidan que la base y el disco no queden desfasados: si algo falla en
medio de una subida, se borran los archivos que ya se habían escrito; y al borrar, primero se
saca la fila y recién después el archivo, para que un error haga rollback y la foto vuelva.

Una foto solo se puede borrar desde la URL de su propia propiedad: se busca por id y por
propiedad a la vez.

## Inmobiliarias

`/api/agencies`

| | |
|---|---|
| `POST /api/agencies` | crear |
| `GET /api/agencies` | listar, paginado |
| `GET /api/agencies/{id}` | traer una |
| `PUT /api/agencies/{id}` | editar |
| `DELETE /api/agencies/{id}` | borrar |

Campos: CUIT, razón social, nombre público, email, teléfono, dirección, web y redes. CUIT,
razón social, email, teléfono, dirección, web y redes son únicos. Tiene un estado
(`PENDING`, `VERIFY`, `DENIED`) para el circuito de verificación.

Guarda fecha de creación y de última modificación solas.

## Usuarios

`/api/users`

| | |
|---|---|
| `POST /api/users` | crear |
| `GET /api/users` | listar, paginado |
| `GET /api/users/{id}` | traer uno |
| `PUT /api/users/{id}` | editar |
| `DELETE /api/users/{id}` | borrar |

Campos: nombre, email, contraseña, teléfono y rol (`USER`, `AGENT`, `Agency`, `ADMIN`).
Email y teléfono son únicos.

El listado va paginado, con el tamaño de página tapado en 50 para que nadie pida la tabla
entera de una.
