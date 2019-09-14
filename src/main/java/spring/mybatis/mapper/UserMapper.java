package spring.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import spring.mybatis.model.Order;
import spring.mybatis.model.User;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Insert({
            "INSERT into t_user (name, create_time)",
            "values (#{name, jdbcType=VARCHAR}, now())"
    })
    @Options(useGeneratedKeys = true)
    public int save(User user);

    @Select({
            "SELECT *",
            "FROM t_user",
    })
    @Results(id = "userMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "id", property = "orders",
                    many = @Many(select = "findOrdersByUser", fetchType = FetchType.EAGER))
    })
    List<User> findAll();

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE user=#{uid}"
    })
    @ResultMap("spring.mybatis.mapper.OrderMapper.orderMap")
    public List<Order> findOrdersByUser(@Param("uid") int id);

    @Select({
            "SELECT *",
            "FROM t_user",
            "WHERE id=#{id,jdbcType=INTEGER}"
    })
    @ResultMap("userMap")
    public User findById(@Param("id") int id);



}
