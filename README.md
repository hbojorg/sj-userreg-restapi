# sj-userreg-restapi
RESTful API App to user registration 

Esta RESTful API App está implementada o incluye:
- **Spring Boot 3.4**
- **Java 21**
- **H2 & Spring Data**
- **OpenAPI [Swagger-ui]**
- **JWT para 'Bearer Authorization'**
- **Bean Validation**
- **PUSH, PUT, GET, DELETE (methods)**

### Ejecutar API APP

#### 1. Requisitos previos
Tener instalado en la máquina local:

- **Java 21**
- **Apache Maven 3.6.x**

#### 2. Descargar el repositorio de la app
descargar --> https://github.com/hbojorg/sj-userreg-restapi


#### 3. Database script
No es necesario ningún script para la creación de la DB, ya que por la configuración que se agregó en el properties esto se lo hace automáticamente al momento de levantar la app.

#### 4. Compilar y ejecutar la API app
Posicionándose al interior de la carpeta principal del proyecto, ejecutar desde consola los siguientes comandos de Maven:
    
    - mvn clean
    - mvn install
    - mvn spring-boot:run


#### 5. Iniciar a probar y usar los APIS implementados
Para este propósito se agregó OpenAPI [Swagger-ui] y para usarlo ingresar desde un navegador a la siguiente URL:

    http://localhost:8080/swagger-ui/index.html

##### 5.2 Tokens
Todos los APIS de user requieren de un token, siéntase libre de usar alguna de las siguientes:

    eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzQ0MzE1OTZ9.Rga0F-h1SBFip2DOJpIPXChGec0hkkuXzV-Y4ktMYBg
    eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzQ0MzE3MDB9.bPUFIoWPb5alDii7s-hIB24E4hThmaF3asQrftiD6BU
    eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzQ0MzE3MTN9.MOPyI4YCoKHRZha7SoG-EaXD80RCloLUN3FZn7bhUjk
    eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzQ0MzE3MjJ9.Lm-a8MSOmywW_xIRPL-BLaqfeauwNsHFh1lDzIZZmWY

O también se ha agregado un endpoint [GET /auth/token] para generar un token y usar ese mismo

##### 5.4 Payload or JSON de creación de user
Los campos **email** y **password** son validados dinámicamente por un custom validator a través de sus respectivos regexp
esos regexp son leídos desde el properties, eso hace flexible la modificación de tales regex

Regex Email: todo email address debe tener un top-level domain **cl** o **CL**
Regex Password: como mínimo debe tener 7 caracteres en total, acepta cualquier caracter, excepto espacios

    validation.regex.email = ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[cC][lL]$
    validation.regex.password = ^[^\\s]{7,}$


#### 6. Consultar los registros insertados desde la console H2
En la siguiente URL es posible acceder a la consola H2:

    http://localhost:8080/h2-console/

Usar las siguiente credenciales para la autenticación:
    
    Driver Class: org.h2.Driver
    JDBC URL: jdbc:h2:mem:smartjobdb
    User Name: sa
    Password: password

#### 7. Diagrama de solución
Para esto se ha agregado el diagrama de clases, ver la imagen **diagram.png**


