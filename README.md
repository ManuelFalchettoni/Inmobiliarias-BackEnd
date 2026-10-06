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

## Modelo de datos

| Dominio | Entidad | Qué guarda |
|---|---|---|
| Core | `Agency` | la inmobiliaria |
| Core | `User` | usuarios y agentes de una inmobiliaria |
| Propiedades | `Property` | el inmueble |
| Propiedades | `PropertyPhoto` | fotos, con su `position` |
| Propiedades | `PropertyPrice` | precio por operación (`SALE`, `RENT`) y moneda |
| Personas y contratos | `People` | clientes y propietarios de la inmobiliaria |
| Personas y contratos | `PropertyOwner` | qué personas son dueñas de qué propiedad |
| Personas y contratos | `PropertyContract` | contrato de venta o alquiler: monto, fechas, estado, documento |
| Personas y contratos | `ContractParty` | quién participa del contrato y con qué `ContractRole` |
| CRM | `CrmProperty` | un cliente interesado en una propiedad, con el agente y la etapa (`CrmStage`) |
| CRM | `CrmHistory` | eventos del lead: nota, llamada, visita, oferta, cambio de etapa |
| CRM | `Offer` | ofertas del lead, con monto, moneda y estado |
| CRM | `CrmAlert` | recordatorios para un agente sobre un lead |

Enums:

- `Currency`: `ARS`, `USD`
- `OperationType` / `ContractType`: `SALE`, `RENT`
- `ContractStatus`: `ACTIVE`, `FINISHED`, `CANCELLED`
- `ContractRole`: `OWNER`, `TENANT`, `BUYER`, `GUARANTOR`
- `CrmStage`: `NEW`, `CONTACTED`, `VISIT`, `NEGOTIATION`, `WON`, `LOST`
- `CrmEventType`: `NOTE`, `CALL`, `VISIT`, `OFFER`, `STAGE_CHANGE`
- `OfferStatus`: `PENDING`, `ACCEPTED`, `REJECTED`

## Casos de uso

### 1. Alta de una inmobiliaria y sus agentes

1. `POST /api/agencies` con `status: "PENDING"`.
2. `PUT /api/agencies/{id}` con `status: "VERIFY"` una vez revisados los datos.
3. `POST /api/users` con `rol: "AGENCY"` y el `agencyId`: el usuario de la inmobiliaria.
4. `POST /api/users` con `rol: "AGENT"` por cada agente, con `cuit` y `license` si los tiene.
5. `GET /api/users?agencyId={id}` para ver el equipo.

### 2. Publicar una propiedad

1. `POST /api/properties`.
2. `POST /api/properties/{id}/photos` con las fotos, de a seis por request.
3. `POST /api/properties/{id}/prices` por cada operación: `SALE`, `RENT` o las dos.
4. `POST /api/people` con el dueño, si no está cargado, y `POST /api/property_owners` para
   vincularlo.
5. `GET /api/properties/{id}`: trae fotos y precios embebidos.

Para sacarla del mercado, `DELETE /api/properties/{id}`; vuelve con `PATCH /{id}/restore`.

### 3. Seguimiento de un interesado

1. `POST /api/people` con nombre, teléfono y email.
2. `POST /api/crm_properties` con la propiedad, la persona, el agente y `stage: "NEW"`.
3. Después de cada contacto, `POST .../history` con el evento (`CALL`, `VISIT`, `NOTE`) y
   `PUT /api/crm_properties/{id}` con la etapa nueva, más su evento `STAGE_CHANGE`.
4. `POST .../alerts` para agendar la visita; `PUT .../alerts/{id}` con `isRead: true` al
   cumplirla.
5. `GET .../history` para ver la línea de tiempo del lead.

### 4. Negociación y cierre con contrato

1. `POST .../offers` con `status: "PENDING"`, su evento `OFFER` en el historial y el lead en
   `NEGOTIATION`.
2. Ante una contraoferta, `PUT .../offers/{id}` con `REJECTED` y un `POST .../offers` nuevo.
3. Al aceptar, la oferta pasa a `ACCEPTED` y el lead a `WON`.
4. `POST /api/property_contracts` con tipo, monto, moneda, fechas y documento.
5. `POST /api/contract_parties` por cada persona: `OWNER`, `TENANT` o `BUYER`, y `GUARANTOR` si
   es alquiler.

Si el contrato se cae, `DELETE /api/property_contracts/{id}` lo pasa a `CANCELLED`.

### 5. Agenda diaria del agente

