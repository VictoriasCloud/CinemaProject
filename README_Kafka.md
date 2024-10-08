# Интеграция Kafka

## Назначение

Apache Kafka используется для обработки асинхронных сообщений в проекте CinemaProject. 
Это позволяет системе работать с реальными временными событиями, такими как бронирование 
мест или уведомления о сеансах.

## Темы Kafka

- `theatre.infra.bells` — эта тема обрабатывает системные уведомления о сеансах в кинотеатре.
- `__consumer_offsets` — внутренняя тема Kafka для отслеживания групп потребителей.

## Запуск Kafka

1. Запустите ZooKeeper:
   ```sh
   zookeeper-server-start.sh config/zookeeper.properties
2. Запустите Kafka в другом окне
```sh 
    kafka-server-start.sh config/server.properties

