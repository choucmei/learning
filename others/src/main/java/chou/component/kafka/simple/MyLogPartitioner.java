package chou.component.kafka.simple;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * key_1 => partition 0
 * key_2 => partition 1
 */
public class MyLogPartitioner implements Partitioner {

    /**
     * @return  partition's id
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String keyString = String.valueOf(key);
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        return Integer.parseInt(keyString.split("_")[1]) - 1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