1. `GET /api/crm_properties?userId={id}`: sus leads.
2. `GET .../alerts` de cada lead, la fecha más próxima primero.
3. Por cada alerta: `PUT` con `isRead: true`, `PUT` con otro `userId` para pasarla, o `DELETE`.
4. `GET .../offers` de cada lead para repasar las pendientes.

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

Aceptan `page`, `size` y `sort`, con tope de 100 por página. Los que tienen baja lógica aceptan
además `active` para elegir entre lo vigente y lo dado de baja (true por defecto).

| Recurso | `size` default | Orden default | Filtros extra |
|---|---|---|---|
| `/api/properties` | 20 | `createdAt` desc | `active`, `agencyId` |
| `/api/agencies` | 20 | `createdAt` desc | `active` |
| `/api/users` | 20 | `createdAt` desc | `active`, `agencyId` |
| `/api/crm_properties` | 20 | `createdAt` desc | `userId` |

`/api/people`, `/api/property_owners`, `/api/property_contracts` y `/api/contract_parties`
aceptan solo `page` y `size`, con 5 por defecto, sin `sort` ni tope.

Lo que cuelga de otro recurso (fotos, precios, ofertas, historial y alertas) sale en un array
común, sin paginar.

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

Al crear, la inmobiliaria del `agencyId` tiene que existir y estar activa.

### Request

Mismo body para `POST` y `PUT`. `DELETE` y `PATCH /restore` no llevan body.

```json
{
  "address": "Av. Siempre Viva 742",
  "type": "HOUSE",
  "province": "Santa Fe",
  "county": "Rosario",
  "city": "Rosario",
  "latitude": -32.9468,
  "longitude": -60.6393,
  "agencyId": 1,
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
| `province` | string | sí | no vacío, máx 50 |
| `county` | string | no | máx 100 (partido o departamento) |
| `city` | string | sí | no vacío, máx 100 |
| `latitude` | number | no | entre -90 y 90 |
| `longitude` | number | no | entre -180 y 180 |
| `agencyId` | number | sí | existente y activa; en el `PUT`, el mismo que ya tiene |
| `year` | number | no | entre 1800 y 2100 |
| `rooms` | number | sí | entero >= 0 |
| `size` | number | sí | entero > 0 |
| `condition` | enum `PropertyCondition` | sí | |
| `occupancy` | enum `PropertyOccupancy` | sí | |
| `floorNumber` | number | no | entero >= 0; 0 es planta baja |

Una propiedad no cambia de inmobiliaria desde el `PUT`: si `agencyId` viene distinto, 400.

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
  "province": "Santa Fe",
  "county": "Rosario",
  "city": "Rosario",
  "latitude": -32.9468,
  "longitude": -60.6393,
  "agencyId": 1,
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
  ],
  "prices": [
    { "id": 5, "operationType": "SALE", "currency": "USD", "amount": 95000.00 }
  ]
}
```

Las fotos vienen embebidas sin orden garantizado; para tenerlas por `position` está el
endpoint de fotos. Los precios vienen igual, en `prices`. Sin fotos o sin precios, la lista viene `[]`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 inmobiliaria inexistente o dada de baja |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 400 `agencyId` distinto al actual · 404 |
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

## Precios de propiedades

`/api/properties/{propertyId}/prices`

| | |
|---|---|
| `POST` | crear |
| `GET` | listar los de una propiedad |
| `GET /{priceId}` | traer uno |
| `PUT /{priceId}` | editar |
| `DELETE /{priceId}` | borrar |

Un precio por operación: una propiedad puede estar en venta y en alquiler, pero no tener dos
precios de venta. El borrado es físico. Como las fotos, un precio solo se ve o se toca desde la
URL de su propia propiedad, y la propiedad tiene que estar activa.

### Request

Mismo body para `POST` y `PUT`.

