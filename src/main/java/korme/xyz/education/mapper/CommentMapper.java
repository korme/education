package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "CommentMapper")
public interface CommentMapper {


    @Select("SELECT c.articleCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from article_comment as c LEFT JOIN user as u on c.userId= u.userId where c.articleId=#{articleId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectArticleComment(@Param("articleId")int articleId);

    @Select("SELECT c.videoCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from video_comment as c LEFT JOIN user as u on c.userId= u.userId where c.videoCommentId=#{videoCommentId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectVideoComment(@Param("videoCommentId")int videoCommentId);

    @Select("SELECT c.dynamicCommentId,c.content,c.createTime,c.haschild,u.userId,u.nickName,u.userType,u.headPortrait from dynamic_comment as c LEFT JOIN user as u on c.userId= u.userId where c.dynamicId=#{dynamicId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectDynamicComment(@Param("dynamicId")int dynamicId);

    @Select("SELECT c.teacherCommentId,c.content,c.createTime,u.userId,u.nickName,u.userType,u.headPortrait from teacher_comment as c LEFT JOIN user as u on c.userId= u.userId where c.teacherId=#{teacherId} and u.delState=0 and c.delState=0 ORDER BY c.createTime DESC")
    List<Map<String,Object>> selectTeacherComment(@Param("teacherId")int teacherId);



    /*int video=1;
    int article=2;
    int dynamic=3;
    int teacherScore=4;

    class CommentProvider{
        private int teacher=1;
        private int parent=2;
        private int principal=3;
        private int official=4;
        public String selectComment(int type,){
            SQL sql=new SQL();

            switch (type){
                case video:
                    sql.SELECT("*");
                    sql.FROM("video_comment");
                    break;
                case article:
                    sql.FROM("article_comment");
                    break;
                case dynamic:
                    sql.FROM("dynamic_comment");
                    break;
                case teacherScore:
                    sql.FROM("teacher_comment");
                    break;
            }


        }

    }*/
}
