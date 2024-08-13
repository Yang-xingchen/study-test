package com.example.sharding.mapper;

import com.example.sharding.model.TestModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ComplexMapper {

    @Insert("INSERT t_complex (id, uid, gid) VALUES (#{param.id}, #{param.uid}, #{param.gid})")
    void insert(@Param("param") TestModel testModel);

    @Select("SELECT * FROM t_complex WHERE uid=#{uid}")
    List<TestModel> getByUser(@Param("uid") String uid);

    @Select("SELECT * FROM t_complex WHERE gid=#{gid}")
    List<TestModel> getByGood(@Param("gid") String gid);

    @Select("SELECT * FROM t_complex WHERE uid=#{uid} and gid=#{gid}")
    List<TestModel> get(@Param("uid") String uid, @Param("gid") String gid);
}