```json
{ "operationType": "SALE", "currency": "USD", "amount": 95000.00 }
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `operationType` | enum `OperationType` | sí | `SALE`, `RENT`; uno por propiedad |
| `currency` | enum `Currency` | sí | `ARS`, `USD` |
| `amount` | number | sí | > 0, hasta 13 enteros y 2 decimales |

### Response

El mismo objeto con su `id`. `GET` devuelve un array ordenado por `operationType`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 propiedad inexistente o dada de baja · 409 ya hay precio para esa operación |
| `GET` listado | 200 | 404 propiedad inexistente o dada de baja |
| `GET /{priceId}` | 200 | 404 propiedad o precio inexistente · 404 el precio es de otra propiedad |
| `PUT /{priceId}` | 200 | 400 validación · 404 igual que arriba · 409 otro precio ya usa esa operación |
| `DELETE /{priceId}` | 204 sin body | 404 igual que arriba |

## Inmobiliarias

`/api/agencies`

| | |
|---|---|
| `POST /api/agencies` | crear |
| `GET /api/agencies` | listar, paginado |
| `GET /api/agencies/{id}` | traer una |
| `PUT /api/agencies/{id}` | editar |
| `DELETE /api/agencies/{id}` | dar de baja |
| `PATCH /api/agencies/{id}/restore` | restaurar |

Baja lógica, como propiedades. `GET /{id}`, `PUT` y `DELETE` solo ven activas; `restore`
busca sin ese filtro.

La inmobiliaria no tiene contraseña: se entra con un usuario de rol `AGENCY` vinculado por
`agencyId` (`POST /api/users`).

`active` y `status` son cosas distintas: `status` es el circuito de verificación y no se toca
al dar de baja.

Dar de baja una inmobiliaria no da de baja sus propiedades, pero no se le pueden cargar nuevas.

Los únicos siguen ocupados por las dadas de baja: no se puede crear otra con el mismo CUIT,
hay que restaurar la que está.

### Request

`POST` y `PUT` llevan el mismo body; el `PUT` pisa todos los campos. `DELETE` y `PATCH /restore` no llevan body.

```json
{
  "cuit": "30712345678",
  "companyName": "Inmobiliaria Zaguán SRL",
  "publicName": "Zaguán",
  "email": "contacto@zaguan.com",
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
| `phoneNumber` | string | sí | sí | 8 a 15, único |
| `address` | string | sí | sí | 6 a 40, único |
| `webURL` | string | no | sí | máx 255 |
| `socials` | string | no | sí | máx 255 |
| `status` | enum `AgencyStatus` | sí | sí | `PENDING`, `VERIFY`, `DENIED`, `DELETED` |

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

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 409 CUIT, razón social, email o teléfono repetidos · 409 dirección repetida |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 · 409 igual que el `POST` |
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

Al crear, la inmobiliaria del `agencyId` tiene que existir y estar activa.

### Request

`POST` y `PUT` no llevan el mismo body: el `PUT` solo pisa nombre, email, teléfono, CUIT y
matrícula, y mandar `password`, `rol` o `agencyId` da 400. El rol y la inmobiliaria no se pueden cambiar por API. `DELETE` y `PATCH /restore` no
llevan body.

```json
{
  "name": "Manuel",
  "email": "manuel@mail.com",
  "password": "unaClave123",
  "phoneNumber": "3415551234",
  "rol": "USER",
  "agencyId": 1,
  "cuit": "20301234567",
  "license": "CMCPSI 1234"
}
```

| Campo | Tipo | Obligatorio | En el `PUT` | Reglas |
|---|---|---|---|---|
| `name` | string | sí | sí | 3 a 20 |
| `email` | string | sí | sí | formato email, máx 100, único |
| `password` | string | sí | **no** | 8 a 20 |
| `phoneNumber` | string | sí | sí | 8 a 15, único |
| `rol` | enum `UserRol` | sí | **no** | `USER`, `AGENT`, `AGENCY`, `ADMIN` |
| `agencyId` | number | sí | **no** | existente y activa |
| `cuit` | string | no | sí | 11 a 13 |
| `license` | string | no | sí | matrícula, máx 20 |

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
  "agencyId": 1,
  "cuit": "20301234567",
  "license": "CMCPSI 1234",
  "createdAt": "2025-09-15T18:22:41.1234",
  "updatedAt": "2025-09-15T18:22:41.1234"
}
```

La contraseña nunca sale. `PATCH /password` no devuelve body.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 inmobiliaria inexistente o dada de baja · 409 email o teléfono repetidos |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 400 si mandás `password`, `rol` o `agencyId` · 404 · 409 email o teléfono de otro usuario |
| `PATCH /{id}/password` | 204 sin body | 400 validación · 400 `currentPassword` incorrecta · 404 |
| `DELETE /{id}` | 204 sin body | 404 |
| `PATCH /{id}/restore` | 200 | 404 |

El 409 dice cuál es el campo repetido (`"Phone number already registered: 3415551234"`).

## Registro

`POST /api/auth/register`

Alta pública de un usuario, aparte de `POST /api/users`: entra con rol `USER` y sin
inmobiliaria. No hay login todavía.

```json
{ "name": "Manuel", "email": "manuel@mail.com", "password": "unaClave123", "phoneNumber": "3415551234" }
```

Las reglas de los campos son las mismas que en `POST /api/users`. Devuelve el usuario como
`GET /api/users/{id}`.

| OK | Errores |
|---|---|
| 201 | 400 validación · 409 email o teléfono repetidos |

## Personas

`/api/people`

| | |
|---|---|
| `POST` | crear |
| `GET` | listar, paginado |
| `GET /{id}` | traer una |
| `PUT /{id}` | editar |

Clientes, interesados y propietarios de cada inmobiliaria. La misma persona puede estar en dos
inmobiliarias, pero no dos veces en la misma: el DNI y el CUIT son únicos por inmobiliaria.

### Request

Mismo body para `POST` y `PUT`. En el `PUT` el `agencyId` se ignora: una persona no cambia de
inmobiliaria.

```json
{
  "agencyId": 1,
  "name": "Juan Pérez",
  "email": "juan@mail.com",
  "phone": "3415559876",
  "address": "Mitre 456",
  "dni": "30123456",
  "cuit": "20301234567"
}
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `agencyId` | number | sí | |
| `name` | string | sí | 3 a 100 |
| `email` | string | sí | formato email, máx 100 |
| `phone` | string | sí | 8 a 15 |
| `address` | string | no | 5 a 150 |
| `dni` | string | no | 6 a 10, único por inmobiliaria |
| `cuit` | string | no | 11 a 13, único por inmobiliaria |

