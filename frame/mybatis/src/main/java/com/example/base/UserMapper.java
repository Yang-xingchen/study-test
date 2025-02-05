package com.example.base;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component("baseUserMapper")
public interface UserMapper {

    int save(@Param("user") User user);

    int delete(@Param("uid") Long uid);

    int update(@Param("uid") Long uid, @Param("name") String name);

    User find(@Param("uid") Long uid);

}
