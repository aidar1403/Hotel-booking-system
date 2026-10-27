# Spring в Hotel-booking-system

## Запуск
Открой эту папку через File > Open в IntelliJ IDEA 2026.1.
Если IDEA предлагает импорт, выбери Maven. Если значка Maven нет:
правой кнопкой по pom.xml > Add as Maven Project, затем Reload All Maven Projects.
Project SDK и Maven Runner JRE: 21.
Запускай src/main/java/org/example/HotelApplication.java зелёным треугольником.
Или двойным щелчком открой Start-Spring.cmd.
Дождись сообщения Started HotelApplication. Открой http://localhost:8080.

На странице введи имя: оно отправится браузером в GET /api/hello?name=...
GreetingController получает это значение через @RequestParam.
Файл requests.http содержит дополнительные примеры запросов.

Старый Main.java запускает прежнюю консольную программу с PostgreSQL.
HotelApplication запускает веб-сервер. Веб-пример пока не выполняет бронирование
и не использует БД: он проверяет подключение Spring и получение данных пользователя.
Существующие сервисы и репозитории сохранены; их HTTP API — отдельный следующий этап.

## Проверка
В терминале этой папки:
    .\mvnw.cmd verify
    .\mvnw.cmd spring-boot:run
Тест HotelHttpTest поднимает настоящий сервер на свободном порту и отправляет
запросы с разными именами, включая кириллицу и символ &.
Если 8080 занят, останови другой проект или запусти:
    .\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
Тогда адрес будет http://localhost:8081.

## Почему раньше не было Spring
В исходном pom.xml был только драйвер PostgreSQL и Java target 24.
Добавлены Spring Boot 4.1.1, Spring Web MVC, Maven Wrapper и точка веб-запуска.
Версии компилятора и SDK проекта согласованы на Java 21.
В Boot 4 веб-стартер называется spring-boot-starter-webmvc.
В уроках по Boot 3 обычно используется spring-boot-starter-web.

## Значок глобуса
Глобус — функция навигации и запуска запросов в IntelliJ, не признак работоспособности сервера.
В установленной IDEA отключён Ultimate, и Spring Web IDE-плагин из-за этого не загружается.
Полные возможности Spring IDE требуют соответствующей лицензии Ultimate.
Сам сервер, браузер и Maven работают без неё. Покупка/активация лицензии не выполнялась.
https://www.jetbrains.com/help/idea/spring-support.html
https://www.jetbrains.com/help/idea/spring-boot.html

## Другие проекты
Spring подключается в pom.xml/build.gradle каждого проекта, а не устанавливается в Java глобально.
Для нового проекта: File > New > Project > Spring Boot > Java 21 > Maven >
стабильная версия Boot > Spring Web.
Также можно запустить ..\New-Spring-Project.cmd: он создаёт готовую копию
проверенного spring-web-ready с Spring Web, формой и HTTP-контроллером.

Чтобы подключить существующий Maven-проект:
1. Сохрани его pom.xml, добавь parent spring-boot-starter-parent либо BOM, если parent уже нужен проекту.
2. Добавь веб-стартер, Java version и spring-boot-maven-plugin; сохрани остальные зависимости.
3. Создай @SpringBootApplication с main() и SpringApplication.run(...) в родительском пакете.
4. Контроллеры с @RestController должны быть в этом пакете или его подпакетах.
5. Reload Maven, запусти Application и проверь URL из браузера.
Нельзя безопасно заменить pom.xml любого проекта одним шаблоном: у проектов разные
родители, версии, зависимости и структура. Для Gradle меняется build.gradle.

## Настройки компьютера после исправления
JAVA_HOME: C:\Program Files\Java\jdk-21.0.12.1.
В настройках новых проектов выбран JDK 21.
Maven Importing / JDK for importer и Maven Runner / JRE закреплены на 21
в этом проекте и в настройках по умолчанию новых проектов.
Maven Wrapper закреплён на 3.9.11, как встроенный Maven этой версии IntelliJ.
Maven Search Pro отключён через disabled_plugins.txt; встроенный Maven сохранён.

После закрытия IDE Windows не смогла обработать старый служебный сокет .port.
Поэтому через штатный idea.system.path назначен новый кэш:
C:\Users\user\AppData\Local\JetBrains\IntelliJIdea2026.1-spring-repaired
Старая папка кэша сохранена. Настройки IDEA остались в прежней Roaming-папке.