### Response

Los mismos campos con `id`, `createdAt` y `updatedAt`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 409 DNI o CUIT repetidos en la inmobiliaria |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 · 409 igual que el `POST` |

## Dueños de propiedades

`/api/property_owners`

| | |
|---|---|
| `POST` | vincular una persona como dueña de una propiedad |
| `GET` | listar, paginado |
| `GET /{id}` | traer uno |
| `PUT /{id}` | editar |
| `DELETE /{id}` | desvincular |

Una propiedad puede tener varios dueños, pero la misma persona no se vincula dos veces a la
misma propiedad. El borrado es físico.

### Request

Mismo body para `POST` y `PUT`.

```json
{ "propertyId": 7, "peopleId": 3, "comments": "50% de la propiedad" }
```

Los tres campos son obligatorios. `propertyId` y `peopleId` tienen que existir.

### Response

Los mismos campos con su `id`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 propiedad o persona inexistente · 409 la persona ya es dueña |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 vínculo, propiedad o persona inexistente |
| `DELETE /{id}` | 204 sin body | 404 |

## Contratos

`/api/property_contracts`

| | |
|---|---|
| `POST` | crear |
| `GET` | listar, paginado |
| `GET /{id}` | traer uno |
| `PUT /{id}` | editar |
| `DELETE /{id}` | cancelar |

El `DELETE` no borra la fila: pasa el contrato a `CANCELLED`. Sigue saliendo en el listado y en
el `GET /{id}`.

### Request

Mismo body para `POST` y `PUT`. En el `PUT` el `propertyId` se ignora: un contrato no cambia de
propiedad.

```json
{
  "propertyId": 7,
  "type": "RENT",
  "status": "ACTIVE",
  "amount": 450000.00,
  "currency": "ARS",
  "startDate": "2026-10-01",
  "endDate": "2028-09-30",
  "documentURL": "https://..."
}
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `propertyId` | number | sí | |
| `type` | enum `ContractType` | sí | `SALE`, `RENT` |
| `status` | enum `ContractStatus` | sí | `ACTIVE`, `FINISHED`, `CANCELLED` |
| `amount` | number | sí | > 0, hasta 15 enteros y 2 decimales |
| `currency` | enum `Currency` | sí | `ARS`, `USD` |
| `startDate` | fecha | sí | `yyyy-MM-dd` |
| `endDate` | fecha | no | `yyyy-MM-dd`; una venta no tiene fecha de fin |
| `documentURL` | string | sí | |

### Response

Los mismos campos con `id`, `createdAt` y `updatedAt`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 |
| `DELETE /{id}` | 204 sin body | 404 |

## Partes de contratos

`/api/contract_parties`

| | |
|---|---|
| `POST` | agregar una persona a un contrato |
| `GET` | listar, paginado |
| `GET /{id}` | traer una |
| `PUT /{id}` | editar |
| `DELETE /{id}` | sacar del contrato |

El borrado es físico.

### Request

Mismo body para `POST` y `PUT`.

```json
{ "contractId": 4, "peopleId": 3, "role": "TENANT", "comments": "Titular" }
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `contractId` | number | sí | contrato existente |
| `peopleId` | number | sí | persona existente |
| `role` | enum `ContractRole` | sí | `OWNER`, `TENANT`, `BUYER`, `GUARANTOR` |
| `comments` | string | no | |

### Response

