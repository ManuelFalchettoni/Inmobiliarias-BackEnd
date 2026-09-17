# zaguan-inmobiliarias

Backend de la app de inmobiliarias. Spring Boot 4 + MySQL, Java 21.

Para correrlo hace falta MySQL levantado. Los datos de conexión salen de variables de
entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`), con defaults para local.
La base se crea sola la primera vez y el esquema lo arma Hibernate con `ddl-auto=update`.

Las fotos se guardan en MinIO, que se levanta con Docker (API en el puerto 9000, consola web
en http://localhost:9001 con `admin` / `admin12345`). Tiene que estar corriendo antes de
arrancar el backend. La conexión sale de `MINIO_URL`, `MINIO_USER`, `MINIO_PASSWORD` y
`MINIO_BUCKET`, también con defaults para local.

```
docker compose up -d
./mvnw spring-boot:run
```

El backend queda en `http://localhost:8080`.

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

# Reglas que valen para toda la API

Antes de las secciones de cada entidad, cuatro cosas que aplican a todos los endpoints.

## CORS

El front corre en otro puerto, así que sin CORS el navegador corta las llamadas aunque la API
esté abierta. Está configurado en `SecurityConfig` y aplica a `/api/**`.

Orígenes habilitados por defecto: `http://localhost:5173` (Vite) y `http://localhost:3000`
(Next, CRA). Si el front levanta en otro puerto, se cambian sin tocar código con la variable
de entorno `CORS_ORIGINS`, separando varios por coma:

```
CORS_ORIGINS=http://localhost:4200,https://app.zaguan.com
```

Métodos permitidos: `GET`, `POST`, `PUT`, `PATCH`, `DELETE` y `OPTIONS`. Headers: todos.

`allowCredentials` está en false: la API no usa cookies ni sesión, así que el front no tiene
que mandar `credentials: "include"` en los `fetch`. Si lo manda, el request falla.

Las fotos son aparte: salen de MinIO (`http://localhost:9000`), no del backend. Como se cargan
con el `src` de un `<img>`, no pasan por CORS. Si en algún momento se bajan con `fetch` (para
un canvas, por ejemplo), hay que configurar CORS en MinIO también.

## Cuerpo de los errores

Todo error que pasa por el `GlobalExceptionHandler` sale así:

```json
{
  "timestamp": "2025-09-15T18:22:41.1234",
  "status": 404,
  "error": "Not Found",
  "message": "Property not found with id: 7",
  "path": "/api/properties/7"
}
```

Los errores de validación de `@Valid` son 400 y juntan todos los campos que fallaron en el
mismo `message`, separados por coma:

```json
{
  "timestamp": "2025-09-15T18:22:41.1234",
  "status": 400,
  "error": "Bad Request",
  "message": "address: must not be blank, size: must be greater than 0",
  "path": "/api/properties"
}
```

Hay tres casos que **no** pasan por el handler y salen con el formato default de Spring
(mismos campos, pero sin garantía de que `message` diga algo útil): JSON mal formado, un valor
de enum que no existe (`"type": "CASA"`) y un campo que el request no conoce. Los tres dan 400.

## Cuerpo de los listados

Todos los `GET` de listado devuelven un `Page` de Spring, no un array pelado:

```json
{
  "content": [ { "id": 1 } ],
  "pageable": { "pageNumber": 0, "pageSize": 20, "offset": 0, "paged": true, "unpaged": false },
  "totalElements": 1,
  "totalPages": 1,
  "size": 20,
  "number": 0,
  "numberOfElements": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

Los parámetros de paginado no son iguales en todos los recursos:

| Recurso | `page` | `size` (default) | Tope | `sort` |
|---|---|---|---|---|
| `/api/properties` | sí | 20 | 100 | sí |
| `/api/agencies` | sí | 5 | sin tope | no |
| `/api/users` | sí | 5 | 50 | no |

La única excepción es el listado de fotos, que devuelve un array común sin paginar.

## Dos reglas de serialización que rompen requests

- **Los campos en `null` no se serializan.** `spring.jackson.default-property-inclusion=non_null`:
  si una propiedad no tiene `year`, la clave `year` directamente no viene en el JSON. No hay
  que esperar `null`, hay que esperar que la clave falte.
- **Un campo de más es un 400.** `spring.jackson.deserialization.fail-on-unknown-properties=true`:
  mandar en el request un campo que el `XRequest` no declara devuelve 400. Esto importa porque
  los `XResponse` traen campos que los `XRequest` no tienen (`id`, `createdAt`, `updatedAt`,
  `active`, `photos`): un response no se puede reenviar tal cual como request, hay que armar
  el body con los campos del request y nada más.

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

### Request

Mismo body para `POST` y `PUT`. `DELETE` y `PATCH /restore` no llevan body.

```json
{
  "address": "Av. Siempre Viva 742",
  "type": "HOUSE",
  "location": "Rosario",
  "idAgency": 1,
  "year": 1998,
  "rooms": 4,
  "size": 120,
  "condition": "GOOD",
  "occupancy": "VACANT",
  "floorNumber": 0
}
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `address` | string | sí | no vacío, máx 150 |
| `type` | enum `PropertyType` | sí | |
| `location` | string | sí | no vacío, máx 100 |
| `idAgency` | number | sí | la inmobiliaria tiene que existir |
| `year` | number | no | entre 1800 y 2100 |
| `rooms` | number | sí | entero >= 0 |
| `size` | number | sí | entero > 0, así que 0 no sirve |
| `condition` | enum `PropertyCondition` | sí | |
| `occupancy` | enum `PropertyOccupancy` | sí | |
| `floorNumber` | number | sí | entero >= 0 |

