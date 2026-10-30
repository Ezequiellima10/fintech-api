# API Fintech — Cuentas y usuarios

API REST en Spring Boot para gestionar usuarios y sus cuentas, con persistencia
en MySQL y consulta de la cotización del dólar a una API externa.

## Tecnologías
- Java 21
- Spring Boot 4.1.1 (Web, Data JPA, Validation)
- MySQL 8
- Lombok
- JUnit 5 y Mockito
- springdoc (Swagger/OpenAPI)

## Requisitos
- Java 21. Si se ejecuta por terminal, verificar con `java -version` y, si hace
  falta, apuntar `JAVA_HOME` a un JDK 21.
- MySQL 8 en el puerto 3306, o Docker.

## Cómo ejecutarlo

### 1. Levantar MySQL

**Opción A: con Docker**

Docker Desktop tiene que estar abierto. El comando descarga la imagen `mysql:8.4`
si no está disponible y crea la base y el usuario.

```bash
docker run --name fintech-mysql -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=cambiame -e MYSQL_DATABASE=fintech -e MYSQL_USER=fintech -e MYSQL_PASSWORD=cambiame mysql:8.4
```

Si el puerto 3306 ya está en uso, por ejemplo por un MySQL instalado, usar la
opción B.

**Opción B: con MySQL instalado**

Ejecutar como `root`:

```sql
CREATE DATABASE fintech;
CREATE USER 'fintech'@'%' IDENTIFIED BY 'cambiame';
GRANT ALL PRIVILEGES ON fintech.* TO 'fintech'@'%';
```

En ambos casos, `cambiame` es una contraseña de ejemplo. Las tablas las crea
Hibernate al arrancar la aplicación.

### 2. Configurar la contraseña

La aplicación lee la contraseña de la variable de entorno `DB_PASSWORD`. Tiene que
coincidir con la que se usó en el paso 1.

En IntelliJ: **Run > Edit Configurations > FintechApiApplication > Environment
variables**, y agregar `DB_PASSWORD=cambiame`.

### 3. Ejecutar

Desde IntelliJ, con `FintechApiApplication`, o por terminal:

**Mac / Linux**

```bash
export DB_PASSWORD=cambiame
./mvnw spring-boot:run
```

**Windows (PowerShell)**

```powershell
$env:DB_PASSWORD="cambiame"
.\mvnw.cmd spring-boot:run
```

**Windows (CMD)**

```bat
set DB_PASSWORD=cambiame
mvnw.cmd spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Documentación
Swagger: http://localhost:8080/swagger-ui/index.html

## Modelo
- `Usuario` (1) —— (N) `Cuenta`
- `Cuenta` es una clase abstracta. Sus hijas son `CajaAhorro` y `CuentaCorriente`.
- `CajaAhorro` no puede quedar con saldo negativo.
- `CuentaCorriente` puede extraer hasta su `limiteDescubierto`.

## Estructura
Monolito modular organizado por funcionalidad:

- `usuario`: usuarios.
- `cuenta`: cuentas. Depende de `usuario` a través de su service.
- `cotizacion`: consumo de la API externa dolarapi.
- `shared`: manejo global de excepciones y DTOs compartidos.

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /cuentas | Lista las cuentas de forma paginada |
| POST | /cuentas | Crea una cuenta |
| GET | /cuentas/{id} | Busca una cuenta por id |
| PUT | /cuentas/{id} | Actualiza el alias de una cuenta |
| DELETE | /cuentas/{id} | Elimina una cuenta |
| GET | /usuarios | Lista los usuarios de forma paginada |
| POST | /usuarios | Crea un usuario |
| GET | /usuarios/{id} | Busca un usuario por id |
| GET | /cotizaciones/dolar | Devuelve la cotización del dólar |

## Tests
Son tests unitarios con JUnit 5 y Mockito, y no necesitan MySQL.

```bash
./mvnw test
```

En Windows: `.\mvnw.cmd test`.

## Colección de Postman
`postman/fintech.postman_collection.json`

Incluye casos correctos y de error, con el código esperado en el nombre de cada
pedido. Con una base vacía, tres pedidos de error devuelven otro código, porque
necesitan datos previos.

## Decisiones
- **Herencia:** `SINGLE_TABLE`, porque las dos cuentas comparten casi todos sus datos.
- **Creación de cuentas:** un solo `POST /cuentas`, con `tipoDeCuenta` para elegir la hija.
- **DTOs:** la API no expone las entidades.
- **Errores:** `GlobalExceptionHandler` los centraliza y evita repetir `try/catch`.
- **API externa:** usa `try/catch`, porque puede fallar. Si falla, responde 503.
- **Dinero:** `BigDecimal`, para evitar errores de redondeo.
- **Reglas en la entidad:** cada tipo de cuenta decide cuánto se puede extraer.
- **Contraseña:** se pasa por variable de entorno y no queda en el repo.
- **`limiteDescubierto`:** solo se usa en `CORRIENTE`.
