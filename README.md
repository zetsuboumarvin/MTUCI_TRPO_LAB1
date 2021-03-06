[![Build Status](https://travis-ci.org/zetsuboumarvin/MTUCI_TRPO_LAB1.svg?branch=master)](https://travis-ci.org/zetsuboumarvin/MTUCI_TRPO_LAB1)
# Технологии разработки ПО
## Лабораторная работа №1: создание микросервиса на Spring Boot с базой данных
### Мареев Никита, группа 3MAC2001
#### Цель лабораторной работы - создание микросервиса на Spring Boot с использованием базы данных PostgreSQL, Docker-контейнеризации, юнит-тестирования и LiquiBase для составления БД. Микросервис представляет собой приложение по управлению паркингом. В нём реализовано API для управления паркоместами, для управления бронированием этих мест, а также имеются методы для работы с базой пользователей (на текущий момент на заглушках, подразумевается использование SecurityContext). Таблица с бронированиями имеет связь один-к-одному с таблицей паркомест.

### Инструкция по развёртыванию
1. В любом удобном месте на локальном компьютере необходимо открыть консоль (везде в консольных командах будет использоваться синтаксис Unix систем, на Windows можно воспользоваться MinGW), инициализировать гит репозиторий командой `git init`, а затем скачать на компьютер исходный код с помощью команды `git pull https://github.com/zetsuboumarvin/MTUCI_TRPO_LAB1.git`.
2. Создаём базу данных PostgreSQL с помощью команды `docker run -e POSTGRES_PASSWORD=root -p 5432:5432 postgres`, при отсутствии образа docker самостоятельно скачает её из репозитория, после завершения запуска можно свернуть процесс сочетанием Ctrl+C. Находим ID контейнера с помощью `docker ps`, копируем его и проверяем IP адрес контейнера выполнив команду `docker inspect *ID контейнера* | grep "IPAddress"`. Находим поле вида "IPAddress": "172.17.0.2", копируем IP адрес и вставляем его в файле application.properties в поле spring.datasource.url вместо старого IP адреса. Обратите внимание, что порт должен остаться 5432. Файл application.properties находится в этой директории /src/main/resources.
2. После этого необходимо собрать приложение, выполнив команду `mvn package`. В логах консоли отобразится компиляция, и в случае успешного прохождения тестов, сервис соберётся в parking-1.0.jar файл.
3. Собираем docker образ на основе конфигурационного Dockerfile, скаченного из репозитория, выполнив команду `docker build . -t parking`. Проверяем, что образ успешно создался и имеется в списке образов докера при выполнении команды `docker images`.
4. Запускаем образ с помощью команды `docker run -p 8080:8080 parking`, при необходимости можно свернуть процесс сочетанием Ctrl+C. При запуске приложения БД автоматически заполнится тестовыми данными с помощью скриптов LiquiBase.

### Инструкция по использованию приложения
С учётом проброса портов все запросы можно направлять на localhost:8080, ниже представлены доступные точки API с описанием результата и примерами curl запросов.
#### API по пользователям
1. GET /me - возвращает информацию о текущем пользователе (в данной версии на заглушке) с указанием полномочий пользователя, его бронированиях и парковочных местах, которые он забронировал. `curl -L -X GET 'localhost:8080/me'`
#### API по паркоместам
2. GET /parking-places - возвращает все имеющиеся парковочные места с поддержкой пагинации. `curl -L -X GET 'localhost:8080/parking-places'`
3. GET /parking-places/{id} - возвращает информацию о конкретном парковочном месте с заданным в параметре id. `curl -L -X GET 'localhost:8080/parking-places/2'`
4. POST /parking-places - сохраняет заданное в теле парковочное место в БД
```
curl -L -X POST 'localhost:8080/parking-places/' \
-H 'Content-Type: application/json' \
--data-raw '{"id":null,"address":"address2","zone":"yellow","floor":2,"forDisabled":false,"reservationDto":{}}'
```
5. PATCH /parking-places/{id} - вносит изменения в существующее парковочное место в соответствии с параметрами парковочного места в теле запроса.
```
curl -L -X PATCH 'localhost:8080/parking-places/1' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=627777763CF9120057A2F6AF011FB377' \
--data-raw '{"id":null,"address":null,"zone":"red","floor":null,"forDisabled":false,"reservationDto":{}}'
```
6. DELETE /parking-places/{id} - удаляет парковочное место с соответствующим id. `curl -L -X DELETE 'localhost:8080/parking-places/5'`
7. POST /parking-places/search - ищет парковочное место с соответствующими параметрами поиска, переданными в теле запроса, поддерживает пагинацию.
```
curl -L -X POST 'localhost:8080/parking-places/search?size=5&page=0' \
--header 'Content-Type: application/json' \
--data-raw '{"id":null,"address":null,"zone":"red","floor":null,"forDisabled":false}'
```
#### API по бронированиям
8. GET /reservation - возвращает все имеющиеся бронирования с поддержкой пагинации для пользователей с доступом "ADMIN" или бронирования, относящиеся к конкретному пользователю, при запросе с доступом "USER"(в текущей реализации захардкожен пользователь "USER"). `curl -L -X GET 'localhost:8080/reservation'` 
9. GET /reservation/{id} - возвращает бронирование по его айди. `curl -L -X GET 'localhost:8080/reservation/1'`
10. PUT /reservation/{id} - создаёт новое бронирование на парковочное место с указанным id, если оно занято - выдаст соответствующее исключение. `curl -L -X PUT 'localhost:8080/reservation/4'`
11. DELETE /reservation/{id} - удаляет бронирование с переданным в параметре id, если бронирование принадлежит пользователю. `curl -L -X DELETE 'localhost:8080/reservation/3'`
#### Hostname
12. GET /hostname - возвращает hostname. `curl -L -X GET 'localhost:8080/hostname'`


## Лабораторная работа №3: CI/CD и деплой приложения в Heroku
#### Цель лабораторной работы - настройка CI на Travis и автоматический деплой микросервиса в Heroku.
Приложение в [Heroku](https://mtuci.herokuapp.com/).  
[![Build Status](https://travis-ci.org/zetsuboumarvin/MTUCI_TRPO_LAB1.svg?branch=master)](https://travis-ci.org/zetsuboumarvin/MTUCI_TRPO_LAB1)    
[SonarCloud](https://sonarcloud.io/organizations/zetsuboumarvin-parking/projects)
