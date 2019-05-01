package korme.xyz.education.mapper;

import korme.xyz.education.model.receiverModel.NoticeModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "NoticeMapper")
public interface NoticeMapper {
    @Insert("INSERT INTO `education`.`notice`(`noticeTitle`,`content`,`kidgardenId`, `createTime`, `creatorId`) VALUES ( #{n.noticeTitle},#{n.content},#{n.kidgardenId}, NOW(),#{n.creatorId})")
    void insertNotice(NoticeModel n);
    //todo:改sql参数注入，改Model层的参数，加个receivedModel
    @Select("SELECT n.noticeTitle,n.content,n.kidgardenId,n.creatorId as userId,u.headPortrait,u.nickName FROM notice as n LEFT JOIN user as u on n.creatorId=u.userId where  n.kidgardenId in (0,#{kidgardenId}) and n.delState=0 and n.createTime>#{lastTime} ORDER BY n.createTime desc")
    NoticeModel selectNotice(@Param("kidgardenId")int kidgardenId,@Param("lastTime")String lastTime);
}
