# Kafka常用命令



```shell
#配置连接 Zookeeper 集群地址
zookeeper.connect=192.168.30.129:2181,192.168.30.130:2181,192.168.30.131:2181
192.168.30.128:2181,192.168.30.133:2181,192.168.30.132:2181

#KAFKA_HOME
export KAFKA_HOME=/usr/local/kafka/kafka_2.12-2.8.0
export PATH=$PATH:$KAFKA_HOME/bin

#启动kafka
bin/kafka-server-start.sh -daemon config/server.properties
#kafka-server-start.sh -daemon /usr/local/kafka/kafka_2.12-2.8.0/config/server.properties

#查看有哪些topic
kafka-topics.sh --zookeeper 192.168.30.129:2181 --list

#创建topic topic名称first，分区 2 副本 2
kafka-topics.sh --zookeeper 192.168.30.129:2181 --create --replication-factor 2 --partitions 2 --topic first

#修改topic 分区数
kafka-topics.sh --zookeeper 192.168.30.129:2181 --alter --topic flink-test-04 --partitions 3

#删除topic
kafka-topics.sh --zookeeper 192.168.30.129:2181 --delete --topic first

#发送消息
kafka-console-producer.sh --broker-list 192.168.30.129:9092 --topic first

#消费消息
kafka-console-consumer.sh --bootstrap-server 192.168.30.129:9092 --topic first

#指定配置文件启动消费者
#bin/kafka-console-consumer.sh --zookeeper 192.168.30.128:2181 --topic qinhao --consumer.config config/consumer.properties

bin/kafka-console-consumer.sh --bootstrap-server 192.168.30.128:9092 --topic qinhao --consumer.config config/consumer.properties

#生产消息
 bin/kafka-console-producer.sh --broker-list 192.168.30.128:9092 --topic first

#查看topic的详细信息
kafka-topics.sh --zookeeper 192.168.30.129:2181 --describe --topic first



#zookeeper启动


```



kafka监控

配置环境变量：

```
export KE_HOME=/usr/local/kafka/eagle/kafka-eagle-web-2.0.6
export PATH=$PATH:$KE_HOME/bin
```



# 问题：

1.Kafka 中的 ISR(InSyncRepli)、OSR(OutSyncRepli)、AR(AllRepli)代表什么？

ISR:内部副本同步队列

OSR:外部副本同步队列

AR = ISR + OSR

内部副本同步队列，它由leader维护，存放与leader在一定延迟时间和延迟条数（0.9.x版本后就只支持延迟时间）的follower。如果在ISR中的follower与leader超过了当前设定的延迟时间，就将其从ISR移动到OSR，如果follower与leader之间没有超过当前设定的延迟时间，则将其从OSR移动到ISR。新加入的follower都先放入OSR。



2.Kafka 中的 HW、LEO 等分别代表什么？

HW:最高水位，消费者可见的最大的offset

LEO:每个副本自己最大的offset值



3.Kafka 中是怎么体现消息顺序性的？

区内有序性，每个分区的消息在写入时是有序的，消费时，同一个主题的不同分区的消息只能被同一个消费者组的不同消费者消费。整个topic不保证有序性，如果想整个topic都有序，则将分区数调整为1



4.Kafka 中的分区器、序列化器、拦截器是否了解？它们之间的处理顺序是什么？ 

拦截器 > 序列化器 > 分区器



5.Kafka 生产者客户端的整体结构是什么样子的？使用了几个线程来处理？分别是什么？



![1630552675587](assets\1630552675587.png)



两个线程：main线程，sender线程

Producer的main线程将数据ProducerRecord放进RecordAccumulator，此过程经历：拦截器，序列化器，分区器。最终由Sender线程将消息发送给Kafka对应的topic



6.“消费组中的消费者个数如果超过 topic 的分区，那么就会有消费者消费不到数据”这句话是否正确？ 

是正确的，因为默认同一消费者组的不同消费者只能消费同一topic的不同分区，不能消费相同的分区。

如果自定义分区策略，则可以实现同一消费者组的不同消费者消费同一topic的不同分区。



7.消费者提交消费位移时提交的是当前消费到的最新消息的 offset 还是 offset+1？

 offset + 1



