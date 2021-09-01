package com.qinh.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 需求：实现一个简单的双 interceptor 组成的拦截链。第一个 interceptor 会在消息发送前将时间
 * 戳信息加到消息 value 的最前部；第二个 interceptor 会在消息发送后更新成功发送消息数或
 * 失败发送消息数
 *
 * 时间拦截器
 *
 * @author Qh
 * @version 1.0
 * @date 2021/9/1 10:11
 */
public class TimeInterceptor implements ProducerInterceptor<String,String> {

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        //创建一个新的ProducerRecord对象，并返回
        return new ProducerRecord<>(record.topic(), record.partition(),record.key(),System.currentTimeMillis() + "," +  record.value());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }


}
