# CinemaProject

## Описание проекта

CinemaProject — это веб-приложение для управления бронированием билетов в кинотеатр, 
сеансами фильмов и местами. Система интегрирована с Kafka и ZooKeeper для обработки 
сообщений, связанных с процессом бронирования и планирования сеансов.

### Текущее состояние проекта

**Реализовано**:
- CRUD операции для основных сущностей (фильмы, сеансы, билеты, места).
- API эндпоинты для работы с фильмами и сеансами.
- Интеграция с Kafka для обработки сообщений (например, уведомления о сеансах и бронировании мест).
- Базовая настройка ZooKeeper для координации Kafka.

### Эндпоинты:

**Фильмы(всё реализовано)**:
1. `POST http://localhost:8080/movies` — Добавление нового фильма.
2. `POST http://localhost:8080/movies/batch` - добавление списка фильмов
3. `PUT http://localhost:8080/movies/?id=2`  - редактирование фильма
4. `GET {{base_url}}/movies` — Получение списка всех фильмов.
5. `GET {{base_url}}/movies/{id}` — Получение информации о конкретном фильме.
6. `GET http://localhost:8080/movies/search?title=In` — Получение списка фильмов с данным совпадением в заголовке.
7. `DELETE http://localhost:8080/movies/?id=1` Удаление фильма по айди 
8. `DELETE http://localhost:8080/movies` - Удаление всех фильмов


**Комнаты(всё реализовано)**
1. `POST http://localhost:8080/rooms` — Добавление новой комнаты
2. `POST http://localhost:8080/rooms/batch` - добавление списка комнат
3. `GET http://localhost:8080/rooms` — Получение комнат для просмотра фильмов
4. `GET http://localhost:8080/rooms/?id=1`  — Получение информации о комнате по айди 
5. `GET http://localhost:8080/rooms/search/?name=VIP`  — Получение информации о комнате по совпадению названия
6. `GET http://localhost:8080/rooms/searchByNumber/?number=101`  — Получение информации о комнате по номера
7. `PUT http://localhost:8080/rooms/?id=2`  - редактирование комнаты
8. `DELETE http://localhost:8080/rooms/?id=1` Удаление комнаты по айди
9. `DELETE http://localhost:8080/rooms` - Удаление всех комнат





8. `POST {{base_url}}/sessions` — Создание нового сеанса.
8. `GET {{base_url}}/sessions` — Список всех сеансов.
9. `GET {{base_url}}/sessions/by-date?date=2024` — Список всех сеансов по дате.
10. `POST {{base_url}}/tickets` — Бронирование билета.
11. `GET {{base_url}}/tickets/{id}` — Получение информации о конкретном билете.

### Будущие разработки

**Планируемые функции**:
- Реализовать уведомления в реальном времени при успешном бронировании и начале сеанса.
- Продвинутые алгоритмы для работы с выбором мест?
- Интеграция с внешними API для обработки платежей?
- Улучшение работы Kafka для системных уведомлений и логирования.
- Аутентификация и управление сессиями пользователей?

### Текущие задачи и планы

**Что нужно доработать**:
- Улучшить валидацию данных при бронировании? 
- надо ли вводить роль администратора для управления системой(удалить/добавить сеанс) а для остальным пользователей нет?
- Добавить обработку ошибок и стандартизацию ответов API.
- 
- Полная настройка ZooKeeper для масштабируемости Kafka.

### Установка

1. Клонируйте проект с репозитория.
2. Установить все зависимости с помощью Maven.

**Требования**:
- Java 9+
- Apache Kafka
- ZooKeeper
- Spring Boot
- Maven
