package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.CommentMapper;
import korme.xyz.education.mapper.TeacherScoreMapper;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.ScoreModel;
import korme.xyz.education.model.UserTypeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("teacher")
public class TeacherScoreController {
    @Autowired
    TeacherScoreMapper teacherScoreMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;
    /*
    * 找到班内老师列表
    * */
    @RequestMapping(value = "findClassTeacherList")
    public ResponseEntity findClassTeacherList(@SessionAttribute("userId") Integer userId){
        UserTypeModel userType=userMapper.findUserTypeClea(userId);
        if(userType.getUserType()!=3&&userType.getUserType()!=2)
            return new ResponseEntity(RespCode.ERROR_INPUT);
        List<ScoreModel> result=teacherScoreMapper.selectClassTeacher(userType.getClassId(),userType.getKidgardenId());
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 找到幼儿园内老师列表
     * */
    @RequestMapping(value = "findKidgardenTeacherList")
    public ResponseEntity findKidgardenTeacherList(@SessionAttribute("userId") Integer userId){
        UserTypeModel userType=userMapper.findUserTypeClea(userId);
        if(userType.getKidgardenId()==0)
            return new ResponseEntity(RespCode.ERROR_INPUT);
        List<ScoreModel> result=teacherScoreMapper.selectKidgardenTeacher(userType.getKidgardenId());
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 给老师打分
     * */
    @Transactional
    @RequestMapping(value = "scoreTeacher")
    public ResponseEntity scoreTeacher(@SessionAttribute("userId") Integer userId,
                                       @NotNull Integer teacherId,
                                       @NotNull int star){
        if(star<1||star>5)
            return new ResponseEntity(RespCode.ERROR_INPUT,"评星数目错误");
        UserTypeModel userType=userMapper.findUserTypeClea(userId);
        UserTypeModel teacher=userMapper.findUserTypeClea(teacherId);
        if(userType.getUserType()!=2||userType.getClassId()!=teacher.getClassId())
            return new ResponseEntity(RespCode.ERROR_INPUT,"用户类型错误");
        int isScored=teacherScoreMapper.teacherScoreExist(teacherId,userId);
        if(isScored==1)
            return new ResponseEntity(RespCode.ERROR_INPUT,"您已经评价！");
        teacherScoreMapper.insertTeacherScore(teacherId,userId,star*10);
        teacherScoreMapper.updateScore(teacherId);
        return new ResponseEntity(RespCode.SUCCESS);
    }
    /*
    * 获取给老师的评价
    * */
    @RequestMapping("findMyScore")
    public ResponseEntity findMyScore(@SessionAttribute("userId") Integer userId,
                                      @NotNull Integer teacherId){
        Integer score=teacherScoreMapper.selectTeacherScore(teacherId,userId);
        if(score==null)
            score=0;
        String comment=commentMapper.selectCommentToTeacher(userId,teacherId);
        Map<String,String> result=new HashMap<>();
        result.put("score",score.toString());
        result.put("comment",comment);
        return new ResponseEntity(RespCode.SUCCESS,result);


    }




}
