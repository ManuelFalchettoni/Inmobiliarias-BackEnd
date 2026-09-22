# zaguan-inmobiliarias

Backend de la app de inmobiliarias. Java 21, Spring Boot 4, MySQL, MinIO.

Hace falta MySQL corriendo en el 3306: no está en el `docker-compose.yml`, que levanta solo
MinIO. La base no hay que crearla, el servidor sí tiene que estar levantado.

```
docker compose up -d
./mvnw spring-boot:run
```

Queda en `http://localhost:8080`.

Variables de entorno, todas con default para local: `DB_HOST`, `DB_PORT`, `DB_NAME`,
`DB_USER`, `DB_PASSWORD`, `MINIO_URL`, `MINIO_USER`, `MINIO_PASSWORD`, `MINIO_BUCKET`,
`CORS_ORIGINS`.

- MySQL: en el 3306, con `root` / `root`. La base la crea el driver con
  `createDatabaseIfNotExist` y el esquema Hibernate con `ddl-auto=update`.
- MinIO: API en el puerto 9000, consola en `http://localhost:9001` (`admin` / `admin12345`).
- MinIO no hace falta para arrancar: el bucket se prepara en la primera subida. Sin MinIO
  fallan solo el `POST` y el `DELETE` de fotos, con 500; los `GET` andan igual, porque la URL
  se arma con `minio.url` sin consultar nada.

## Estructura

Un paquete por capa (`controller`, `service`, `mapper`, `repository`, `dto`, `entity`) y
adentro uno por entidad: `controller/agency`, `service/agency`, `dto/request/agency`. Un
controller y un service por operación.

Las entidades no entran ni salen por la API: entra un `XRequest`, sale un `XResponse`, traduce
el mapper. Los errores los unifica `GlobalExceptionHandler` con `@RestControllerAdvice`.
`SecurityConfig` deja la API abierta, sin sesión, y define el `PasswordEncoder` (BCrypt).
Las excepciones siguen el mismo corte por entidad; la única compartida por dos entidades,
`InvalidCurrentPasswordException`, vive en la raíz de `exception`.

---

# Reglas de toda la API

## CORS

Configurado en `SecurityConfig`, aplica a `/api/**`.

| | |
|---|---|
| Orígenes | `http://localhost:5173` y `http://localhost:3000`; se cambian con `CORS_ORIGINS`, separados por coma |
| Métodos | `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS` |
| Headers | todos |
| `allowCredentials` | false: el front no manda `credentials: "include"` |

Las fotos salen de MinIO, no del backend. Con el `src` de un `<img>` no pasan por CORS; con
`fetch` hay que configurar CORS en MinIO.

## Errores

Los errores que pasan por `GlobalExceptionHandler` tienen todos el mismo formato:

```json
{
  "timestamp": "2025-09-15T18:22:41.1234",
  "status": 404,
  "error": "Not Found",
  "message": "Property not found with id: 7",
  "path": "/api/properties/7"
}
```

| Caso | Status | `message` |
|---|---|---|
| Validación de `@Valid` | 400 | los campos que fallaron, separados por coma: `"address: must not be blank, size: must be greater than 0"` |
| JSON roto, enum inexistente o campo desconocido | 400 | `"Malformed or invalid request body"` |
| Path variable del tipo equivocado | 400 | `"Invalid value for id"` |
| Único repetido que no se controla antes | 409 | `"Some of the values are already registered"` |

Lo que no llega al handler sale con el formato default de Spring: el mismo JSON pero **sin
`message`**. Son tres casos: una ruta que no existe, un `multipart` con el nombre de parte
equivocado y cualquier excepción no prevista.

## Listados

Devuelven un `Page` de Spring, no un array:

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

Los tres aceptan `page`, `size` y `sort`, con tope de 100 por página, y `active` para elegir
entre lo vigente y lo dado de baja (true por defecto).

| Recurso | `size` default | Orden default | Filtros extra |
|---|---|---|---|
| `/api/properties` | 20 | `createdAt` desc | `idAgency` |
| `/api/agencies` | 20 | `createdAt` desc | — |
| `/api/users` | 20 | `createdAt` desc | — |

El listado de fotos es la excepción: array común, sin paginar.

## Serialización

- **Los campos en `null` no se serializan.** Si una propiedad no tiene `year`, la clave `year`
  no viene en el JSON.
- **Un campo de más es un 400.** Los `XResponse` traen campos que los `XRequest` no tienen
  (`id`, `createdAt`, `updatedAt`, `active`, `photos`), así que un response no se puede
  reenviar tal cual como request.

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

Baja lógica: `DELETE` pone `active` en false, la fila queda. `GET /{id}`, `PUT` y `DELETE`
solo ven activas; `restore` busca sin ese filtro.

