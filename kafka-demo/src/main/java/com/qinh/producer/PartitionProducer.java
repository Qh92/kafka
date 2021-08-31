package com.qinh.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 自定义分区测试
 *
 * @author Qh
 * @version 1.0
 * @date 2021/8/31 17:10
 */
public class PartitionProducer {
    public static void main(String[] args){
        //创建Kafka生产者的配置信息
        Properties properties = new Properties();
        //指定连接的Kafka集群，生产环境就要配置多个
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.30.129:9092");
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

        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.qinh.partition.MyPartitioner");

        //创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //发送数据，异步发送
        for (int i = 0; i < 10; i++) {
            Future<RecordMetadata> send = producer.send(new ProducerRecord<>("first", "qinhao--" + i), (metadata, exception) -> {
                if (exception == null) {
                    System.out.println(metadata.partition() + " -- " + metadata.offset());
                } else {
                    exception.printStackTrace();
                }
            });
            try {
                //使用get()让kafka同步发送，阻塞main线程
                RecordMetadata recordMetadata = send.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


        }

        //关闭资源，会做数据刷新操作，做资源的回收
        producer.close();
    }
}
