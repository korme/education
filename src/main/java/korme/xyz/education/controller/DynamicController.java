package korme.xyz.education.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.OfficialDynamic;
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
    * 访问动态页面up刷新
    * */
    @GetMapping("allDynamicUp")
    public ResponseEntity allDynamicUp(@SessionAttribute("userId") Integer userId,
                                        @NotNull Integer dynamicCateId){
        switch (dynamicCateId){
            case 1://班级
                return new ResponseEntity(RespCode.SUCCESS,allClassDynamicUp(userId));
            case 2://本园园长
                return new ResponseEntity(RespCode.SUCCESS,allMyKindergartenDynamicUp(userId));
            case 3://官方
                return new ResponseEntity(RespCode.SUCCESS,allOfficialDynamicUp());
            case 4://所有园长
                return new ResponseEntity(RespCode.SUCCESS,allDynamicUp());
        }
        return new ResponseEntity(RespCode.ERROR_INPUT);
    }
    /*
     * 访问动态页面
     * */
    @GetMapping("allDynamicDown")
    public ResponseEntity allDynamicDown(@SessionAttribute("userId") Integer userId,
                                         @NotBlank String lastDynamicTime,
                                       @NotNull Integer page,
                                       @NotNull Integer dynamicCateId){
        switch (dynamicCateId){
            case 1://班级
                return new ResponseEntity(RespCode.SUCCESS,allClassDynamicDown(userId,lastDynamicTime,page));
            case 2://本园园长
                return new ResponseEntity(RespCode.SUCCESS,allMyKindergartenDynamicDown(userId,lastDynamicTime,page));
            case 3://官方
                return new ResponseEntity(RespCode.SUCCESS,allOfficialDynamicDown(userId,lastDynamicTime,page));
            case 4://所有园长
                return new ResponseEntity(RespCode.SUCCESS,allDynamicDown(userId,lastDynamicTime,page));
        }
        return new ResponseEntity(RespCode.ERROR_INPUT);
    }

    /*
     * 全部班级动态（根据userType返回不同）
     * */
    @GetMapping("allClassDynamicUp")
    public ResponseEntity allClassDynamicUp(Integer userId){
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
    public ResponseEntity allClassDynamicDown(Integer userId,
                                              String lastDynamicTime,
                                              int page){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectLimitClassBeforeTime((Integer) userType.get("userType"),
                (Integer)userType.get("kidgardenId"),(Integer)userType.get("classId"),lastDynamicTime);

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 全部本园长动态
    * */
    @GetMapping("allMyKindergartenDynamicDown")
    public ResponseEntity allMyKindergartenDynamicDown(Integer userId,
                                              String lastDynamicTime,
                                              int page){

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
    public ResponseEntity allMyKindergartenDynamicUp(Integer userId){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectPrincipalAll((Integer)userType.get("kidgardenId"));

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 在lastDynamicTime前的所有幼儿园长动态
     * */
    @GetMapping("allKindergartenDynamicDown")
    public ResponseEntity allDynamicDown(Integer userId,
                                         String lastDynamicTime,
                                         int page){
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=
                dynamicMapper.selectAllPrincipalBeforeTime(lastDynamicTime);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 全部所有幼儿园长动态
     * */
    @GetMapping("allKindergartenDynamicUp")
    public ResponseEntity allDynamicUp(){
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectAllPrincipalAll();
        return new ResponseEntity(RespCode.SUCCESS,result);
    }

    /*
     * 在lastDynamicTime前的官方长动态
     * */
    @GetMapping("allOfficialDynamicDown")
    public ResponseEntity allOfficialDynamicDown(Integer userId,
                                         String lastDynamicTime,
                                         int page){
        PageHelper.startPage(page,pagesize);
        List<OfficialDynamic> result=
                dynamicMapper.selectOfficialBeforeTime(lastDynamicTime);
        for(OfficialDynamic i:result){
            if(i.getTransDynamicId()!=0){
                i.setChild(dynamicMapper.selectDynamicById(i.getTransDynamicId()));
            }
        }

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 全部官方动态
     * */
    @GetMapping("allOfficialDynamicUp")
    public ResponseEntity allOfficialDynamicUp(){
        PageHelper.startPage(1,pagesize);
        List<OfficialDynamic> result=dynamicMapper.selectOfficialAll();
        for(OfficialDynamic i:result){
            if(i.getTransDynamicId()!=0){
                i.setChild(dynamicMapper.selectDynamicById(i.getTransDynamicId()));
            }
        }
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*@GetMapping("addDynamic")
    public ResponseEntity*/




}
