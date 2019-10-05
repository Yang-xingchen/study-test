package spring.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import spring.mybatis.model.Commodity;
import spring.mybatis.model.Order;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {

    @Select({
            "SELECT *",
            "FROM t_order",
            "WHERE id=#{id}"
    })
    @Results(id = "orderMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user", property = "user",
                    one = @One(
                            select = "spring.mybatis.mapper.UserMapper.findById"
                    )
            ),
            @Result(column = "id", property = "commodities",
                    many = @Many(select = "findCommodityByOrder", fetchType = FetchType.LAZY))
    })
    public Order find(long id);

    @Select({
          "SELECT *",
          "FROM t_commodity",
          "WHERE",
            "id in (SELECT t_commodity FROM t_order_commodity WHERE t_order=#{oid})"
    })
//    @ResultMap("spring.mybatis.mapper.CommodityMapper.commodityMap")
    public List<Commodity> findCommodityByOrder(@Param("oid") long oid);

}
