package korme.xyz.education.mapper;

import korme.xyz.education.model.receiverModel.NoticeModel;
import korme.xyz.education.model.receiverModel.NoticeWholeModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "NoticeMapper")
public interface NoticeMapper {
    /*插入通知信息
    * */
    @Insert("INSERT INTO `education`.`notice`(`noticeTitle`,`content`,`kidgardenId`, `createTime`, `creatorId`,`isBack`) VALUES ( #{n.noticeTitle},#{n.content},#{n.kidgardenId}, NOW(),#{n.userId},0)")
    void insertNotice(@Param("n") NoticeModel n);
    //返回通知信息
    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN `user` u on t.creatorId=u.userId where t.kidgardenId='1' and t.isBack=0 and t.delState=0) s  ORDER BY s.noticeId desc")
    List<NoticeWholeModel> selectNotice(@Param("kidgardenId")int kidgardenId);
    //选择在某时间前的、某幼儿园的 通知信息（普通用户使用）
    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN `user` u on t.creatorId=u.userId where t.kidgardenId='1' and t.isBack=0 and t.delState=0) s where s.noticeId<#{lastNoticeId} ORDER BY s.noticeId desc ")
    List<NoticeWholeModel> selectNoticeBefore(@Param("kidgardenId")int kidgardenId, @Param("lastNoticeId")int lastNoticeId);
    //通知计数（普通用户）
    @Select("SELECT COUNT(*) from notice where (kidgardenId=#{kidgardenId} or isBack=1) and createTime>#{activeTime}")
    int countNotice(@Param("kidgardenId")int kidgardenId,@Param("activeTime")String activeTime);
    //返回全部通知（管理员）
    @Select("select * from (SELECT t.creatorId as userId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from notice t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from notice t LEFT JOIN `user` u on t.creatorId=u.userId where t.isBack=0 and t.delState=0) s  ORDER BY s.noticeId desc")
    List<NoticeWholeModel> selectAllNotice();
    //选择在某时间前的全部通知（管理员）
    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from notice t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from notice t LEFT JOIN `user` u on t.creatorId=u.userId where t.isBack=0 and t.delState=0) s where s.noticeId<#{lastNoticeId} ORDER BY s.noticeId desc  ")
    List<NoticeWholeModel> selectAllNoticeBefore(@Param("lastNoticeId")int lastNoticeId);
    //通知计数（管理员）
    @Select("SELECT COUNT(*) from notice where createTime>#{activeTime}")
    int countAllNotice(@Param("activeTime")String activeTime);
    //假删除通知
    @Update("update notice set delState=1,delTime=NOW() where noticeId=#{noticeId} and creatorId=#{creatorId}")
    int delMyNotice(@Param("noticeId")int noticeId,@Param("creatorId")int creatorId);
    //没用
    @Update("update notice set delState=1,delTime=NOW() where noticeId=#{noticeId}")
    int delRandomNotice(@Param("noticeId")int noticeId);


}
