# ZooKeeper Overview

ZooKeeper — это централизованный сервис для управления конфигурациями и 
координации распределенных приложений. Он используется для координации работы Apache Kafka.

## Как работают Kafka и ZooKeeper вместе:
- ZooKeeper управляет метаданными о брокерах Kafka и темах.
- Kafka использует ZooKeeper для отслеживания статуса своих узлов.

Без ZooKeeper Kafka не сможет работать корректно в распределенной среде.

# Инструкции по развертыванию Kafka и ZooKeeper

## Запуск ZooKeeper:
1. Перейдите в директорию Kafka:
   cd kafka_2.13-3.5.1
2. Запустите ZooKeeper:
   .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
3. Запуск Kafka
   .\bin\windows\kafka-server-start.bat .\config\server.properties
4. Просмотр существующих тем в Kafka:
   .\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092 