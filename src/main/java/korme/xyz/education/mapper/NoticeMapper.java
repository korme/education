package korme.xyz.education.mapper;

import korme.xyz.education.model.receiverModel.NoticeModel;
import korme.xyz.education.model.receiverModel.NoticeWholeModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "NoticeMapper")
public interface NoticeMapper {
    @Insert("INSERT INTO `education`.`notice`(`noticeTitle`,`content`,`kidgardenId`, `createTime`, `creatorId`,`isBack`) VALUES ( #{n.noticeTitle},#{n.content},#{n.kidgardenId}, NOW(),#{n.creatorId},0)")
    void insertNotice(@Param("n") NoticeModel n);
    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN `user` u on t.creatorId=u.userId where t.kidgardenId='1' and t.isBack=0 and t.delState=0) s  ORDER BY s.noticeId desc")
    List<NoticeWholeModel> selectNotice(@Param("kidgardenId")int kidgardenId);

    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from (select * from notice where kidgardenId=#{kidgardenId} or isBack=1) t LEFT JOIN `user` u on t.creatorId=u.userId where t.kidgardenId='1' and t.isBack=0 and t.delState=0) s where s.noticeId<#{lastNoticeId} ORDER BY s.noticeId desc ")
    List<NoticeWholeModel> selectNoticeBefore(@Param("kidgardenId")int kidgardenId, @Param("lastNoticeId")int lastNoticeId);

    @Select("SELECT COUNT(*) from notice where kidgardenId=#{kidgardenId} or isBack=1 and createTime>#{activeTime}")
    int countNotice(@Param("kidgardenId")int kidgardenId,@Param("activeTime")String activeTime);

    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from notice t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from notice t LEFT JOIN `user` u on t.creatorId=u.userId where t.isBack=0 and t.delState=0) s  ORDER BY s.noticeId desc")
    List<NoticeWholeModel> selectAllNotice();

    @Select("select * from (SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,a.nickName,a.headPortrait,a.id as userId from notice t LEFT JOIN administrator a on t.creatorId=a.id WHERE isBack=1 and t.delState=0  UNION SELECT t.creatorId,t.noticeId,t.noticeTitle,t.content,t.createTime,u.nickName,u.headPortrait,u.userId from notice t LEFT JOIN `user` u on t.creatorId=u.userId where t.isBack=0 and t.delState=0) s where s.noticeId<#{lastNoticeId} ORDER BY s.noticeId desc  ")
    List<NoticeWholeModel> selectAllNoticeBefore(@Param("lastNoticeId")int lastNoticeId);

    @Select("SELECT COUNT(*) from notice where createTime>#{activeTime}")
    int countAllNotice(@Param("activeTime")String activeTime);


}
