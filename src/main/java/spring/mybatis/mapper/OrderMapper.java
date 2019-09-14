package spring.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import spring.mybatis.model.Order;

@Mapper
@Repository
public interface OrderMapper {

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE id=#{id}"
    })
    @Results(id="orderMap", value = {
            @Result(id=true, column = "id", property = "id"),
            @Result(column = "user", property = "user",
                one = @One(
                        select = "spring.mybatis.mapper.UserMapper.findById"
                )
            )
    })
    public Order find(int id);

}
