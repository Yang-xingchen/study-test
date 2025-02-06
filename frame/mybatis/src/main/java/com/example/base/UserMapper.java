package com.example.base;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component("baseUserMapper")
public interface UserMapper {

    int save(@Param("user") User user);

    int delete(@Param("id") Long uid);

    int update(@Param("id") Long uid, @Param("name") String name);

    User find(@Param("id") Long uid);

}
