package spring.mybatis.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;
    private String name;
    private Date createTime;
//    private Set<Order> orders;
}
