package korme.xyz.education.mapper;

import korme.xyz.education.model.CommentWholeModel;
import korme.xyz.education.model.receiverModel.CommentModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "CommentMapper")
public interface CommentMapper {

    /*
    * 返回全部评论List  文章 视频 动态 老师
    * */
    @Select("SELECT c.articleCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from article_comment as c LEFT JOIN user as u on c.userId= u.userId where c.articleId=#{articleId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectArticleComment(@Param("articleId")int articleId);//查询字段自表命名c左链接u表关联条件cid=uid当   按创建时间倒序排序
    @Select("SELECT c.videoCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from video_comment as c LEFT JOIN user as u on c.userId= u.userId where c.videoId=#{videoId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectVideoComment(@Param("videoId")int videoId);
    @Select("SELECT c.dynamicCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from dynamic_comment as c LEFT JOIN user as u on c.userId= u.userId where c.dynamicId=#{dynamicId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectDynamicComment(@Param("dynamicId")int dynamicId);
    @Select("SELECT c.teacherCommentId,c.content,c.createTime,u.userId,u.nickName,u.userType,u.headPortrait from teacher_comment as c LEFT JOIN user as u on c.userId= u.userId where c.teacherId=#{teacherId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectTeacherComment(@Param("teacherId")int teacherId);

    /*
    * 返回在某Id之前的评论List
    * */
    @Select("SELECT c.articleCommentId,c.articleId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from article_comment as c LEFT JOIN user as u on c.userId= u.userId where c.articleId=#{articleId} and c.articleCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectArticleCommentBefore(@Param("articleId")int articleId,@Param("id")int lastId);
    @Select("SELECT c.videoCommentId,c.videoId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from video_comment as c LEFT JOIN user as u on c.userId= u.userId where c.videoId=#{videoId} and c.videoCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectVideoCommentBefore(@Param("videoId")int videoId,@Param("id")int lastId);
    @Select("SELECT c.dynamicCommentId,c.DynamicId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from dynamic_comment as c LEFT JOIN user as u on c.userId= u.userId where c.dynamicId=#{dynamicId} and c.dynamicCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectDynamicCommentBefore(@Param("dynamicId")int dynamicId,@Param("id")int lastId);
    @Select("SELECT c.teacherCommentId,c.content,c.createTime,u.userId,u.nickName,u.userType,u.headPortrait from teacher_comment as c LEFT JOIN user as u on c.userId= u.userId where c.teacherId=#{teacherId} and c.teacherCommentId<#{id} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectTeacherCommentBefore(@Param("teacherId")int teacherId,@Param("id")int lastId);

    /*
    * 插入评论
    * */
    @Insert("INSERT INTO `education`.`video_comment`(`videoId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertVideoComment(@Param("c") CommentModel c);
    @Insert("INSERT INTO `education`.`article_comment`(`articleId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertArticleComment(@Param("c")CommentModel c);
    @Insert("INSERT INTO `education`.`dynamic_comment`(`dynamicId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertDynamicComment(@Param("c")CommentModel c);
    @Insert("INSERT INTO `education`.`teacher_comment`(`teacherId`, `userId`, `content`, `createTime`) VALUES ( #{c.pId}, #{c.userId}, #{c.content}, NOW())")
    void insertTeacherComment(@Param("c")CommentModel c);
    //评论数自增1
    @Update("update mainpage set commentNum=commentNum+1 where videoOrArticleId=#{videoOrArticleId} and type=1")
    void addVideoCommentNum(@Param("videoOrArticleId")int videoOrArticleId);
    @Update("update mainpage set commentNum=commentNum+1 where videoOrArticleId=#{videoOrArticleId} and type=2")
    void addArticleCommentNum(@Param("videoOrArticleId")int videoOrArticleId);
    @Update("update dynamic set commentNum=commentNum+1 where dynamicId=#{dynamicId}")
    void addDynamicCommentNum(@Param("dynamicId")int dynamicId);
    //删除评论
    @Update("update video_comment set delState=1 , delTime=NOW() where videoCommentId=#{videoCommentId}")
    void delVideoComment(@Param("videoCommentId")int videoCommentId);
    @Update("update article_comment set delState=1 , delTime=NOW() where articleCommentId=#{articleCommentId}")
    void delArticleComment(@Param("articleCommentId")int articleCommentId);
    @Update("update dynamic_comment set delState=1, delTime=NOW() where dynamicCommentId=#{dynamicCommentId}")
    void delDynamicComment(@Param("dynamicCommentId")int dynamicCommentId);
    @Update("update teacher_comment set delState=1, delTime=NOW() where teacherCommentId=#{teacherCommentId}")
    void delTeacherComment(@Param("teacherCommentId")int teacherCommentId);

    //评论数减一
    @Update("update mainpage set commentNum=commentNum-1 where videoOrArticleId=#{videoId} and type=1 and commentNum>0")
    void decVideoCommentNum(@Param("videoId")int videoId);
    @Update("update mainpage set commentNum=commentNum-1 where videoOrArticleId=#{articleId} and type=2  and commentNum>0")
    void decArticleCommentNum(@Param("articleId")int articleId);
    @Update("update dynamic set commentNum=commentNum-1 where dynamicId=#{dynamicId} and commentNum>0")
    void decDynamicCommentNum(@Param("dynamicId")int dynamicId);



    @Update("UPDATE video_comment set haschild=#{haschild} where videoCommentId=#{videoCommentId}")
    void updateVideoCommentHasChild(@Param("haschild")int haschild,@Param("videoCommentId")int videoCommentId);
    @Update("UPDATE article_comment set haschild=#{haschild} where articleCommentId=#{articleCommentId}")
    void updateArticleCommentHasChild(@Param("haschild")int haschild,@Param("articleCommentId")int articleCommentId);
    @Update("UPDATE dynamic_comment set haschild=#{haschild} where dynamicCommentId=#{dynamicCommentId}")
    void updateDynamicCommentHasChild(@Param("haschild")int haschild,@Param("dynamicCommentId")int dynamicCommentId);

    /*
    * 返回对教师的评论
    * */
    @Select("SELECT content from teacher_comment where userId=#{userId} and teacherId=#{teacherId} limit 1")
    String selectCommentToTeacher(@Param("userId")int userId,@Param("teacherId")int teacherId);


    /*
    *返回某条评论
    * */
    @Select("SELECT c.dynamicId as pId,c.content,c.userId,c.createTime,u.nickName,u.headPortrait,u.userType from dynamic_comment c LEFT JOIN user u on c.userId=u.userId where c.dynamicCommentId=#{dynamicCommentId} and c.delState=0")
    CommentWholeModel selectSingleDynamicComment(@Param("dynamicCommentId")int dynamicCommentId);
    @Select("SELECT c.videoId as pId,c.content,c.userId,c.createTime,u.nickName,u.headPortrait,u.userType from video_comment c LEFT JOIN user u on c.userId=u.userId where c.videoCommentId=#{videoCommentId} and c.delState=0")
    CommentWholeModel selectSingleVideoComment(@Param("videoCommentId")int videoCommentId);
    @Select("SELECT c.articleId as pId,c.content,c.userId,c.createTime,u.nickName,u.headPortrait,u.userType from article_comment c LEFT JOIN user u on c.userId=u.userId where c.articleCommentId=#{articleCommentId} and c.delState=0")
    CommentWholeModel selectSingleArticleComment(@Param("articleCommentId")int articleCommentId);

    @Select("select userId from video_comment WHERE videoCommentId=#{videoCommentId}")
    Integer selectUserIdByVideoCommentId(@Param("videoCommentId")int videoCommentId);
    @Select("select userId from article_comment WHERE articleCommentId=#{articleCommentId}")
    Integer selectUserIdByArticleCommentId(@Param("articleCommentId")int articleCommentId);
    @Select("select userId from dynamic_comment WHERE dynamicCommentId=#{dynamicCommentId}")
    Integer selectUserIdByDynamicCommentId(@Param("dynamicCommentId")int dynamicCommentId);
}
