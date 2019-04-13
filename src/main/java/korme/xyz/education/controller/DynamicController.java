package korme.xyz.education.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("dynamic")
public class DynamicController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DynamicMapper dynamicMapper;
    @Value("${pagehelp-size}")
    int pagesize;
    /*
    * 全部班级动态（根据userType返回不同）
    * */
    @GetMapping("allClassDynamicUp")
    public ResponseEntity allClassDynamicUp(@SessionAttribute("userId") Integer userId){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectLimitClassAll((Integer) userType.get("userType"),
                (Integer)userType.get("kidgardenId"),(Integer)userType.get("classId"));

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 在时间lastDynamicTime之前的班级动态（根据userType返回不同）
    * */
    @GetMapping("allClassDynamicDown")
    public ResponseEntity allClassDynamicDown(@SessionAttribute("userId") Integer userId,
                                              @NotBlank String lastDynamicTime,
                                              @NotNull int page){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectLimitClassBeforeTime((Integer) userType.get("userType"),
                (Integer)userType.get("kidgardenId"),(Integer)userType.get("classId"),lastDynamicTime);

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 全部本园长动态
    * todo:后面所有的测试还没做
    * */
    @GetMapping("allMyKindergartenDynamicDown")
    public ResponseEntity allMyKindergartenDynamicDown(@SessionAttribute("userId") Integer userId,
                                              @NotBlank String lastDynamicTime,
                                              @NotNull int page){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=
                dynamicMapper.selectPrincipalBeforeTime((Integer)userType.get("kidgardenId"), lastDynamicTime);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 在lastDynamicTime之前的本园长动态
    * */
    @GetMapping("allMyKindergartenDynamicUp")
    public ResponseEntity allMyKindergartenDynamicUp(@SessionAttribute("userId") Integer userId){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectPrincipalAll((Integer)userType.get("kidgardenId"));

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 全部所有幼儿园长动态
    * */
    @GetMapping("allDynamicDown")
    public ResponseEntity allDynamicDown(@SessionAttribute("userId") Integer userId,
                                                       @NotBlank String lastDynamicTime,
                                                       @NotNull int page){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=
                dynamicMapper.selectAllPrincipalBeforeTime((Integer)userType.get("kidgardenId"),lastDynamicTime);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 在lastDynamicTime史前的所有幼儿园长动态
    * */
    @GetMapping("allDynamicUp")
    public ResponseEntity allDynamicUp(@SessionAttribute("userId") Integer userId){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectAllPrincipalAll((Integer)userType.get("kidgardenId"));

        return new ResponseEntity(RespCode.SUCCESS,result);
    }



}
