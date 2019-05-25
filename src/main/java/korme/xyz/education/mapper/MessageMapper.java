package korme.xyz.education.mapper;

import korme.xyz.education.model.MessageModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "MessageMapper")
public interface MessageMapper {
    /*
    * message计数
    * */
    @Select("select count(*) FROM message where userId=#{userId} and isRead=0")
    int countMessage(@Param("userId")int userId);
    /*
    * 返回所有message
    * */
    @Select("SELECT m.messageId,m.userId,m.commentId,m.content,m.messageType,u.nickName,u.headPortrait ,u.userType from message m LEFT JOIN `user` u on m.replyUserId=u.userId where m.userId=#{userId} order by m.messageId desc")
    List<MessageModel> selectMessage(@Param("userId")int userId);
    /*
     * 返回messageId在lastId前的message
     * */
    @Select("SELECT m.messageId,m.userId,m.commentId,m.content,u.nickName,u.headPortrait ,u.userType,m.isRead from message m LEFT JOIN `user` u on m.replyUserId=u.userId where m.userId=#{userId} and m.messageId<#{lastId} order by m.messageId desc")
    List<MessageModel> selectMessageBefore(@Param("userId")int userId,@Param("lastId")int lastId);
    /*
    * 更新isread
    * */
    @Update("update message set isRead=1 where userId=#{userId}")
    void updateIsread(@Param("userId")int userId);
    /*
    * 插入message
    * */
    @Insert("INSERT INTO `education`.`message`(`userId`,`replyUserId`, `messageType`, `commentId`,`content`) VALUES (#{userId},#{replyUserId},#{messageType},#{commentId},#{content})")
    void insertMessage(@Param("userId")int userId,@Param("replyUserId")int replyUserId,@Param("messageType")int messageType,@Param("commentId")int commentId,@Param("content")String content);

}
