package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "FeedbackMapper")
public interface FeedbackMapper {
    /*
    * 插入用户反馈
    * */
    @Insert("INSERT INTO `education`.`feedback`( `feedbackContent`, `createTime`, `creatorId`) VALUES (#{feedbackContent}, NOW(),#{userId})")
    void insertFeedback(@Param("feedbackContent")String feedbackContent,@Param("userId")int userId);
}
