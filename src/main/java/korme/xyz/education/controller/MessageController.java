package korme.xyz.education.controller;

import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.MessageMapper;
import korme.xyz.education.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageMapper messageMapper;
    @Value("${pagehelp-size}")
    int pageSize;
    @RequestMapping("/getMessage")
    public ResponseEntity getMessage(@SessionAttribute("userId") Integer userId,
                                     @NotNull Integer lastId){
        List<MessageModel> result;
        if(lastId==0){
            PageHelper.startPage(1,pageSize);
            result=messageMapper.selectMessage(userId);
            messageMapper.updateIsread(userId);
        }
        else {
            result=messageMapper.selectMessageBefore(userId,lastId);
        }
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    @RequestMapping("/countMessage")
    public ResponseEntity countMessage(@SessionAttribute("userId") Integer userId){
        return new ResponseEntity(RespCode.SUCCESS,messageMapper.countMessage(userId));
    }
}
