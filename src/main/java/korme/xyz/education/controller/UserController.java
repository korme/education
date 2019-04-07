package korme.xyz.education.controller;

import java.text.ParseException;
import java.util.Map;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.service.MD5Utils.MD5Util;
import korme.xyz.education.service.StringUtil.StringJudge;
import korme.xyz.education.service.timeUtil.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MD5Util md5Util;
    @Autowired
    TimeUtils timeUtils;
    /*
    * 登录接口
    * todo：返回值看接口
    * */
    @GetMapping("/login")
    public ResponseEntity login(@NotBlank String userName, @NotBlank String passWord,
            HttpServletRequest request) throws Exception {
        Map<String,Object> map=userMapper.findPasswordByUserName(userName,timeUtils.getNowTime());
        String truePassWord=(String)map.get("passWord");
        try{
            if(!StringJudge.stringIsNull(truePassWord)&&md5Util.getStringMD5(passWord).equals(truePassWord)){
                int userId=(int)map.get("userId");
                HttpSession sessoin=request.getSession();
                sessoin.setAttribute("userId",userId);
                return new ResponseEntity(RespCode.SUCCESS,sessoin.getId());
            }
        }
        catch (Exception e){
            return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.ERROR_USER);
    }
    /*
    * 前台用户更改密码
    * */
    @GetMapping("/changePassWord")
    public ResponseEntity changePassWord(
            @NotEmpty @SessionAttribute("userId") Integer userId,
            @NotEmpty String passWord, @NotEmpty String newpassWord)throws Exception{
        //校验新密码
        if (!StringJudge.stringIsPassword(newpassWord))
            return new ResponseEntity(RespCode.ERROR_INPUT,"新密码格式错误");

        String truePassWord=userMapper.findPasswordByUserId(userId,timeUtils.getNowTime());
        //判断旧密码
        if(!md5Util.getStringMD5(passWord).equals(truePassWord))
            return new ResponseEntity(RespCode.ERROR_INPUT,"旧密码输入错误！");


        userMapper.updatePassword(newpassWord,userId);



        return new ResponseEntity(RespCode.SUCCESS);
    }


    @GetMapping("/loginError")
    public ResponseEntity loginError() throws ParseException {
        return new ResponseEntity(RespCode.ERROR_SESSION);
    }
    @GetMapping("/test")
    public ResponseEntity ttt(@Max(value=1)Integer userId)throws Exception{
        return new ResponseEntity(RespCode.ERROR_SESSION,userId);
    }
}