`rooms`, `size` y `floorNumber` son `int` primitivos: si no se mandan valen 0, y ese 0 hace
fallar la validación de `size`. Conviene mandarlos siempre.

En el `PUT`, `idAgency` tiene que ser el mismo que la propiedad ya tiene. Una propiedad no
cambia de inmobiliaria desde el update: si viene otro, es 400 y no se guarda nada.

Enums:

- `PropertyType`: `HOUSE`, `APARTMENT`, `PH`, `LAND`, `OFFICE`, `COMMERCIAL_SPACE`, `WAREHOUSE`, `GARAGE`, `FARM`
- `PropertyCondition`: `BRAND_NEW`, `GOOD`, `NEEDS_REPAIR`, `UNDER_CONSTRUCTION`
- `PropertyOccupancy`: `VACANT`, `OWNER_OCCUPIED`, `TENANT_OCCUPIED`

### Response

`POST`, `GET /{id}`, `PUT` y `PATCH /restore` devuelven una propiedad. El `GET` de listado
devuelve un `Page` con estas propiedades adentro de `content`.

```json
{
  "id": 7,
  "address": "Av. Siempre Viva 742",
  "active": true,
  "type": "HOUSE",
  "location": "Rosario",
  "idAgency": 1,
  "year": 1998,
  "createdAt": "2025-09-15T18:22:41.1234",
  "updatedAt": "2025-09-15T18:22:41.1234",
  "rooms": 4,
  "size": 120,
  "condition": "GOOD",
  "occupancy": "VACANT",
  "floorNumber": 0,
  "photos": [
    {
      "id": 3,
      "url": "http://localhost:9000/photos/2f1c8e0a-....jpg",
      "photoName": "frente.jpg",
      "position": 0
    }
  ]
}
```

Las fotos vienen embebidas en la propiedad, en el orden en que las trae la base. Para tenerlas
ordenadas por `position` está el endpoint de fotos. Si la propiedad no tiene fotos, `photos`
viene como `[]`. Si no tiene `year`, la clave `year` no aparece.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 la inmobiliaria del `idAgency` no existe |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 no existe o está dada de baja |
| `PUT /{id}` | 200 | 400 validación · 400 `idAgency` distinto al actual · 404 |
| `DELETE /{id}` | 204 sin body | 404 no existe o ya estaba dada de baja |
| `PATCH /{id}/restore` | 200 | 404 no existe |

`GET /{id}`, `PUT` y `DELETE` solo ven propiedades activas. `restore` busca sin ese filtro,
que es como se llega a una dada de baja; si ya estaba activa no hace nada y devuelve 200
igual.

## Fotos de propiedades

`/api/properties/{propertyId}/photos`

| | |
|---|---|
| `POST` | subir una o varias (multipart) |
| `GET` | listar las de una propiedad |
| `GET /{photoId}` | traer una |
| `DELETE /{photoId}` | borrar |

Los archivos se suben al bucket `photos` de MinIO, que el backend crea solo al arrancar si no
existe y deja con lectura pública, así la URL (`http://localhost:9000/photos/<uuid>.jpg`) se
abre directo desde el navegador. Subir y borrar solo lo hace el backend. En la base no se
guarda la URL sino el `objectKey` (`<uuid>.jpg`); la URL se arma al responder con
`minio.url`, así no queda atada a `localhost` cuando cambie el servidor. El nombre original
se guarda aparte, cortado en 255 caracteres. Si MinIO falla, la respuesta es un 500 con el
formato de error de siempre. El nombre del archivo se reemplaza por un UUID,
sin ninguna relación con el original, y se aceptan jpg, jpeg, png y webp. Máximo 5MB por
archivo, 30MB por request y 20 fotos por propiedad.

