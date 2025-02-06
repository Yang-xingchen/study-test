package com.example.annotation;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.springframework.stereotype.Component;

@Mapper
@Component("annotationsUserMapper")
public interface UserMapper {

    @Insert("INSERT INTO t_user(`u_name`, `u_gender`) VALUES (#{user.name}, #{user.gender,javaType=com.example.annotation.Gender,typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(@Param("user") User user);

    @Delete("DELETE FROM t_user WHERE u_id=#{id}")
    int delete(@Param("id") Long uid);

    @Update("UPDATE t_user SET u_name=#{name} WHERE u_id=#{id}")
    int update(@Param("id") Long uid, @Param("name") String name);

    @Select("SELECT u_id as id, u_name as name, u_gender as gender FROM t_user WHERE u_id=#{id}")
    @Results(@Result(javaType = Gender.class, typeHandler = EnumOrdinalTypeHandler.class, column = "gender", property = "gender"))
    User find(@Param("id") Long uid);

}
