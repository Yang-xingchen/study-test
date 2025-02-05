package com.example.annotation;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component("annotationsUserMapper")
public interface UserMapper {

    @Insert("INSERT INTO t_user " +
            "    (`uname`) " +
            "VALUES " +
            "    (#{user.uname})")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    int save(@Param("user") User user);

    @Delete("DELETE FROM t_user WHERE uid=#{uid}")
    int delete(@Param("uid") Long uid);

    @Update("UPDATE t_user SET uname=#{name} WHERE uid=#{uid}")
    int update(@Param("uid") Long uid, @Param("name") String name);

    @Select("SELECT uid, uname FROM t_user WHERE uid=#{uid}")
    User find(@Param("uid") Long uid);

}
