package korme.xyz.education.mapper;

import korme.xyz.education.model.receiverModel.CommentModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "CommentReplyMapper")
public interface CommentReplyMapper {
    @Select("SELECT r.videoCommentReplyId,r.content,r.createTime,ur.nickName,u.headPortrait,u.nickName,u.userId,u.userType from video_comment_reply as r LEFT JOIN user as u ON r.userId=u.userId LEFT JOIN user as ur ON r.replyedUserId = ur.userId where r.videoCommentId=#{videoCommentId} and r.delState=0 and u.delState=0 ORDER BY r.createTime DESC")
    List<Map<String,Object>> selectVideoCommentReply(@Param("videoCommentId")int videoCommentId);

    @Select("SELECT r.articleCommentReplyId,r.content,r.createTime,ur.nickName,u.headPortrait,u.nickName,u.userId,u.userType from article_comment_reply as r LEFT JOIN user as u ON r.userId=u.userId LEFT JOIN user as ur ON r.replyedUserId = ur.userId where r.articleCommentId=#{articleCommentId} and r.delState=0 and u.delState=0 ORDER BY r.createTime DESC")
    List<Map<String,Object>> selectArticleCommentReply(@Param("articleCommentId")int articleCommentId);

    @Select("SELECT r.dynamicCommentReplyId,r.content,r.createTime,ur.nickName,u.headPortrait,u.nickName,u.userId,u.userType from dynamic_comment_reply as r LEFT JOIN user as u ON r.userId=u.userId LEFT JOIN user as ur ON r.replyedUserId = ur.userId where r.dynamicCommentId=#{dynamicCommentId} and r.delState=0 and u.delState=0 ORDER BY r.createTime DESC")
    List<Map<String,Object>> selectDynamicCommentReply(@Param("dynamicCommentId")int dynamicCommentId);

    @Select("SELECT r.videoCommentReplyId,r.content,r.createTime,ur.nickName,u.headPortrait,u.nickName,u.userId,u.userType from video_comment_reply as r LEFT JOIN user as u ON r.userId=u.userId LEFT JOIN user as ur ON r.replyedUserId = ur.userId where r.videoCommentId=#{videoCommentId} and r.articleCommentReplyId<#{lastId} and r.delState=0 and u.delState=0 ORDER BY r.createTime DESC")
    List<Map<String,Object>> selectVideoCommentReplyBefore(@Param("videoCommentId")int videoCommentId,@Param("lastId")int lastId);

    @Select("SELECT r.articleCommentReplyId,r.content,r.createTime,ur.nickName,u.headPortrait,u.nickName,u.userId,u.userType from article_comment_reply as r LEFT JOIN user as u ON r.userId=u.userId LEFT JOIN user as ur ON r.replyedUserId = ur.userId where r.articleCommentId=#{articleCommentId} and r.videoCommentReplyId<#{lastId} and r.delState=0 and u.delState=0 ORDER BY r.createTime DESC")
    List<Map<String,Object>> selectArticleCommentReplyBefore(@Param("articleCommentId")int articleCommentId,@Param("lastId")int lastId);

    @Select("SELECT r.dynamicCommentReplyId,r.content,r.createTime,ur.nickName,u.headPortrait,u.nickName,u.userId,u.userType from dynamic_comment_reply as r LEFT JOIN user as u ON r.userId=u.userId LEFT JOIN user as ur ON r.replyedUserId = ur.userId where r.dynamicCommentId=#{dynamicCommentId} and r.dynamicCommentReplyId<#{lastId} and r.delState=0 and u.delState=0 ORDER BY r.createTime DESC")
    List<Map<String,Object>> selectDynamicCommentReplyBefore(@Param("dynamicCommentId")int dynamicCommentId,@Param("lastId")int lastId);

    @Insert("INSERT INTO `education`.`video_comment`(`videoId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertVideoComment(@Param("c") CommentModel c);
    @Insert("INSERT INTO `education`.`article_comment`(`articleId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertArticleComment(@Param("c")CommentModel c);
    @Insert("INSERT INTO `education`.`dynamic_comment`(`dynamicId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertDynamicComment(@Param("c")CommentModel c);
    @Insert("INSERT INTO `education`.`teacher_comment`(`teacherId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertTeacherComment(@Param("c")CommentModel c);

    @Select("select COUNT(*) from dynamic_comment_reply where dynamicCommentId=#{dynamicCommentId} limit 1")
    int countDynamicCommentReply(@Param("dynamicCommentId")int dynamicCommentId);
    @Select("select COUNT(*) from article_comment_reply where articleCommentId=#{articleCommentId} limit 1")
    int countArticleCommentReply(@Param("articleCommentId")int articleCommentId);
    @Select("select COUNT(*) from video_comment_reply where videoCommentId=#{videoCommentId} limit 1")
    int countVideoCommentReply(@Param("videoCommentId")int videoCommentId);
}
