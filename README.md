# Пятое задание курса [java-backend-learning-course](https://zhukovsd.github.io/java-backend-learning-course/)

[![Java CI with Maven](https://github.com/farneser/weather-viewer/actions/workflows/maven.yml/badge.svg)](https://github.com/farneser/weather-viewer/actions/workflows/maven.yml)

## [Задание](https://zhukovsd.github.io/java-backend-learning-course/Projects/WeatherViewer/)

Веб-приложение для просмотра текущей погоды. Пользователь может зарегистрироваться и добавить в коллекцию один или
несколько локаций (городов, сёл, других пунктов), после чего главная страница приложения начинает отображать список
локаций с их текущей погодой.

## Запуск проекта

Стандартные значения запуска приложения [параметры необходимо установить свои](#установка-переменных-окружения)

```keyvalue
IP_ENV_VARIABLE=localhost
PORT_ENV_VARIABLE=5432
WEATHER_DB_NAME_ENV_VARIABLE=weather-viewer
USERNAME_ENV_VARIABLE=postgres
PASSWORD_ENV_VARIABLE=postgres
WEATHER_DB_NAME_ENV_VARIABLE=your_api_key
```

### Запуск через Intellij Idea

1. Открытие панели конфигураций
2. Выбрать конфигурацию `Tomcat server`
3. В пункте `Application Server` необходимо указать путь до своего [Tomcat](https://tomcat.apache.org/download-10.cgi)
   сервера
4. Перейти на вкладку `Startup/Connection` и установить переменные окружения
5. Запуск. Проект будет находиться по пути http://localhost:8080/weather_viewer_war_exploded/

### Ручная сборка и запуск

#### Установка переменных окружения

##### Установка переменных окружения на Linux и MacOS

```bash
export VARIABLE_NAME=variable_value
```

##### Установка переменных окружения на Windows

```batch
set VARIABLE_NAME=variable_value
```

#### Сборка

```bash
git clone https://github.com/farneser/weather-viewer
cd weather-viewer
./mvnw install
./mnvw war:war
```

War файл будет находиться по пути `./targer/weather-viewer-1.0.war`

#### Запуск

1. Скачать сервер [Tomcat](https://tomcat.apache.org/download-10.cgi)
2. Распаковать архив с сервером
3. Поместить файл `weather-viewer-1.0.war` в папку `SERVER_PATH/webapps/`
4. Запуск сервера `./SERVERPATH/bin/startup.sh`
5. Приложение будет находиться по пути http://localhost:8080/weather-viewer-1.0/

## Что нужно знать

- [Java](https://zhukovsd.github.io/java-backend-learning-course/Technologies/Java/) - коллекции, ООП
- [Паттерн MVC(S)](https://zhukovsd.github.io/java-backend-learning-course/Technologies/Java/#mvc)
- [Maven/Gradle](https://zhukovsd.github.io/java-backend-learning-course/Technologies/BuildSystems/)
- [Backend](https://zhukovsd.github.io/java-backend-learning-course/Technologies/Backend/)
    - Java сервлеты
    - GET и POST запросы, HTTP заголовки, cookies
    - Thymeleaf
- [Базы данных](https://zhukovsd.github.io/java-backend-learning-course/Technologies/Databases/)
    - SQL
    - Hibernate
- [Frontend](https://zhukovsd.github.io/java-backend-learning-course/Technologies/Frontend/) - HTML/CSS, Bootstrap
- [Тесты](https://zhukovsd.github.io/java-backend-learning-course/Technologies/Tests/) - интеграционное тестирование,
  моки, JUnit 5
- [Деплой](https://zhukovsd.github.io/java-backend-learning-course/Technologies/DevOps/#деплой) - облачный хостинг,
  командная строка Linux, Tomcat

## Функционал приложения

Работа с пользователями:

- Регистрация
- Авторизация
- Logout

Работа с локациями:

- Поиск
- Добавление в список
- Просмотр списка локаций, для каждой локации отображается название и температура
- Удаление из списка

## Работа с сессиями и cookies

Чтобы реализовать авторизацию пользователя и позволить браузеру "запоминать" авторизован ли текущий пользователь,
необходимы сессии и cookies. Подразумевается, что студент имеет общее представление об этих понятиях.

При авторизации пользователя бэкенд приложение создаёт сессию с идентификатором, и устанавливает этот идентификатор в
cookies HTTP ответа, которым приложение отвечает на POST запрос формы авторизации. К тому же, сессия содержит в себе ID
авторизовавшегося юзера.

Далее, при каждом запросе к любой странице, бэкенд приложение анализирует cookies из запроса и определяет, существует ли
сессия для ID из cookies. Если есть - страница рендерится для того пользователя, ID которого соответствует ID сессии из
cookies.

Фреймворки, в том числе Spring Boot, умеют управлять всем этим. Цель этого проекта - поработать с cookies и сессиями
вручную, чтобы понимать, как они работают.

## Получение информации о погоде с помощью OpenWeatherMap API

В предыдущих проектах мы писали своё API, в этом будем пользоваться чужим. Нам нужно искать локации по названию и
получать погоду для локации.

### OpenWeather

Существует множество сервисов, предоставляющих API с таким функционалом, один из
них - [https://openweathermap.org/](https://openweathermap.org/). Я выбрал этот вариант, потому что он позволяет
бесплатно совершать 60 запросов в минуту.

Для выполнения запросов к API нужен ключ, для его получения необходимо:

- Зарегистрироваться на [https://openweathermap.org/](https://openweathermap.org/)
- Создать бесплатный ключ с лимитом 60 запросов в минуту
- Дождаться активации ключа (

### Работа с API

Документация - [https://openweathermap.org/api](https://openweathermap.org/api).

Нам нужно 2 метода API:

- Поиск локаций по
  названию - [https://openweathermap.org/current#geocoding](https://openweathermap.org/current#geocoding)
- Получение погоды по координатам
  локации - [https://openweathermap.org/current#one](https://openweathermap.org/current#one)

Первым делом следует поэкспериментировать с API вручную, чтобы понять как делать запросы, и что приходит в ответе.
Сделать это можно в [HTTP клиенте](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html)
встроенном в Intellij IDEA, либо воспользоваться отдельным приложением,
например [https://insomnia.rest/](https://insomnia.rest/).

### Интеграция OpenWeather API с Java приложением

Шаги:

- Делаем запрос
- Получаем ответ
- Десериализуем ответ в объект Java

Для работы с API потребуется HTTP Client,
например [https://www.baeldung.com/java-9-http-client](https://www.baeldung.com/java-9-http-client).

С десериализацией поможет `JsonMapper` из библиотеки Jackson.

## Тесты

### Интеграционные тесты сервисов по работе с пользователями и сессиями

Покроем тестами связку слоя данных с классами-сервисами, отвечающими за пользователей и сессии.

Пример - вызов метода регистрации в классе-сервисе, отвечающем за работу с пользователями, должен привести к тому, что в
таблице `Users` появляется новая запись. Если при регистрации автоматически создается сессия, так же можем проверить,
что она была создана.

Что ещё стоит проверить тестами:

- Регистрация юзера с неуникальным логином приводит к exception
- Истекание сессии

Детали:

- Для тестов должна использоваться БД, отдельная от основной (in-memory БД или независимая schema в основной БД),
  которая пересоздается (или очищается) перед каждым тест кейсом
- Понадобится 2 конфигурации приложения - основная (для разработки и деплоя) и для прогона тестов. Конфигурации могут
  отличаться настройками доступа к БД для Hibernate, настройками приложения (длительность сессии, например)

### Интеграционные тесты для сервиса по работе с OpenWeather API

Покроем тестами связку HTTP клиента и класса-сервиса, который пользуется этим клиентом для получения данных. Для того
чтобы не делать настоящие запросы к API во время прогона тестов, следует использовать мок HTTP клиента и его ответов.

Пример - запрашиваем список локаций у сервиса, мок HTTP клиента возвращает заданный в тесте ответ (в виде строки,
например), сервис его парсит и возвращает коллекцию объектов-моделей. Проверяем, что коллеция содержит ожидаемую
локацию.

Что ещё стоит проверить тестами:

- В случай ошибки (статусы 4xx, 5xx) от OpenWeather API сервис выбрасывает ожидаемый тип исключения

## Деплой

Будем вручную деплоить war артефакт в Tomcat, установленный на удалённом сервере. Потребуется установка внешней SQL БД
по выбору.

Шаги:

- Локально собрать war артефакт приложения
- В хостинг-провайдере по выбору арендовать облачный сервер на Linux
- Установить JRE, Tomcat, выбранную SQL БД
- Зайти в админский интерфейс Tomcat, установить собранный war артефакт

Ожидаемый результат - приложение доступно по адресу `http://$server_ip:8080/$app_root_path`.

## План работы над приложением

- Создать заготовку Java бэкенд приложения с `javax.servlet`
- Написать модели сущностей БД - `User`, `Location`, `Session`
- С помощью Thymeleaf создать страницы авторизации и регистрации
- В обработчиках форм авторизации и регистрации реализовать бизнес логику работы с сессиями, cookies, БД
- Интеграционные тесты для сервиса регистрации
- Написать сервис для работы с OpenWeather API
- Интеграционные тесты для OpenWeather API
- Реализовать бизнес логику приложения - поиск, добавление, удаление локаций, просмотр погоды
- Создать интерфейс главной страницы и страницы поиска локаций
- Деплой
