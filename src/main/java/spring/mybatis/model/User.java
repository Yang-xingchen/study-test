package spring.mybatis.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class User implements Serializable {
    private Integer id;
    private String name;
    private Date createTime;
    private List<Order> orders;
}
