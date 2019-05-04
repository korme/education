package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.CommentMapper;
import korme.xyz.education.mapper.CommentReplyMapper;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.UserTypeModel;
import korme.xyz.education.model.receiverModel.CommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;
@Validated
@RestController
public class DelController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentReplyMapper commentReplyMapper;
    @Autowired
    DynamicMapper dynamicMapper;
    @Autowired
    CommentMapper commentMapper;
    @RequestMapping("del")
    public ResponseEntity del(@SessionAttribute("userId")int userId,
                              @NotNull Integer type,//分类Type 1-video,2-article,3-dynamic
                              @NotNull Integer types,//0-本身，1-comment，2-reply
                              @NotNull Integer id){
        UserTypeModel userType=userMapper.findUserTypeClea(userId);
        switch (type){
            case 1:
                switch (types){
                    case 1:
                        if(userType.getUserType()!=4){
                            Integer s=commentReplyMapper.selectUserIdByVideoCommentId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        commentMapper.delVideoComment(id);
                        commentMapper.decVideoCommentNum(id);
                        break;
                    case 2:
                        if(userType.getUserType()!=4){
                            Integer s=commentReplyMapper.selectUserIdByVideoCommentReplyId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        commentReplyMapper.delVideoCommentReply(id);
                        commentReplyMapper.delVideoCommentReply(id);
                        commentMapper.updateVideoCommentHasChild(commentReplyMapper.countVideoCommentReply(id),id);
                        break;
                }
                break;
            case 2:
                switch (types){
                    case 1:
                        if(userType.getUserType()!=4){
                            Integer s=commentReplyMapper.selectUserIdByArticleCommentId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        commentMapper.delArticleComment(id);
                        commentMapper.decArticleCommentNum(id);
                        break;
                    case 2:
                        if(userType.getUserType()!=4){
                            Integer s=commentReplyMapper.selectUserIdByArticleCommentReplyId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        commentReplyMapper.delArticleCommentReply(id);
                        commentMapper.updateArticleCommentHasChild(commentReplyMapper.countArticleCommentReply(id),id);
                        break;
                }
                break;
            case 3:
                switch (types){
                    case 0:
                        if(userType.getUserType()!=4){
                            Integer s=dynamicMapper.selectUserIdByDynamicId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        dynamicMapper.delDynamic(id);
                        break;
                    case 1:
                        if(userType.getUserType()!=4){
                            Integer s=commentReplyMapper.selectUserIdByDynamicCommentId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        commentMapper.delDynamicComment(id);
                        commentMapper.decDynamicCommentNum(id);
                        break;
                    case 2:
                        if(userType.getUserType()!=4){
                            Integer s=commentReplyMapper.selectUserIdByDynamicCommentReplyId(id);
                            if(s==null||s!=userId)
                                return new ResponseEntity(RespCode.ERROR_USER,"没有限权！");
                        }
                        commentReplyMapper.delDynamicCommentReply(id);
                        commentMapper.updateDynamicCommentHasChild(commentReplyMapper.countDynamicCommentReply(id),id);
                }

                break;
                default:
                    return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }
    /*public void delComment(@SessionAttribute("userId")int userId,
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
        }
    }
    public void delCommentReply(@SessionAttribute("userId")int userId,
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