Al crear, la inmobiliaria del `idAgency` tiene que existir y estar activa.

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
| `idAgency` | number | sí | existente y activa; en el `PUT`, el mismo que ya tiene |
| `year` | number | no | entre 1800 y 2100 |
| `rooms` | number | sí | entero >= 0 |
| `size` | number | sí | entero > 0 |
| `condition` | enum `PropertyCondition` | sí | |
| `occupancy` | enum `PropertyOccupancy` | sí | |
| `floorNumber` | number | sí | entero >= 0 |

`rooms`, `size` y `floorNumber` son `int` primitivos: si no se mandan valen 0, y ese 0 hace
fallar la validación de `size`.

Una propiedad no cambia de inmobiliaria desde el `PUT`: si `idAgency` viene distinto, 400.

Enums:

- `PropertyType`: `HOUSE`, `APARTMENT`, `PH`, `LAND`, `OFFICE`, `COMMERCIAL_SPACE`, `WAREHOUSE`, `GARAGE`, `FARM`
- `PropertyCondition`: `BRAND_NEW`, `GOOD`, `NEEDS_REPAIR`, `UNDER_CONSTRUCTION`
- `PropertyOccupancy`: `VACANT`, `OWNER_OCCUPIED`, `TENANT_OCCUPIED`

### Response

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

Las fotos vienen embebidas sin orden garantizado; para tenerlas por `position` está el
endpoint de fotos. Sin fotos, `photos` viene `[]`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 inmobiliaria inexistente o dada de baja |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 400 `idAgency` distinto al actual · 404 |
| `DELETE /{id}` | 204 sin body | 404 |
| `PATCH /{id}/restore` | 200 | 404 |

## Fotos de propiedades

`/api/properties/{propertyId}/photos`

| | |
|---|---|
| `POST` | subir una o varias |
| `GET` | listar las de una propiedad |
| `GET /{photoId}` | traer una |
| `DELETE /{photoId}` | borrar |

Los archivos van al bucket `photos` de MinIO, con lectura pública. En la base se guarda el
`objectKey` (`<uuid>.jpg`), no la URL: la URL se arma al responder con `minio.url`. El nombre
original se guarda aparte, cortado en 255 caracteres.

Cada foto tiene una `position`. La próxima sale de la más alta que ya existe, así un borrado
no libera una posición que después se repita.

Una foto solo se puede ver o borrar desde la URL de su propia propiedad.

### Request

`multipart/form-data`, no JSON. El campo se llama **`files`** y admite varios archivos:

```js
const form = new FormData();
form.append("files", file1);
form.append("files", file2);
await fetch(`/api/properties/${propertyId}/photos`, { method: "POST", body: form });
```

No hay que setear `Content-Type` a mano.

| | |
|---|---|
| Formatos | `jpg`, `jpeg`, `png`, `webp`: tienen que coincidir la extensión del nombre y el `Content-Type` de la parte, que debe empezar con `image/` |
| Tamaño | máx 5MB por archivo, 30MB por request |
| Cantidad | máx 20 por propiedad, contando las que ya están |

Los dos límites de tamaño se combinan: las 20 fotos no entran en un request de 30MB. Siete
archivos de 5MB dan 413 aunque ninguno pase el máximo por archivo, así que conviene subir de
a tandas de seis.

`GET` y `DELETE` no llevan body.

### Response

`POST` devuelve un array con las fotos creadas, en el orden en que se mandaron. `GET` devuelve
un array ordenado por `position` ascendente. Ninguno viene paginado.

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

`url` va directo al `src` de un `<img>`, sin token ni headers. `photoName` es el nombre
original, solo para mostrar.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 sin archivos, archivo vacío, extensión no permitida o `Content-Type` que no empieza con `image/` · 404 propiedad inexistente o dada de baja · 409 se pasa de 20 · 413 archivo > 5MB o request > 30MB · 500 MinIO caído |
| `GET` listado | 200 | 404 propiedad inexistente o dada de baja |
| `GET /{photoId}` | 200 | 404 propiedad o foto inexistente · 404 la foto es de otra propiedad |
| `DELETE /{photoId}` | 204 sin body | 404 igual que arriba · 500 MinIO caído |

El 413 lo tira el servidor antes del controller: en una subida de varios no dice cuál se pasó.

## Inmobiliarias

`/api/agencies`

| | |
|---|---|
| `POST /api/agencies` | crear |
| `GET /api/agencies` | listar, paginado |
| `GET /api/agencies/{id}` | traer una |
| `PUT /api/agencies/{id}` | editar |
| `PATCH /api/agencies/{id}/password` | cambiar la contraseña |
| `DELETE /api/agencies/{id}` | dar de baja |
| `PATCH /api/agencies/{id}/restore` | restaurar |

Baja lógica, como propiedades. `GET /{id}`, `PUT`, `PATCH /password` y `DELETE` solo ven
activas; `restore` busca sin ese filtro.

`active` y `status` son cosas distintas: `status` es el circuito de verificación y no se toca
al dar de baja.

