package spring.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import spring.mybatis.model.Order;

import java.util.List;

@Mapper
@Component
public interface OrderMapper {

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE user=#{id,jdbcType=INTEGER}"
    })
    public List<Order> findOrdersByUser(int id);

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE id=#{id}"
    })
    public Order find(int id);

}
