package korme.xyz.education.mapper;

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

}
