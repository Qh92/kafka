package com.qinh.producer;

import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 带回调函数
 *
 * @author Qh
 * @version 1.0
 * @date 2021-08-30-22:56
 */
public class CallbackProducer {

    public static void main(String[] args) {

        //创建配置信息
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.116.128:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //等待时间
        properties.put("linger.ms", 1);

        //创建生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        //发送数据
        for (int i = 0; i < 10; i++) {
            //只指定topic 和 value,key和分区都没指定则轮询向每个分区插入数据
            /*producer.send(new ProducerRecord<>("aaa", "qinhao-" + i), (metadata, exception) -> {
                if (exception == null){
                    System.out.println(metadata.partition() + " -- " + metadata.offset());
                }else {
                    exception.printStackTrace();
                }
            });*/
            //添加指定分区和key，则按指定分区插入数据
            /*producer.send(new ProducerRecord<>("aaa",1,"dasheng", "qinhao-" + i), (metadata, exception) -> {
                if (exception == null){
                    System.out.println(metadata.partition() + " -- " + metadata.offset());
                }else {
                    exception.printStackTrace();
                }
            });*/
            //不添加分区，添加key，则按key的哈希值确定往那个分区插入数据
            producer.send(new ProducerRecord<>("aaa","allen", "qinhao-" + i), (metadata, exception) -> {
                if (exception == null){
                    System.out.println(metadata.partition() + " -- " + metadata.offset());
                }else {
                    exception.printStackTrace();
                }
            });
        }

        //关闭资源
        producer.close();
    }
}
