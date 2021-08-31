# Kafka常用命令



```markdown
#配置连接 Zookeeper 集群地址
zookeeper.connect=192.168.30.129:2181,192.168.30.130:2181,192.168.30.131:2181

#KAFKA_HOME
export KAFKA_HOME=/usr/local/kafka/kafka_2.12-2.8.0
export PATH=$PATH:$KAFKA_HOME/bin

kafka-server-start.sh -daemon /usr/local/kafka/kafka_2.12-2.8.0/config/server.properties

kafka-topics.sh --zookeeper 192.168.30.129:2181 --list

kafka-topics.sh --zookeeper 192.168.30.129:2181 --create --replication-factor 2 --partitions 2 --topic first

kafka-console-producer.sh --broker-list 192.168.30.129:9092 --topic first

kafka-console-consumer.sh --bootstrap-server 192.168.30.129:9092 --topic first
```

