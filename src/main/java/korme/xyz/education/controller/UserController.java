package korme.xyz.education.controller;

import java.text.ParseException;
import java.util.Map;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.service.MD5Utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MD5Util md5Util;
    @GetMapping("/login")
    public ResponseEntity login(String userName, String passWord, HttpServletRequest request) throws ParseException {
        Map<String,Object> map=userMapper.findPassword(userName);
        String truePassWord=(String)map.get("passWord");
        try{
            if(truePassWord!=null&&md5Util.getStringMD5(passWord).equals(truePassWord)){
                int userId=(int)map.get("userId");
                HttpSession sessoin=request.getSession();
                sessoin.setAttribute("userId",userId);
                return new ResponseEntity(RespCode.SUCCESS,sessoin.getId());
            }
        }
        catch (Exception e){
            return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.ERROR_SESSION);
    }

    @GetMapping("/loginError")
    public ResponseEntity loginError(Model model) throws ParseException {
        return new ResponseEntity(RespCode.ERROR_SESSION);
    }
    @GetMapping("/test")
    public ResponseEntity ttt(Model model) throws ParseException {
        return new ResponseEntity(RespCode.ERROR_SESSION);
    }
}