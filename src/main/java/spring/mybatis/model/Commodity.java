package spring.mybatis.model;

import lombok.*;
import org.apache.ibatis.annotations.AutomapConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Commodity implements Serializable {
    private Long id;
    private String name;
    private Set<Order> orders;
    @AutomapConstructor
    public Commodity(){}
}
