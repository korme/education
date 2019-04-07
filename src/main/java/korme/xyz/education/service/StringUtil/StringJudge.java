package korme.xyz.education.service.StringUtil;

import org.springframework.stereotype.Service;

@Service
public class StringJudge {
    /*
    * 校验字符串是否为空
    * */
    public static boolean stringIsNull(String s){
        if(s==null||s.equals(""))
            return true;
        else
            return false;
    }
    /*
    * 校验字符串是否为密码格式
    * 规则：至少8个字符，至少1个字母，1个数字和1个特殊字（@$!%*#?&）。
    * */
    public static boolean stringIsPassword(String s){
        String passwordRegex="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        if(!stringIsNull(s)&& s.matches(passwordRegex))
            return true;
        return false;
    }
}
