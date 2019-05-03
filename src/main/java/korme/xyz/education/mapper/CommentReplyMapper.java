package korme.xyz.education.mapper;

import korme.xyz.education.model.receiverModel.CommentModel;
import korme.xyz.education.model.receiverModel.CommentReplyModel;
import org.apache.ibatis.annotations.*;
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
    //todo:gaiming
    @Insert("INSERT INTO `education`.`video_comment_reply`( `videoCommentId`, `userId`, `replyedUserId`, `content`, `createTime`) VALUES (#{c.pId}, #{c.userId}, #{c.replyUserId}, #{c.content}, NOW())")
    void insertVideoCommentReply(@Param("c") CommentReplyModel c);
    @Insert("INSERT INTO `education`.`article_comment_reply`( `articleCommentId`, `userId`, `replyedUserId`, `content`, `createTime`) VALUES (#{c.pId}, #{c.userId}, #{c.replyUserId}, #{c.content}, NOW())")
    void insertArticleCommentReply(@Param("c")CommentReplyModel c);
    @Insert("INSERT INTO `education`.`dynamic_comment_reply`( `dynamicCommentId`, `userId`, `replyedUserId`, `content`, `createTime`) VALUES (#{c.pId}, #{c.userId}, #{c.replyUserId}, #{c.content}, NOW())")
    void insertDynamicCommentReply(@Param("c")CommentReplyModel c);

    @Select("select COUNT(*) from dynamic_comment_reply where dynamicCommentId=#{dynamicCommentId} limit 1")
    int countDynamicCommentReply(@Param("dynamicCommentId")int dynamicCommentId);
    @Select("select COUNT(*) from article_comment_reply where articleCommentId=#{articleCommentId} limit 1")
    int countArticleCommentReply(@Param("articleCommentId")int articleCommentId);
    @Select("select COUNT(*) from video_comment_reply where videoCommentId=#{videoCommentId} limit 1")
    int countVideoCommentReply(@Param("videoCommentId")int videoCommentId);

    @Update("update video_comment_reply set delState=1 ,delTime=NOW() where videoCommentReplyId=#{videoCommentReplyId}")
    void delVideoCommentReply(@Param("videoCommentReplyId")int videoCommentReplyId);
    @Update("update article_comment_reply set delState=1 ,delTime=NOW() where articleCommentReplyId=#{articleCommentReplyId}")
    void delArticleCommentReply(@Param("articleCommentReplyId")int articleCommentReplyId);
    @Update("update dynamic_comment_reply set delState=1 ,delTime=NOW() where dynamicCommentReplyId=#{dynamicCommentReplyId}")
    void delDynamicCommentReply(@Param("dynamicCommentReplyId")int dynamicCommentReplyId);

    @Select("select IFNULL((SELECT videoCommentId from video_comment_reply where videoCommentReplyId=#{videoCommentReplyId}),0)")
    int selectVideoCommentIdByReplyId(@Param("videoCommentReplyId")int videoCommentReplyId);
    @Select("select IFNULL((SELECT articleCommentId from video_comment_reply where articleCommentReplyId=#{articleCommentReplyId}),0)")
    int selectArticleCommentIdByReplyId(@Param("articleCommentReplyId")int articleCommentReplyId);
    @Select("select IFNULL((SELECT dynamicCommentId from video_comment_reply where dynamicCommentReplyId=#{dynamicCommentReplyId}),0)")
    int selectDynamicCommentIdByReplyId(@Param("dynamicCommentReplyId")int dynamicCommentReplyId);

    @Select("select userId from video_comment WHERE videoCommentId=#{videoCommentId}")
    int selectUserIdByVideoCommentId(@Param("videoCommentId")int videoCommentId);
    @Select("select userId from article_comment WHERE articleCommentId=#{articleCommentId}")
    int selectUserIdByArticleCommentId(@Param("articleCommentId")int articleCommentId);
    @Select("select userId from dynamic_comment WHERE dynamicCommentId=#{dynamicCommentId}")
    int selectUserIdByDynamicCommentId(@Param("dynamicCommentId")int dynamicCommentId);
}
