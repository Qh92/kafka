package com.qinh.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 生产者
 *
 * @author Qh
 * @version 1.0
 * @date 2021-08-29-21:06
 */
public class MyProducer {

    public static void main(String[] args) {

        //创建Kafka生产者的配置信息
        Properties properties = new Properties();
        //指定连接的Kafka集群，生产环境就要配置多个
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.116.129:9092");
        //ACK应答级别
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        //重试次数
        properties.put("retries", 1);
        //批次大小
        properties.put("batch.size", 16384);
        //等待时间
        properties.put("linger.ms", 1);
        //RecordAccumulator缓冲区的大小
        properties.put("buffer.memory", 33554432);
        //Key,Value的序列化类
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //发送数据
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>("first", "qinhao--" + i));
        }

        //关闭资源，会做数据刷新操作，做资源的回收
        producer.close();



    }
}
