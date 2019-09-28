package spring.mybatis.model;

import lombok.*;
import org.apache.ibatis.annotations.AutomapConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@ToString(exclude = {"orders"})
@Builder
public class User implements Serializable {
    private Integer id;
    private String name;
    private Date createTime;
    private List<Order> orders;
    @AutomapConstructor
    public User(){}
}
