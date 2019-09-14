package spring.mybatis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Commodity implements Serializable {
    private Long id;
    private String name;
    private Set<Order> orders;
}
