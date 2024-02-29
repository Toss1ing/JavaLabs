# Проект "To-do list"
## Описание
Проект "To-do list" представляет собой легковесный веб/REST сервис, созданный с целью предоставления простого и локального функционала. Проект включает в себя реализацию запросов в формате web/REST, а также предоставляет возможность выполнения запросов, возвращая результат в формате JSON.

## Технологии
- Java spring boot.
- postgreSQL.

## Руководство по установке Spring Boot 

### Шаг 1 Установка Java Development Kit:
Проверьте установлен ли [JDK](https://www.oracle.com/java/technologies/javase-downloads.html) на вашем устройстве 8 версии и выше

### Шаг 2 Установка среды разработки:
Установите среду разработки на ваш вкус: Visual Studio Code, IntelliJ IDEA,

### Шаг 3 Установка Spring Boot:
#### Используя Maven

1. Добавьте зависимость Spring Boot в ваш файл `pom.xml`:

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter</artifactId>
       </dependency>
   </dependencies>

2. Создайте основной класс с методом main:

```Java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}
```

### Шаг 4: Запуск приложения

Выполните следующую команду Maven для запуска:

```bash
mvn spring-boot:run
```

Ваше приложение будет доступно по адресу http://localhost:8080.

## Результаты тестирования
[ссылка на sonarcloud](https://sonarcloud.io/summary/new_code?id=Toss1ing_JavaLabs)

## Участники
Разработчик - [Кирилл Борисенко](https://t.me/SoMnoiTvo9Maliha).




