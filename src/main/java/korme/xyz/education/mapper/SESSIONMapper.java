package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface SESSIONMapper {
    @Delete("delete from spring_session WHERE PRIMARY_ID=#{PRIMARY_ID};delete from spring_session_attributes WHERE SESSION_PRIMARY_ID=#{PRIMARY_ID}")
    void delSESSION(@Param("PRIMARY_ID")String PRIMARY_ID);

    @Select("SELECT PRIMARY_ID from spring_session WHERE SESSION_ID=#{SESSION_ID}")
    String selectSESSION(@Param("SESSION_ID")String SESSION_ID);
}
