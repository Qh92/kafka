package com.qinh.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * 消费者测试
 *
 * @author Qh
 * @version 1.0
 * @date 2021/8/31 17:28
 */
public class MyConsumer {
    
    public static void main(String[] args){
        //创建配置信息
        Properties properties = new Properties();
        //给配置信息赋值
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.30.129:9092");
        //开启自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //自动提交的延迟
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        //key,value的反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "bigdata");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //订阅主题
        consumer.subscribe(Arrays.asList("first","second"));

        while (true){
            //获取主题的数据
            ConsumerRecords<String, String> records = consumer.poll(100);
            //解析并打印数据
            for (ConsumerRecord<String,String> record : records){
                System.out.println(record.key() + " : " + record.value());
            }
        }

        //关闭连接
        //consumer.close();


    }
}
