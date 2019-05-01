package korme.xyz.education.mapper;

import korme.xyz.education.model.ScoreModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "TeacherScoreMapper")
public interface TeacherScoreMapper {
    /*
    *查询某条评分是否存在
    * */
    @Select(" select 1 from teacher_score where teacherId=#{teacherId} and userId=#{userId} and DATE_FORMAT( createTime , '%Y%m' ) = DATE_FORMAT( NOW() , '%Y%m' ) limit 1")
    int teacherScoreExist(@Param("teacherId")int teacherId,@Param("userId") int userId);
    /*
    * 插入评分
    * */
    @Insert("INSERT INTO `education`.`teacher_score`(`teacherId`, `userId`, `score`, `createTime`) VALUES (#{teacherId}, #{userId}, #{score}, NOW())")
    void insertTeacherScore(@Param("teacherId")int teacherId,@Param("userId")int userId,@Param("score")int score);
    /*
    * 更新教师的分数
    * */
    @Update("update `user` set score=50+(select IFNULL(AVG(t.score),0) from teacher_score t where t.teacherId=#{teacherId}) where user.userId=#{teacherId}")
    void updateScore(@Param("teacherId")int teacherId);

    /*
     *查询某家长班内老师
     * */
    @Select("SELECT u.userId,u.nickName,u.headPortrait,u.score from user as u,(select u2.classId,u2.kidgardenId from user as u2 where userId=#{userId}) as a where u.classId=a.classId and u.kidgardenId=a.kidgardenId and userType=2 order by u.score DESC")
    List<ScoreModel> selectClassTeacher(@Param("userId")int userId);

    /*
     *查询某家长校内老师
     * */
    @Select("SELECT u.userId,u.nickName,u.headPortrait,u.score from user as u where kidgardenId=(select kidgardenId where userId=#{userId}) and userType=2 order by u.score DESC")
    List<ScoreModel> selectKidgardenTeacher(@Param("userId")int userId);
}
