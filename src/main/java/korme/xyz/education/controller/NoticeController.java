package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.NoticeMapper;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.UserTypeModel;
import korme.xyz.education.model.receiverModel.NoticeModel;
import korme.xyz.education.model.receiverModel.NoticeWholeModel;
import korme.xyz.education.service.timeUtil.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TimeUtils timeUtils;
    @RequestMapping("countNotice")
    public ResponseEntity countNotice(@SessionAttribute("userId") Integer userId){
        //userName,nickName,kidgardenId,classId,userType,lastActiveTime
        UserTypeModel userType= userMapper.findUserTypeClea(userId);
        int result;
        if((int)userType.getUserType()==4)
            result=noticeMapper.countAllNotice((String)userType.getLastActiveTime());
        else
            result=noticeMapper.countNotice((int)userType.getKidgardenId(),(String)userType.getLastActiveTime());
        return new ResponseEntity(RespCode.SUCCESS,result);
    }

    @RequestMapping("getNotice")
    public ResponseEntity getNotice(@SessionAttribute("userId") Integer userId,
                                      @NotNull Integer lastId){
        //userName,nickName,kidgardenId,classId,userType,lastActiveTime
        Map<String,Object> userType= userMapper.findUserType(userId);
        List<NoticeWholeModel> result;
        if(lastId==0){
            if((int)userType.get("userType")==4){
                result=noticeMapper.selectAllNotice();
                userMapper.updateLastActiveTime(userId);
            }
            else{
                result=noticeMapper.selectNotice((int)userType.get("kidgardenId"));
                userMapper.updateLastActiveTime(userId);
            }
        }
        else{
            if((int)userType.get("userType")==4)
                result=noticeMapper.selectAllNoticeBefore(lastId);
            else
                result=noticeMapper.selectNoticeBefore((int)userType.get("kidgardenId"),lastId);
        }

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    @Transactional
    @RequestMapping("addNotice")
    public ResponseEntity addNotice(@SessionAttribute("userId") Integer userId,
                                      @Validated NoticeModel notice){
        //userName,nickName,kidgardenId,classId,userType,lastActiveTime
        UserTypeModel userType= userMapper.findUserTypeClea(userId);
        if(userType.getUserType()==3){
            notice.setKidgardenId(userType.getKidgardenId());
            notice.setUserId(userId);
            noticeMapper.insertNotice(notice);
            return new ResponseEntity(RespCode.SUCCESS);
        }
        else
            return new ResponseEntity(RespCode.ERROR_USER);


    }



}
