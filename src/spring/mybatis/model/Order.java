package spring.mybatis.model;

import lombok.Data;

import java.util.Set;

@Data
public class Order {
    private Long id;
    private User user;
    private Set<Commodity> commodities;
}