Cada foto tiene una posición. La próxima se calcula a partir de la posición más alta que ya
existe, no contando cuántas hay, para no repetir una que quedó libre por un borrado.

La subida y el borrado cuidan que la base y MinIO no queden desfasados: si algo falla en
medio de una subida, se borran los archivos que ya se habían escrito; y al borrar, primero se
saca la fila y recién después el archivo, para que un error haga rollback y la foto vuelva.

Una foto solo se puede borrar desde la URL de su propia propiedad: se busca por id y por
propiedad a la vez.

### Request

El `POST` es `multipart/form-data`, no JSON. El campo se llama **`files`** y admite varios
archivos en el mismo request:

```js
const form = new FormData();
form.append("files", file1);
form.append("files", file2);
await fetch(`/api/properties/${propertyId}/photos`, { method: "POST", body: form });
```

No hay que setear `Content-Type` a mano: el browser le pone el boundary solo.

Reglas del archivo: extensión `jpg`, `jpeg`, `png` o `webp` (se mira la extensión del nombre,
no el mime type), no vacío, máx 5MB cada uno, máx 30MB el request entero y máx 20 fotos por
propiedad contando las que ya están.

`GET` y `DELETE` no llevan body.

### Response

`POST` devuelve un **array** con las fotos creadas, en el orden en que se mandaron. El `GET`
de listado devuelve un array ordenado por `position` ascendente. `GET /{photoId}` devuelve una
sola. Ninguno de los tres viene paginado.

```json
[
  {
    "id": 3,
    "url": "http://localhost:9000/photos/2f1c8e0a-....jpg",
    "photoName": "frente.jpg",
    "position": 0
  }
]
```

`url` es la URL pública y ya armada: va directo al `src` de un `<img>`, sin token ni headers.
`photoName` es el nombre original que subió el cliente, solo para mostrar; el archivo en MinIO
se llama distinto.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 sin archivos, archivo vacío o formato no permitido · 404 la propiedad no existe o está dada de baja · 409 se pasa de 20 fotos · 413 archivo > 5MB o request > 30MB · 500 MinIO caído |
| `GET` listado | 200 | 404 la propiedad no existe o está dada de baja |
| `GET /{photoId}` | 200 | 404 la propiedad o la foto no existen · 404 la foto es de otra propiedad |
| `DELETE /{photoId}` | 204 sin body | 404 igual que arriba · 500 MinIO caído |

El 413 lo tira el servidor antes de llegar al controller, así que en una subida de varios
archivos no dice cuál fue el que se pasó.

## Inmobiliarias

`/api/agencies`

| | |
|---|---|
| `POST /api/agencies` | crear |
| `GET /api/agencies` | listar, paginado |
| `GET /api/agencies/{id}` | traer una |
| `PUT /api/agencies/{id}` | editar |
| `DELETE /api/agencies/{id}` | borrar |

Campos: CUIT, razón social, nombre público, email, teléfono, dirección, web y redes. Web y
redes son opcionales y pueden repetirse; CUIT, razón social, email, teléfono y dirección son
únicos. Tiene un estado
(`PENDING`, `VERIFY`, `DENIED`) para el circuito de verificación.

Guarda fecha de creación y de última modificación solas.

Antes de crear o editar se controla que CUIT, razón social, email y teléfono no estén usados
por otra inmobiliaria; si lo están devuelve 409 con el campo repetido. Si se repite la dirección
también devuelve 409, con un mensaje general.

Las validaciones están solo en el request; la entidad tiene el largo de cada columna, que
coincide con el `@Size` del request. Lo mismo en usuarios.

La contraseña se guarda hasheada con BCrypt (`PasswordEncoder` en `SecurityConfig`), nunca
como llega. Lo mismo en usuarios.

### Request

Mismo body para `POST` y `PUT`. El `PUT` es un reemplazo completo: pisa todos los campos, así
que hay que mandarlos todos, incluida la contraseña, que se vuelve a hashear en cada update.
`DELETE` no lleva body.

