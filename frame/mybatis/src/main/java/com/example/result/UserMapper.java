package com.example.result;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("resultUserMapper")
public interface UserMapper {

    int saveUser(@Param("user") User user);

    int saveRole(@Param("role") Role role);

    int savePermissions(@Param("permissions") Permissions permissions);

    int saveRolePermissions(@Param("role") Long rid, @Param("pids") List<Long> pids);

    User.Man findUser(@Param("uid") Long uid);

}
