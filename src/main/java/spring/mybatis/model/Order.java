package spring.mybatis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Order implements Serializable {
    private Long id;
    private User user;
    private Set<Commodity> commodities;
}