```json
{
  "cuit": "30712345678",
  "companyName": "Inmobiliaria Zaguán SRL",
  "publicName": "Zaguán",
  "email": "contacto@zaguan.com",
  "password": "unaClave123",
  "phoneNumber": "3415551234",
  "address": "Córdoba 1234",
  "webURL": "https://zaguan.com",
  "socials": "@zaguan",
  "status": "PENDING"
}
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `cuit` | string | sí | 11 a 13 caracteres, único |
| `companyName` | string | sí | 3 a 30, único |
| `publicName` | string | sí | 3 a 30 |
| `email` | string | sí | formato email, 3 a 100, único |
| `password` | string | sí | 8 a 20 |
| `phoneNumber` | string | sí | 8 a 15, único |
| `address` | string | sí | 6 a 40, único |
| `webURL` | string | no | máx 255 |
| `socials` | string | no | máx 255 |
| `status` | enum `AgencyStatus` | sí | `PENDING`, `VERIFY`, `DENIED` |

`status` es obligatorio también al crear: el alta manda `PENDING`.

### Response

`POST`, `GET /{id}` y `PUT` devuelven una inmobiliaria. El `GET` de listado devuelve un `Page`
con estas adentro de `content`.

```json
{
  "id": 1,
  "cuit": "30712345678",
  "companyName": "Inmobiliaria Zaguán SRL",
  "publicName": "Zaguán",
  "email": "contacto@zaguan.com",
  "phoneNumber": "3415551234",
  "address": "Córdoba 1234",
  "webURL": "https://zaguan.com",
  "socials": "@zaguan",
  "status": "PENDING",
  "createdAt": "2025-09-15T18:22:41.1234",
  "updatedAt": "2025-09-15T18:22:41.1234"
}
```

La contraseña nunca sale en la respuesta. `webURL` y `socials` no aparecen si están vacíos.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 409 CUIT, razón social, email o teléfono repetidos · 409 dirección repetida |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 · 409 igual que el POST, mirando las demás inmobiliarias |
| `DELETE /{id}` | 204 sin body | 404 |

El 409 de CUIT, razón social, email y teléfono dice en `message` cuál es el repetido
(`"Email already registered: contacto@zaguan.com"`), así se puede marcar el campo en el
formulario. El de dirección lo tira la base y sale genérico:
`"Some of the values are already registered"`.

El borrado es físico: la fila se va y no hay restore.

## Usuarios

`/api/users`

| | |
|---|---|
| `POST /api/users` | crear |
| `GET /api/users` | listar, paginado |
| `GET /api/users/{id}` | traer uno |
| `PUT /api/users/{id}` | editar |
| `DELETE /api/users/{id}` | borrar |

Campos: nombre, email, contraseña, teléfono y rol (`USER`, `AGENT`, `AGENCY`, `ADMIN`).
Las fechas de creación y modificación se ponen solas.
Email y teléfono son únicos: si
ya los tiene otro usuario, crear o editar devuelve 409. El email acepta hasta 100 caracteres y
la contraseña se guarda hasheada con BCrypt.

El listado va paginado, con el tamaño de página tapado en 50 para que nadie pida la tabla
entera de una.

### Request

Mismo body para `POST` y `PUT`. El `PUT` pisa nombre, email, contraseña y teléfono, así que
hay que mandarlos todos. `DELETE` no lleva body.

```json
{
  "name": "Manuel",
  "email": "manuel@mail.com",
  "password": "unaClave123",
  "phoneNumber": "3415551234",
  "rol": "USER"
}
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `name` | string | sí | 3 a 20 |
| `email` | string | sí | formato email, máx 100, único |
| `password` | string | sí | 8 a 20 |
| `phoneNumber` | string | sí | 8 a 15, único |
| `rol` | enum `UserRol` | sí | `USER`, `AGENT`, `AGENCY`, `ADMIN` |

El campo es `rol`, no `role`. El `PUT` lo exige por la validación pero no lo guarda: el rol que
se manda en un update se descarta.

### Response

`POST`, `GET /{id}` y `PUT` devuelven un usuario. El `GET` de listado devuelve un `Page` con
estos adentro de `content`.

```json
{
  "id": 1,
  "name": "Manuel",
  "email": "manuel@mail.com",
  "active": true,
  "phoneNumber": "3415551234",
  "rol": "USER",
  "createdAt": "2025-09-15T18:22:41.1234",
  "updatedAt": "2025-09-15T18:22:41.1234"
}
```

La contraseña nunca sale en la respuesta. `active` se pone en true al crear y hoy no hay
endpoint que lo cambie, así que siempre viene en true.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 409 email o teléfono repetidos |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 · 409 email o teléfono de otro usuario |
| `DELETE /{id}` | 204 sin body | 404 |

El 409 dice en `message` cuál es el campo repetido
(`"Phone number already registered: 3415551234"`).

El borrado es físico: la fila se va, el `active` no se toca y no hay restore.
