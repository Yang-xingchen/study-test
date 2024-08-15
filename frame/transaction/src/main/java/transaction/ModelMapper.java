package transaction;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ModelMapper {

    @Select("SELECT COUNT(*) FROM model WHERE value=#{value}")
    long countByValue(@Param("value") String value);

    @Insert("INSERT model (value) VALUES (#{param.value})")
    void save(@Param("param") Model model);

    @Delete("DELETE FROM model")
    void clear();

}
