package korme.xyz.education.controller;

import java.text.ParseException;
import java.util.Base64;
import java.util.Map;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.UserLoginModel;
import korme.xyz.education.service.MD5Utils.MD5Util;
import korme.xyz.education.service.StringUtil.StringJudge;
import korme.xyz.education.service.timeUtil.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.Sun;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import sun.misc.BASE64Decoder;

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
    * */
    @GetMapping("/login")
    public ResponseEntity login(@NotBlank String userName, @NotBlank String passWord,
            HttpServletRequest request) throws Exception {
        UserLoginModel map=userMapper.findPasswordByUserName(userName,timeUtils.getNowTime());
        if(map==null)
            return new ResponseEntity(RespCode.ERROR_USER,"用户不存在或已过期");
        String truePassWord=(String)map.getPassWord();
        try{
            if(md5Util.getStringMD5(passWord).equals(truePassWord)){
                int userId=(int)map.getUserId();
                HttpSession sessoin=request.getSession();
                sessoin.setAttribute("userId",userId);
                map.setPassWord("");
                map.setSessionKey(new sun.misc.BASE64Encoder().encode(sessoin.getId().getBytes()));
                return new ResponseEntity(RespCode.SUCCESS,map);
            }
            else
                return new ResponseEntity(RespCode.ERROR_USER,"账号或密码错误");
        }
        catch (Exception e){
            return new ResponseEntity(RespCode.ERROR_INPUT);
        }
    }
    /*
    * 前台用户更改密码
    * */
    @Transactional
    @GetMapping("/changePassWord")
    public ResponseEntity changePassWord(
            @NotBlank String userName,
            @NotEmpty String passWord, @NotEmpty String newpassWord)throws Exception{
        //校验新密码
        /*if (!StringJudge.stringIsPassword(newpassWord))
            return new ResponseEntity(RespCode.ERROR_INPUT,"新密码格式错误");*/

        UserLoginModel user=userMapper.findPasswordByUserName(userName,timeUtils.getNowTime());
        //判断旧密码
        if(!md5Util.getStringMD5(passWord).equals(user.getPassWord()))
            return new ResponseEntity(RespCode.ERROR_INPUT,"旧密码输入错误！");

        userMapper.updatePassword(md5Util.getStringMD5(newpassWord),user.getUserId());
        return new ResponseEntity(RespCode.SUCCESS);
    }

    /*
     * 前台用户更改头像
     * */
    @Transactional
    @GetMapping("/changeHeadPortrait")
    public ResponseEntity changeHeadPortrait(@SessionAttribute("userId") Integer userId,
                                         @NotEmpty String headPortrait)throws Exception{

        userMapper.updateHeadPortrait(headPortrait,userId);
        return new ResponseEntity(RespCode.SUCCESS);
    }
    /*
     * 前台用户更改昵称
     * */
    @Transactional
    @GetMapping("/changeNickName")
    public ResponseEntity changeNickName(@SessionAttribute("userId") Integer userId,
                                         @NotEmpty String nickName)throws Exception{

        userMapper.updateNickName(nickName,userId);
        return new ResponseEntity(RespCode.SUCCESS);
    }


    @GetMapping("/loginError")
    public ResponseEntity loginError() throws ParseException {
        return new ResponseEntity(RespCode.ERROR_SESSION);
    }

}