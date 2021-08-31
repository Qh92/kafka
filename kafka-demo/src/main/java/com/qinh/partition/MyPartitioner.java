package com.qinh.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * 测试自定义分区
 *
 * @author Qh
 * @version 1.0
 * @date 2021/8/31 16:50
 */
public class MyPartitioner implements Partitioner {

    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        //可用分区数
        List<PartitionInfo> partitions = cluster.availablePartitionsForTopic(s);
        //返回分区
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
