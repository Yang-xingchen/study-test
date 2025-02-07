package com.example.plugin;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component("pluginUserMapper")
public interface UserMapper {

    @Insert("INSERT INTO t_user(`u_name`) VALUES (#{user.name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(@Param("user") User user);

    @Select("SELECT u_id as id, u_name as name FROM t_user WHERE u_id=#{id}")
    User find(@Param("id") Long uid);

}
