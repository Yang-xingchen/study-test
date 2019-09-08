package spring.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import spring.mybatis.model.Order;
import spring.mybatis.model.User;

import java.util.List;

@Repository
public interface UserMapper {

    @Insert({
            "INSERT into t_user (name, create_time) ",
            "values (#{name, jdbcType=VARCHAR}, now())"
    })
    @Options(useGeneratedKeys = true)
    public int save(User user);

    @Select({
            "SELECT * ",
            "FROM t_user",
            "WHERE id=#{id,jdbcType=int}"
    })
//    @Results({
//            @Result(id=true,column = "id", property = "id"),
//            @Result(column = "create_time", property = "createTime")
//    })
    public User findById(int id);

    @Select({
            "SELECT * ",
            "FROM t_user",
    })
    List<User> findAll();

    @Select({
            "SELECT * ",
            "FROM t_order",
            "WHERE user=#{id}"
    })
    public List<Order> findOrder(int id);



}