Los mismos campos con su `id`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 contrato o persona inexistente |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 |
| `DELETE /{id}` | 204 sin body | 404 |

---

# CRM

Todo el CRM gira alrededor del lead (`CrmProperty`): una persona interesada en una propiedad,
con un agente asignado. Ofertas, historial y alertas cuelgan del lead en la URL, como las fotos
y los precios cuelgan de la propiedad: solo se ven o se tocan desde la URL de su propio lead.

## Leads

`/api/crm_properties`

| | |
|---|---|
| `POST` | crear |
| `GET` | listar, paginado |
| `GET /{id}` | traer uno |
| `PUT /{id}` | editar: cambiar la etapa o reasignar el agente |

No hay `DELETE`: un lead que no avanza pasa a `LOST`.

### Request

Mismo body para `POST` y `PUT`.

```json
{ "propertyId": 7, "peopleId": 3, "userId": 1, "stage": "NEW" }
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `propertyId` | number | sí | propiedad existente y activa |
| `peopleId` | number | sí | persona existente |
| `userId` | number | sí | agente existente y activo |
| `stage` | enum `CrmStage` | sí | `NEW`, `CONTACTED`, `VISIT`, `NEGOTIATION`, `WON`, `LOST` |

### Response

Los mismos campos con `id`, `createdAt` y `updatedAt`.

### Códigos

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 propiedad, persona o agente inexistente o dado de baja |
| `GET` listado | 200 | — |
| `GET /{id}` | 200 | 404 |
| `PUT /{id}` | 200 | 400 validación · 404 lead · 404 igual que el `POST` |

## Ofertas

`/api/crm_properties/{crmPropertyId}/offers`

| | |
|---|---|
| `POST` | crear |
| `GET` | listar las del lead, las más nuevas primero |
| `GET /{offerId}` | traer una |
| `PUT /{offerId}` | editar: aceptar o rechazar con `status` |
| `DELETE /{offerId}` | borrar |

Una oferta rechazada no se borra, pasa a `REJECTED`. El `DELETE` es físico, para una cargada
por error.

```json
{ "amount": 90000.00, "currency": "USD", "status": "PENDING" }
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `amount` | number | sí | > 0, hasta 13 enteros y 2 decimales |
| `currency` | enum `Currency` | sí | `ARS`, `USD` |
| `status` | enum `OfferStatus` | sí | `PENDING`, `ACCEPTED`, `REJECTED` |

El response trae además `id`, `crmPropertyId`, `createdAt` y `updatedAt`.

## Historial

`/api/crm_properties/{crmPropertyId}/history`

| | |
|---|---|
| `POST` | registrar un evento |
| `GET` | la línea de tiempo del lead, lo más nuevo primero |
| `GET /{historyId}` | traer un evento |

Un evento no se edita ni se borra.

```json
{ "userId": 1, "type": "CALL", "comments": "Quiere visitar el sábado" }
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `userId` | number | sí | el agente que registra el evento, existente y activo |
| `type` | enum `CrmEventType` | sí | `NOTE`, `CALL`, `VISIT`, `OFFER`, `STAGE_CHANGE` |
| `comments` | string | no | |

El response trae además `id`, `crmPropertyId` y `createdAt`.

## Alertas

`/api/crm_properties/{crmPropertyId}/alerts`

| | |
|---|---|
| `POST` | crear |
| `GET` | listar las del lead, la fecha más próxima primero |
| `GET /{alertId}` | traer una |
| `PUT /{alertId}` | editar: marcar como leída o pasarla a otro agente |
| `DELETE /{alertId}` | borrar |

El borrado es físico.

```json
{ "userId": 1, "message": "Llamar para confirmar la visita", "alertDate": "2026-10-10T10:00:00", "isRead": false }
```

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---|---|
| `userId` | number | sí | el agente al que le llega, existente y activo |
| `message` | string | sí | no vacío |
| `alertDate` | fecha y hora | sí | `yyyy-MM-ddTHH:mm:ss` |
| `isRead` | boolean | sí | `false` al crear |

El response trae además `id`, `crmPropertyId` y `createdAt`.

### Códigos de ofertas, historial y alertas

| Endpoint | OK | Errores |
|---|---|---|
| `POST` | 201 | 400 validación · 404 lead inexistente · 404 agente inexistente o dado de baja (historial y alertas) |
| `GET` listado | 200 | 404 lead inexistente |
| `GET /{id}` | 200 | 404 lead o registro inexistente · 404 el registro es de otro lead |
| `PUT /{id}` | 200 | 400 validación · 404 igual que arriba |
| `DELETE /{id}` | 204 sin body | 404 igual que arriba |
