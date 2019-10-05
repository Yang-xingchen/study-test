package spring.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import spring.mybatis.model.Commodity;
import spring.mybatis.model.Order;

import java.util.List;

@Mapper
@Repository
public interface CommodityMapper {

    @Select({
            "SELECT *",
            "FROM t_commodity",
            "WHERE id=#{id}"
    })
    @Results(id="commodityMap", value = {
            @Result(id=true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "id", property = "orders",
                many = @Many(select = "findOrderByCommodity", fetchType = FetchType.LAZY))
    })
    public Commodity find(long id);

    @Select({
            "SELECT *",
            "FROM t_commodity"
    })
    @ResultMap("commodityMap")
    public List<Commodity> findAll();


    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE",
            "id in (SELECT t_order FROM t_order_commodity WHERE t_commodity=#{cid})"
    })
    @ResultMap("spring.mybatis.mapper.OrderMapper.orderMap")
    public List<Order> findOrderByCommodity(long cid);
}
