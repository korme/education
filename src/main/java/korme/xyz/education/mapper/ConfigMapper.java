package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "ConfigMapper")
public interface ConfigMapper {
    @Update("update user set score=0")
    void resetScore();
    @Delete("DELETE from message where createTime<#{endTime}")
    void deleteMessage(@Param("endTime")String endTime);

}