Dar de baja una inmobiliaria no da de baja sus propiedades, pero no se le pueden cargar nuevas.

Los únicos siguen ocupados por las dadas de baja: no se puede crear otra con el mismo CUIT,
hay que restaurar la que está.

### Request

`POST` y `PUT` no llevan el mismo body: el `PUT` no acepta `password` y mandarla da 400. El
resto de los campos el `PUT` los pisa todos. `DELETE` y `PATCH /restore` no llevan body.

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

| Campo | Tipo | Obligatorio | En el `PUT` | Reglas |
|---|---|---|---|---|
| `cuit` | string | sí | sí | 11 a 13, único |
| `companyName` | string | sí | sí | 3 a 30, único |
| `publicName` | string | sí | sí | 3 a 30 |
| `email` | string | sí | sí | formato email, 3 a 100, único |
| `password` | string | sí | **no** | 8 a 20 |
| `phoneNumber` | string | sí | sí | 8 a 15, único |
| `address` | string | sí | sí | 6 a 40, único |
| `webURL` | string | no | sí | máx 255 |
| `socials` | string | no | sí | máx 255 |
| `status` | enum `AgencyStatus` | sí | sí | `PENDING`, `VERIFY`, `DENIED` |

`PATCH /{id}/password` lleva la contraseña actual y la nueva. Las dos son obligatorias: sin
la actual no se cambia nada.

```json
{ "currentPassword": "unaClave123", "password": "otraClave123" }
```

Si `currentPassword` no coincide con la guardada, da 400 con
`"Current password does not match"`.

### Response

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
  "active": true,
  "status": "PENDING",
  "createdAt": "2025-09-15T18:22:41.1234",
  "updatedAt": "2025-09-15T18:22:41.1234"
}
```

La contraseña nunca sale. `PATCH /password` no devuelve body.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 409 CUIT, razón social, email o teléfono repetidos · 409 dirección repetida |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 400 si mandás `password` · 404 · 409 igual que el `POST` |
| `PATCH /{id}/password` | 204 sin body | 400 validación · 400 `currentPassword` incorrecta · 404 |
| `DELETE /{id}` | 204 sin body | 404 |
| `PATCH /{id}/restore` | 200 | 404 |

El 409 dice cuál es el campo repetido, dirección incluida
(`"Email already registered: contacto@zaguan.com"`).

## Usuarios

`/api/users`

| | |
|---|---|
| `POST /api/users` | crear |
| `GET /api/users` | listar, paginado |
| `GET /api/users/{id}` | traer uno |
| `PUT /api/users/{id}` | editar |
| `PATCH /api/users/{id}/password` | cambiar la contraseña |
| `DELETE /api/users/{id}` | dar de baja |
| `PATCH /api/users/{id}/restore` | restaurar |

Baja lógica, como propiedades e inmobiliarias. `GET /{id}`, `PUT`, `PATCH /password` y
`DELETE` solo ven activos; `restore` busca sin ese filtro.

El email y el teléfono siguen ocupados por los dados de baja: no se puede crear otro usuario
con el mismo email, hay que restaurar el que está.

### Request

`POST` y `PUT` no llevan el mismo body: el `PUT` solo pisa nombre, email y teléfono, y mandar
`password` o `rol` da 400. El rol no se puede cambiar por API. `DELETE` y `PATCH /restore` no
llevan body.

```json
{
  "name": "Manuel",
  "email": "manuel@mail.com",
  "password": "unaClave123",
  "phoneNumber": "3415551234",
  "rol": "USER"
}
```

| Campo | Tipo | Obligatorio | En el `PUT` | Reglas |
|---|---|---|---|---|
| `name` | string | sí | sí | 3 a 20 |
| `email` | string | sí | sí | formato email, máx 100, único |
| `password` | string | sí | **no** | 8 a 20 |
| `phoneNumber` | string | sí | sí | 8 a 15, único |
| `rol` | enum `UserRol` | sí | **no** | `USER`, `AGENT`, `AGENCY`, `ADMIN` |

El campo es `rol`, no `role`.

`PATCH /{id}/password` lleva la contraseña actual y la nueva. Las dos son obligatorias: sin
la actual no se cambia nada.

```json
{ "currentPassword": "unaClave123", "password": "otraClave123" }
```

Si `currentPassword` no coincide con la guardada, da 400 con
`"Current password does not match"`.

### Response

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

La contraseña nunca sale. `PATCH /password` no devuelve body.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 409 email o teléfono repetidos |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 400 si mandás `password` o `rol` · 404 · 409 email o teléfono de otro usuario |
| `PATCH /{id}/password` | 204 sin body | 400 validación · 400 `currentPassword` incorrecta · 404 |
| `DELETE /{id}` | 204 sin body | 404 |
| `PATCH /{id}/restore` | 200 | 404 |

El 409 dice cuál es el campo repetido (`"Phone number already registered: 3415551234"`).
