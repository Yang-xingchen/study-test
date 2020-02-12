package spring.mybatis.model;

import lombok.*;
import org.apache.ibatis.annotations.AutomapConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@ToString(exclude = "commodities")
@EqualsAndHashCode(exclude = "commodities")
@Builder
public class Order implements Serializable {
    private Long id;
    private User user;
    private Set<Commodity> commodities;
    @AutomapConstructor
    public Order(){}
}
