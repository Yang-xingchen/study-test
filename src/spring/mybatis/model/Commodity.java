package spring.mybatis.model;

import lombok.Data;

import java.util.Set;

@Data
public class Commodity {
    private Long id;
    private String name;
    private Set<Order> orders;
}
