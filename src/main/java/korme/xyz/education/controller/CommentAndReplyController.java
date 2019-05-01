package korme.xyz.education.controller;

import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.CommentMapper;
import korme.xyz.education.mapper.CommentReplyMapper;
import korme.xyz.education.model.receiverModel.CommentModel;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class CommentAndReplyController {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentReplyMapper commentReplyMapper;
    @Autowired
    ALiYunOssUtil oss;
    @Value("${pagehelp-size}")
    int pagesize;
    @RequestMapping(value = "allComment")
    public ResponseEntity findComment(@SessionAttribute("userId")int userId,
                                      @NotNull Integer type,
                                      @NotNull Integer id,
                                      @NotNull Integer lastId){
        List<Map<String,Object>> result=null;
        if(lastId==0){
            switch (type){
                case 1://video
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectVideoComment(id);
                    break;
                case 2://article
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectArticleComment(id);
                    break;
                case 3://dynamic
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectDynamicComment(id);
                    break;
                case 4://teacher
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectTeacherComment(id);
                    break;
                default:
                    return new ResponseEntity(RespCode.ERROR_INPUT);
            }
        }
        else{
            switch (type){
                case 1://video
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectVideoCommentBefore(id,lastId);
                    break;
                case 2://article
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectArticleCommentBefore(id,lastId);
                    break;
                case 3://dynamic
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectDynamicCommentBefore(id,lastId);
                    break;
                case 4://teacher
                    PageHelper.startPage(1,pagesize);
                    result=commentMapper.selectTeacherCommentBefore(id,lastId);
                    break;
                default:
                    return new ResponseEntity(RespCode.ERROR_INPUT);
        }

        }
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    @RequestMapping(value = "allCommentReply")
    public ResponseEntity findCommentReply(@SessionAttribute("userId")int userId,
                                           @NotNull Integer type,
                                           @NotNull Integer id,
                                           @NotNull Integer lastId){
        List<Map<String,Object>> result=null;
        if(lastId==0){
            switch (type){
                case 1://video
                    PageHelper.startPage(1,pagesize);
                    result=commentReplyMapper.selectVideoCommentReply(id);
                    break;
                case 2://article
                    PageHelper.startPage(1,pagesize);
                    result=commentReplyMapper.selectArticleCommentReply(id);
                    break;
                case 3://dynamic
                    PageHelper.startPage(1,pagesize);
                    result=commentReplyMapper.selectDynamicCommentReply(id);
                    break;
                    default:
                        return new ResponseEntity(RespCode.ERROR_INPUT);
            }
            return new ResponseEntity(RespCode.SUCCESS,result);
        }
        else{
            switch (type){
                case 1://video
                    PageHelper.startPage(1,pagesize);
                    result=commentReplyMapper.selectVideoCommentReplyBefore(id,lastId);
                    break;
                case 2://article
                    PageHelper.startPage(1,pagesize);
                    result=commentReplyMapper.selectArticleCommentReplyBefore(id,lastId);
                    break;
                case 3://dynamic
                    PageHelper.startPage(1,pagesize);
                    result=commentReplyMapper.selectDynamicCommentReplyBefore(id,lastId);
                    break;
                    default:
                        return new ResponseEntity(RespCode.ERROR_INPUT);
            }
            return new ResponseEntity(RespCode.SUCCESS,result);
        }
    }
    @RequestMapping(value = "addComment")
    public ResponseEntity addComment(@SessionAttribute("userId")int userId,
                                     @Validated CommentModel comment,
                                     @NotNull Integer type) {
        try {
            comment.setUserId(userId);
            List<String> tempString=new ArrayList<>();
            tempString.add(comment.getContent());
            tempString=oss.textScan(tempString);
            comment.setContent(tempString.get(0));
            switch (type) {
                case 1://视频
                    commentMapper.insertVideoComment(comment);
                    commentMapper.addVideoCommentNum(comment.getpId());
                    break;
                case 2://文章
                    commentMapper.insertArticleComment(comment);
                    commentMapper.addArticleCommentNum(comment.getpId());
                    break;
                case 3://动态
                    commentMapper.insertDynamicComment(comment);
                    commentMapper.addDynamicCommentNum(comment.getpId());
                    break;
                case 4://老师
                    commentMapper.insertTeacherComment(comment);
                    break;
                    default:
                        return new ResponseEntity(RespCode.ERROR_INPUT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }
    @RequestMapping(value = "addCommentReply")
    public ResponseEntity addCommentReply(@SessionAttribute("userId")int userId,
                                     @Validated CommentModel commentReply,
                                     @NotNull Integer type) {
        try {
            commentReply.setUserId(userId);
            List<String> tempString=new ArrayList<>();
            tempString.add(commentReply.getContent());
            tempString=oss.textScan(tempString);
            commentReply.setContent(tempString.get(0));
            switch (type) {
                case 1://视频
                    commentMapper.insertVideoComment(commentReply);
                    commentMapper.updateVideoCommentHasChild(1,commentReply.getpId());
                    break;
                case 2://文章
                    commentMapper.insertArticleComment(commentReply);
                    commentMapper.updateArticleCommentHasChild(1,commentReply.getpId());
                    break;
                case 3://动态
                    commentMapper.insertDynamicComment(commentReply);
                    commentMapper.updateDynamicCommentHasChild(1,commentReply.getpId());
                    break;
                default:
                    return new ResponseEntity(RespCode.ERROR_INPUT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }
}
