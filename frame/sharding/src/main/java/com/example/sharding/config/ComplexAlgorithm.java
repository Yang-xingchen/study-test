package com.example.sharding.config;

import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.*;
import java.util.stream.Collectors;

public class ComplexAlgorithm implements ComplexKeysShardingAlgorithm<Comparable<String>> {

    private String prefix;

    @Override
    public void init(Properties props) {
        ComplexKeysShardingAlgorithm.super.init(props);
        prefix = props.get("prefix").toString();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<Comparable<String>> complexKeysShardingValue) {
        Map<String, Collection<Comparable<String>>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        Set<String> uid;
        if (columnNameAndShardingValuesMap.containsKey("uid")) {
            uid = columnNameAndShardingValuesMap.get("uid")
                    .stream()
                    .map(Objects::hashCode)
                    .map(i -> i & 1)
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        } else {
            uid = collection
                    .stream()
                    .map(table -> table.split("_")[2])
                    .collect(Collectors.toSet());
        }
        Set<String> gid;
        if (columnNameAndShardingValuesMap.containsKey("gid")) {
            gid = columnNameAndShardingValuesMap.get("gid")
                    .stream()
                    .map(Objects::hashCode)
                    .map(i -> i & 1)
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        } else {
            gid = collection
                    .stream()
                    .map(table -> table.split("_")[3])
                    .collect(Collectors.toSet());
        }
        Set<String> res = new HashSet<>();
        uid.forEach(user -> gid.forEach(good -> res.add(prefix + "_" + user + "_" + good)));
        return res;
    }

}
