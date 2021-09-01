package com.qinh.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * 计数拦截器，可以形成拦截器链
 *
 * @author Qh
 * @version 1.0
 * @date 2021/9/1 10:19
 */
public class CounterInterceptor implements ProducerInterceptor<String,String> {

    private int success;
    private int error;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (Objects.nonNull(metadata)){
            success++;
        }else {
            error++;
        }

    }

    @Override
    public void close() {
        System.out.println("success : " + success + " error : " + error);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
