package korme.xyz.education.controller;

import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.CommentMapper;
import korme.xyz.education.mapper.CommentReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class CommentController {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentReplyMapper commentReplyMapper;
    @Value("${pagehelp-size}")
    int pagesize;
    @RequestMapping(value = "/comment")
    public ResponseEntity findComment(@SessionAttribute("userId")int userId,
                                      @NotNull @Size(min = 1,max = 4) Integer type,
                                      @NotNull Integer id,
                                      @NotNull Integer page){
        List<Map<String,Object>> result=null;
        switch (type){
            case 1://video
                PageHelper.startPage(page,pagesize);
                result=commentMapper.selectVideoComment(id);
                break;
            case 2://article
                PageHelper.startPage(page,pagesize);
                result=commentMapper.selectArticleComment(id);
                break;
            case 3://dynamic
                PageHelper.startPage(page,pagesize);
                result=commentMapper.selectDynamicComment(id);
                break;
            case 4://teacher
                PageHelper.startPage(page,pagesize);
                result=commentMapper.selectTeacherComment(id);
                break;
        }
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    @RequestMapping(value = "/commentReply")
    public ResponseEntity findCommentReply(@SessionAttribute("userId")int userId,
                                           @NotNull @Size(min = 1,max = 3) Integer type,
                                           @NotNull Integer id,
                                           @NotNull Integer page){
        List<Map<String,Object>> result=null;
        switch (type){
            case 1://video
                PageHelper.startPage(page,pagesize);
                result=commentReplyMapper.selectVideoCommentReply(id);
                break;
            case 2://article
                PageHelper.startPage(page,pagesize);
                result=commentReplyMapper.selectArticleCommentReply(id);
                break;
            case 3://dynamic
                PageHelper.startPage(page,pagesize);
                result=commentReplyMapper.selectDynamicCommentReply(id);
                break;
        }
        return new ResponseEntity(RespCode.SUCCESS,result);

    }
}
