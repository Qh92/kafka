package com.qinh.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 拦截器生产者测试
 *
 * @author Qh
 * @version 1.0
 * @date 2021/9/1 10:23
 */
public class InterceptorProducer {
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

        //添加拦截器
        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.qinh.interceptor.TimeInterceptor");
        interceptors.add("com.qinh.interceptor.CounterInterceptor");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        //Key,Value的序列化类
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //发送数据
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>("first", "james","qinhao--" + i));
        }

        //关闭资源，会做数据刷新操作，做资源的回收。会执行分区器，拦截器等相关close方法。
        producer.close();
    }
}
