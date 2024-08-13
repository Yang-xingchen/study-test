package com.example.sharding.config;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;

public class SingleAlgorithm implements StandardShardingAlgorithm<Comparable<String>> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Comparable<String>> preciseShardingValue) {
        Comparable<String> value = preciseShardingValue.getValue();
        int index = Math.abs(value.hashCode() % collection.size());
        return collection.stream().skip(index).findFirst().get();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Comparable<String>> rangeShardingValue) {
        return collection;
    }

}
