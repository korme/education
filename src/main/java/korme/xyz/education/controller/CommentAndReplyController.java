package korme.xyz.education.controller;

import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.CommentMapper;
import korme.xyz.education.mapper.CommentReplyMapper;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.mapper.MessageMapper;
import korme.xyz.education.model.CommentWholeModel;
import korme.xyz.education.model.receiverModel.CommentModel;
import korme.xyz.education.model.receiverModel.CommentReplyModel;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    DynamicMapper dynamicMapper;
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
    @RequestMapping("findComment")
    public ResponseEntity findCommentAndReply(@SessionAttribute("userId")int userId,
                                              @NotNull Integer commentId,
                                              @NotNull Integer type){
        CommentWholeModel comment;
        switch (type){
            case 1:
                comment=commentMapper.selectSingleVideoComment(commentId);
                comment.setType(1);
                break;
            case 2:
                comment=commentMapper.selectSingleArticleComment(commentId);
                comment.setType(2);
                break;
            case 3:
                comment=commentMapper.selectSingleDynamicComment(commentId);
                comment.setType(3);
                break;
            default:
                return new ResponseEntity(RespCode.ERROR_INPUT,"type输入错误");
        }
        if (comment==null)
            return new ResponseEntity(RespCode.WARN_ENPTY,"内容不存在或已删除");

        return new ResponseEntity(RespCode.SUCCESS,comment);

    }
    @Transactional
    @RequestMapping(value = "addComment")
    public ResponseEntity addComment(@SessionAttribute("userId")int userId,
                                     @Validated CommentModel comment,
                                     @NotNull Integer type) {
        try {
            comment.setUserId(userId);
            List<String> tempString=new ArrayList<>();
            tempString.add(comment.getContent());
            try{
                tempString=oss.textScan(tempString);
            }
            catch (Exception e){
                return new ResponseEntity(RespCode.WARN_INPUT);
            }

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
                    int replyedUserId=dynamicMapper.selectUserIdByDynamicId(comment.getpId());
                    if(replyedUserId!=userId)
                        messageMapper.insertMessage(replyedUserId,userId,3,comment.getpId(),comment.getContent());
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
    @Transactional
    @RequestMapping(value = "addReply")
    public ResponseEntity addCommentReply(@SessionAttribute("userId")int userId,
                                     @Validated CommentReplyModel commentReply,
                                     @NotNull Integer type) {
        try {
            commentReply.setUserId(userId);
            List<String> tempString=new ArrayList<>();
            tempString.add(commentReply.getContent());
            try{
                tempString=oss.textScan(tempString);
            }
            catch (Exception e){
                return new ResponseEntity(RespCode.WARN_INPUT);
            }
            commentReply.setContent(tempString.get(0));
            switch (type) {
                case 1://视频
                    commentReplyMapper.insertVideoCommentReply(commentReply);
                    commentMapper.updateVideoCommentHasChild(1,commentReply.getpId());
                    if(commentReply.getReplyUserId()==0)
                        commentReply.setReplyUserId(commentReplyMapper.selectUserIdByVideoCommentId(commentReply.getpId()));
                    if(commentReply.getReplyUserId()!=userId)
                        messageMapper.insertMessage(commentReply.getReplyUserId(),userId,1,commentReply.getpId(),commentReply.getContent());
                    break;
                case 2://文章
                    commentReplyMapper.insertArticleCommentReply(commentReply);
                    commentMapper.updateArticleCommentHasChild(1,commentReply.getpId());
                    if(commentReply.getReplyUserId()==0)
                        commentReply.setReplyUserId(commentReplyMapper.selectUserIdByArticleCommentId(commentReply.getpId()));
                    if(commentReply.getReplyUserId()!=userId)
                        messageMapper.insertMessage(commentReply.getReplyUserId(),userId,2,commentReply.getpId(),commentReply.getContent());
                    break;
                case 3://动态
                    commentReplyMapper.insertDynamicCommentReply(commentReply);
                    commentMapper.updateDynamicCommentHasChild(1,commentReply.getpId());
                    if(commentReply.getReplyUserId()==0)
                        commentReply.setReplyUserId(commentReplyMapper.selectUserIdByDynamicCommentId(commentReply.getpId()));
                    if(commentReply.getReplyUserId()!=userId)
                        messageMapper.insertMessage(commentReply.getReplyUserId(),userId,3,commentReply.getpId(),commentReply.getContent());
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
    /*@RequestMapping("delComment")
    public ResponseEntity delComment(@SessionAttribute("userId")int userId,
                                          @NotNull Integer commentId,
                                          @NotNull Integer type){

        switch (type) {
            case 1://视频
                commentMapper.delVideoComment(commentId);
                commentMapper.decVideoCommentNum(commentId);
                break;
            case 2://文章
                commentMapper.delArticleComment(commentId);
                commentMapper.decArticleCommentNum(commentId);
                break;
            case 3://动态
                commentMapper.delDynamicComment(commentId);
                commentMapper.decDynamicCommentNum(commentId);
                break;
            case 4:
                commentMapper.delTeacherComment(commentId);
            default:
                return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }
    @RequestMapping("delCommentReply")
    public ResponseEntity delCommentReply(@SessionAttribute("userId")int userId,
                                          @NotNull Integer commentReplyId,
                                          @NotNull Integer type){
        int commentId;
        switch (type) {
            case 1://视频
                commentId=commentReplyMapper.selectVideoCommentIdByReplyId(commentReplyId);
                if(commentId==0)
                    return new ResponseEntity(RespCode.ERROR_INPUT);
                commentReplyMapper.delVideoCommentReply(commentReplyId);
                commentMapper.updateVideoCommentHasChild(commentReplyMapper.countVideoCommentReply(commentReplyId),commentReplyId);
                break;
            case 2://文章
                commentId=commentReplyMapper.selectArticleCommentIdByReplyId(commentReplyId);
                if(commentId==0)
                    return new ResponseEntity(RespCode.ERROR_INPUT);
                commentReplyMapper.delArticleCommentReply(commentReplyId);
                commentMapper.updateArticleCommentHasChild(commentReplyMapper.countArticleCommentReply(commentReplyId),commentReplyId);
                break;
            case 3://动态
                commentId=commentReplyMapper.selectDynamicCommentIdByReplyId(commentReplyId);
                if(commentId==0)
                    return new ResponseEntity(RespCode.ERROR_INPUT);
                commentReplyMapper.delDynamicCommentReply(commentReplyId);
                commentMapper.updateDynamicCommentHasChild(commentReplyMapper.countDynamicCommentReply(commentReplyId),commentReplyId);
                break;
            default:
                return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }*/


}
