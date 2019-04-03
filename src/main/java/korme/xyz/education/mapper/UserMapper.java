package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component(value = "UserMapper")
public interface UserMapper {
    @Select("select passWord,userId from user where userName=#{userName}")
    Map<String,Object> findPassword(@Param("userName")String userName);
}
