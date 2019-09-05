package com.wwc.community.mapper;

import com.wwc.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (account_id,name,token,gmt_create,gmt_modified) VALUES (#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified})")
    void insert(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);
}
