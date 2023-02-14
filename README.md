## Итоговая работа по модулю SPRING «RESTFul API права пользователя».

![logo](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTFhVIKHS77LnlY32T-ZIEsxpSBLUK6D0PRKg&usqp=CAU)

### **Задача:**  
- разработать API сервис авторизации пользователей по логину и паролю, соответствующее принципам RESTFul.  
- реализовать обработчик ошибок.
- реализовать обработчик пользовательских параметров (@Аннотация)  
- реализовать хранение базы данных IN MEMORY (Hibernate)
#### *Дополнительное задание:*
- при запросе на удаление пользовательские данные не удаляют, а помечают на удаление, соответственно при запросе возвращаются данные, без отметки на удаление. 
- добавить возможность авторизоваться из HTML-формы (1 общая форма на авторизацию и добавление/обновление)  
- реализовать хранение базы данных на сервере Apache(NGINX) (PostgreSQL, MySQL)

### Ресурсы: 
Основная библиотека: **Spring-boot-starter-web**.

1. для работы с базой данных используется библиотека `spring-boot-starter-data-jpa`, подключение:
 
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
2. для хранения базы данных IN MEMORY используется библиотека `Hibernate`, подключение:

       <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
        </dependency> 
        
3. для хранения базы данных на сервере используется библиотека `mysql-connector-j`, подключение:

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency> 
        
4. для валидации пользовательских параметров используется библиотека `spring-boot-starter-validation`, подключение:
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
5. Файл application.properties - настройки конфигурации проекта:  
<code>server.port = 8085
    #Set true for upload data during configuration class LoadDataBase
    userTable.data.preload=false
    #Comment this block if decided to use DB in H2:MEMORY
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/user_restfull_db
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver</code>
6. Форма для авторизации/добавления пользователя находится в /resources/static/user-form.html.      
    

## Реализация. 

### Web-сервер - Apache, cервер базы данных - MySQL, администрирование - PhpMyAdmin.

| **Класс** | **Описание** |
| --------- | ----------- |
| `RestfullApplication` | Запуск SpringBoot приложения |
| **Основные классы** |
| `UserController`| Класс-обработчик end-point'ов. |
| `UserService`| Класс, реализующий основную логику обработки данных. |
| `UserRepository`| `Interface`  для храния данных, наследует `JpaRepository`. |
| **Классы данных** |
| `User`| Класс , описывающий модель пользователя. |
| `Authorities`| Класс Enum, описывающий перечень прав пользователя. |
| `UserModelAssembler`| Класс , реализующий представление ответа в формате RESTFul. |
| **Классы конфигурации** |
| `LoadDataBase`| Реализует предзагрузку БД, если userTable.data.preload=true. |
| `WebMvcConfig`| Добавляет в конфигурацию обработчик пользовательских параметров (@UserParam) `MyHandlerMethodArgumentResolver` |
| **Классы обработки аннотаций** |
| `MyHandlerMethodArgumentResolver`| Класс-обработчик пользовательских параметров (@UserParam). |
| **Классы обработки исключений** |
| `exeption`| Package пользовательских исключений. Описывает 3 класса исключений: `UnauthorizedUser`, `NotFoundException`, `InvalidCredentials` |
| `ExceptionHandlerAdvice`| Класс-обработчик исключений. |

#### Пример ответа на запрос:
```json
{
  "id": 1,
  "login": "user1",
  "password": "admin",
  "authorities": [
    "READ",
    "WRITE"
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8085/restAPI/users/1"
    },
    "/restAPI/users": {
      "href": "http://localhost:8085/restAPI/users"
    }
  }
}
```

#### HTML-форма авторизации:

```
<form action="http://localhost:8085/restAPI/users/form" id="user-form">
    <label for="id_login">введите логин</label>
    <input name="login" placeholder="Login" id="id_login">
    <label for="id_password">введите пароль</label>
    <input name="password" placeholder="Password" id="id_password">
    <br><br>
    Authorities:
    <input type="checkbox" name="authorities" value="READ" title="READ" required checked/>READ
    <input type="checkbox" name="authorities" value="WRITE"/>WRITE
    <input type="checkbox" name="authorities" value="DELETE"/>DELETE
    <br><br>
    <button id="get-button" formmethod="get">Login</button>
    <button id="post-button" formmethod="post">Add User</button>
</form>
```
