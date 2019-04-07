package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "DynamicMapper")
public interface DynamicMapper {
    /*
    * 返回家长能看到的班级动态，时间倒序
    * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where d.classId=#{classId} " +
            "and d.delState=0 and u.overdueTime>NOW() order by d.date DESC")
    List<Object> selectLimitParentAll(@Param("classId")Integer classId);

    /*
     * 返回家长能看到的班级动态,时间倒序
     * 条件：动态发布时间小于Date
     * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where d.classId=#{classId} " +
            "and d.date<#{date} and d.delState=0 and u.overdueTime>NOW() order by d.date DESC")
    List<Object> selectLimitParentBeforeTime(@Param("classId")Integer classId,@Param("date")String date);

    /*
     * 返回园长发送的所有动态，时间倒序
     * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.userId=(select userId from user as u2 where u2.kidgardenId=#{kidgardenId} and u2.userType=4 limit 1)" +
            "and d.delState=0 order by d.date DESC;")
    List<Object> selectPrincipalAll(@Param("kidgardenId")Integer kidgardenId);

    /*
     * 返回园长发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.userId=(select userId from user as u2 where u2.kidgardenId=1 and u2.userType=4 limit 1)" +
            " and d.date>#{date} and d.delState=0 order by d.date DESC;")
    List<Object> selectPrincipalBeforeTime(@Param("classId")Integer classId,@Param("date")String date);




}