8.有哪些情形会造成重复消费？

消费者消费后没有提交offset(程序崩溃/强行kill/消费耗时/自动提交偏移情况下unsubscrible)



9.那些情景会造成消息漏消费？

消费者没有处理完消息就提交offset(自动提交偏移 未处理情况下程序异常结束)



10.当你使用 kafka-topics.sh 创建（删除）了一个 topic 之后，Kafka 背后会执行什么逻辑？ 

 1）会在 zookeeper 中的/brokers/topics 节点下创建一个新的 topic 节点，如： 

/brokers/topics/first 

 2）触发 Controller 的监听程序 

 3）kafka Controller 负责 topic 的创建工作，并更新 metadata cache 

创建：在zk上/brokers/topics/下创建一个新的topic节点，然后触发Controller的监听程序，kafkabroker会监听节点变化创建topic，kafka Controller 负责topic的创建工作，并更新metadata cache
删除：调用脚本删除topic会在zk上将topic设置待删除标志，kafka后台有定时的线程会扫描所有需要删除的topic进行删除，也可以设置一个配置server.properties的delete.topic.enable=true直接删除



11.topic 的分区数可不可以增加？如果可以怎么增加？如果不可以，那又是为什么？ 

12.topic 的分区数可不可以减少？如果可以怎么减少？如果不可以，那又是为什么？ 

可增不可减，减少的分区的数据难以解决。

增加分区：kafka-topics.sh --zookeeper 192.168.30.129:2181 --alter --topic flink-test-04 --partitions 3



13.Kafka 有内部的 topic 吗？如果有是什么？有什么所用？ 

有，__consumer_offsets，作用保存消费者的offset



14.Kafka 分区分配的概念？ 

一个topic会有多个分区，一个消费者组有多个消费者，kafka需要确定消费者消费哪个分区的消息。

分配有两种策略：

RoundRobin:面向组来分配，轮询的分配，该策略需要保证当前消费者订阅的topic相同

Range:按topic来分配，可以不需要消费者订阅相同的topic，确定当前topic有多少消费者，有多少分区，按一定比例进行分配。



15.简述 Kafka 的日志目录结构？ 

![1630571221630](assets\1630571221630.png)

每个partition一个文件夹，包含四类文件.index .log .timeindex leader-epoch-checkpoint
.index .log .timeindex 三个文件成对出现 前缀为上一个segment的最后一个消息的偏移
log文件中保存了所有的消息
index文件中保存了稀疏的相对偏移的索引
timeindex保存的则是时间索引
leader-epoch-checkpoint中保存了每一任leader开始写入消息时的offset，会定时更新
follower被选为leader时会根据这个确定哪些消息可用

16.如果我指定了一个 offset，Kafka Controller 怎么查找到对应的消息？ 

通过二分查找法，定位到哪个具体的index文件，通过offset找到具体的物理地址偏移量

![1630574084732](assets\1630574084732.png)

![1630574108639](assets\1630574108639.png)

“.index”文件存储大量的索引信息，“.log”文件存储大量的数据，索引文件中的元数据指向对应数据文件中 message 的物理偏移地址



17.聊一聊 Kafka Controller 的作用？ 

负责管理集群broker的上下线，所有topic的分区副本分配和leader选举等工作



18.Kafka 中有那些地方需要选举？这些地方的选举策略又有哪些？ 

partition leader（ISR），controller（抢占资源，先到先得）



19.失效副本是指什么？有那些应对措施？ 

OSR中存放的副本

不能及时与leader同步，暂时踢出ISR，等其与leader之前的时间延迟在设定范围之内之后再重新加入



20.Kafka 的哪些设计让它有如此高的性能？

分区

Cache Filesystem Cache PageCache缓存

顺序写磁盘，由于现代的操作系统提供了预读和写技术，磁盘的顺序写大多数情况下比随机写内存还要快。

Batching of Messages 批量处理。合并小的请求，然后以流的方式进行交互，直顶网络上限

Pull 拉模式 使用拉模式进行消息的获取消费，与消费端处理能力相符。

0-copy 零拷技术减少拷贝次数


