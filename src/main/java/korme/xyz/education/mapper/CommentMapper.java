package korme.xyz.education.mapper;

import korme.xyz.education.model.receiverModel.CommentModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "CommentMapper")
public interface CommentMapper {


    @Select("SELECT c.articleCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from article_comment as c LEFT JOIN user as u on c.userId= u.userId where c.articleId=#{articleId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectArticleComment(@Param("articleId")int articleId);

    @Select("SELECT c.videoCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from video_comment as c LEFT JOIN user as u on c.userId= u.userId where c.videoId=#{videoId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectVideoComment(@Param("videoId")int videoId);

    @Select("SELECT c.dynamicCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from dynamic_comment as c LEFT JOIN user as u on c.userId= u.userId where c.dynamicId=#{dynamicId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectDynamicComment(@Param("dynamicId")int dynamicId);

    @Select("SELECT c.teacherCommentId,c.content,c.createTime,u.userId,u.nickName,u.userType,u.headPortrait from teacher_comment as c LEFT JOIN user as u on c.userId= u.userId where c.teacherId=#{teacherId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectTeacherComment(@Param("teacherId")int teacherId);


    @Select("SELECT c.articleCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from article_comment as c LEFT JOIN user as u on c.userId= u.userId where c.articleId=#{articleId} and c.articleCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectArticleCommentBefore(@Param("articleId")int articleId,@Param("id")int lastId);

    @Select("SELECT c.videoCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from video_comment as c LEFT JOIN user as u on c.userId= u.userId where c.videoId=#{videoId} and c.videoCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectVideoCommentBefore(@Param("videoId")int videoId,@Param("id")int lastId);

    @Select("SELECT c.dynamicCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from dynamic_comment as c LEFT JOIN user as u on c.userId= u.userId where c.dynamicId=#{dynamicId} and c.dynamicCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectDynamicCommentBefore(@Param("dynamicId")int dynamicId,@Param("id")int lastId);

    @Select("SELECT c.teacherCommentId,c.content,c.createTime,u.userId,u.nickName,u.userType,u.headPortrait from teacher_comment as c LEFT JOIN user as u on c.userId= u.userId where c.teacherId=#{teacherId} and c.teacherCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectTeacherCommentBefore(@Param("teacherId")int teacherId,@Param("id")int lastId);

    @Insert("INSERT INTO `education`.`video_comment`(`videoId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertVideoComment(@Param("c") CommentModel c);
    @Insert("INSERT INTO `education`.`article_comment`(`articleId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertArticleComment(@Param("c")CommentModel c);
    @Insert("INSERT INTO `education`.`dynamic_comment`(`dynamicId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertDynamicComment(@Param("c")CommentModel c);
    @Insert("INSERT INTO `education`.`teacher_comment`(`teacherId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertTeacherComment(@Param("c")CommentModel c);

    @Update("update video set commentNum=commentNum+1 where videoId=#{videoId}")
    void addVideoCommentNum(@Param("videoId")int videoId);
    @Update("update article set commentNum=commentNum+1 where articleId=#{articleId}")
    void addArticleCommentNum(@Param("articleId")int articleId);
    @Update("update dynamic set commentNum=commentNum+1 where dynamicId=#{dynamicId}")
    void addDynamicCommentNum(@Param("dynamicId")int dynamicId);
    //todo:评论删除
    @Update("update video set commentNum=commentNum-1 where videoId=#{videoId}")
    void decCommentNum(@Param("videoId")int videoId);
    @Update("update article set commentNum=commentNum-1 where articleId=#{articleId}")
    void decArticleIdNum(@Param("articleId")int articleId);
    @Update("update dynamic set commentNum=commentNum-1 where dynamicId=#{dynamicId}")
    void decDynamicIdNum(@Param("dynamicId")int dynamicId);



    @Update("UPDATE video_comment set haschild=#{haschild} where videoCommentId=#{videoCommentId}")
    void updateVideoCommentHasChild(@Param("haschild")int haschild,@Param("videoCommentId")int videoCommentId);
    @Update("UPDATE article_comment set haschild=#{haschild} where articleCommentId=#{articleCommentId}")
    void updateArticleCommentHasChild(@Param("haschild")int haschild,@Param("articleCommentId")int articleCommentId);
    @Update("UPDATE dynamic_comment set haschild=#{haschild} where dynamicCommentId=#{dynamicCommentId}")
    void updateDynamicCommentHasChild(@Param("haschild")int haschild,@Param("dynamicCommentId")int dynamicCommentId);


}
