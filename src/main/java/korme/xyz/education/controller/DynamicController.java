package korme.xyz.education.controller;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.OfficialDynamicModel;
import korme.xyz.education.model.receiverModel.DynamicModel;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    @Autowired
    ALiYunOssUtil oss;
    @Value("${pagehelp-size}")
    int pagesize;
    int page=1;
    /*
    * 访问动态页面up刷新 下拉，全部刷新
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
                return new ResponseEntity(RespCode.SUCCESS,allOfficialDynamicUp(userId));
            case 4://所有园长
                return new ResponseEntity(RespCode.SUCCESS,allDynamicUp(userId));
        }
        return new ResponseEntity(RespCode.ERROR_INPUT);
    }
    /*
     * 访问动态页面Down刷新，上滑，查看更多
     * */
    @GetMapping("allDynamicDown")
    public ResponseEntity allDynamicDown(@SessionAttribute("userId") Integer userId,
                                         @NotNull Integer lastDynamicId,
                                       @NotNull Integer dynamicCateId){
        switch (dynamicCateId){
            case 1://班级
                return new ResponseEntity(RespCode.SUCCESS,allClassDynamicDown(userId,lastDynamicId,page));
            case 2://本园园长
                return new ResponseEntity(RespCode.SUCCESS,allMyKindergartenDynamicDown(userId,lastDynamicId,page));
            case 3://官方
                return new ResponseEntity(RespCode.SUCCESS,allOfficialDynamicDown(userId,lastDynamicId,page));
            case 4://所有园长
                return new ResponseEntity(RespCode.SUCCESS,allDynamicDown(userId,lastDynamicId,page));
        }
        return new ResponseEntity(RespCode.ERROR_INPUT);
    }
    /*
     * 插入动态
     * */
    @Transactional
    @GetMapping("addDynamic")
    public ResponseEntity addDynamic(@Validated DynamicModel dynamicModel,
                                     @SessionAttribute("userId") Integer userId) throws UnsupportedEncodingException, ClientException {
        Map<String,Object> userTypeMap=userMapper.findUserType(userId);
        dynamicModel.setUserId(userId);
        dynamicModel.setClassId((int)userTypeMap.get("classId"));
        dynamicModel.setKidgardenId((int)userTypeMap.get("kidgardenId"));
        int isEmpty=0;
        if(dynamicModel.getImages() == null ||dynamicModel.getImages().isEmpty())
            dynamicModel.setImages("[]");
        if(dynamicModel.getContent() == null ||dynamicModel.getContent().isEmpty()){
            dynamicModel.setContent("  ");
            isEmpty=1;
        }

        if(dynamicModel.getExcerpt() == null ||dynamicModel.getExcerpt().isEmpty()){
            dynamicModel.setExcerpt("  ");
        }
        if(isEmpty==0){
            List<String> tempString=new ArrayList<>();
            tempString.add(dynamicModel.getContent());
            tempString.add(dynamicModel.getExcerpt());
            try{
                tempString=oss.textScan(tempString);
            }
            catch (Exception e){
                return new ResponseEntity(RespCode.WARN_INPUT);
            }
            dynamicModel.setContent(tempString.get(0));
            dynamicModel.setExcerpt(tempString.get(1));
        }
        dynamicMapper.insertDynamic(dynamicModel);
        return new ResponseEntity(RespCode.SUCCESS);
    }
    /*
     * dynamic详情
     * */
    @GetMapping("dynamic")
    public ResponseEntity dynamic(@NotNull Integer dynamicId,
                                  @SessionAttribute("userId") Integer userId){
        OfficialDynamicModel result=dynamicMapper.selectOffDynamicById(userId,dynamicId);
        if(result==null)
            return new ResponseEntity(RespCode.WARN_ENPTY);
        if(result.getTransDynamicId()!=0)
            result.setChild(dynamicMapper.selectDynamicById(userId,result.getTransDynamicId()));
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
///////////////////////////////////////////本类调用方法/////////////////////////////////////////////////////////
    /*
     * 全部班级动态（根据userType返回不同）
     * */
    public ResponseEntity allClassDynamicUp(Integer userId){
        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectLimitClassAll(userId,(Integer) userType.get("userType"),
                (Integer)userType.get("kidgardenId"),(Integer)userType.get("classId"));
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 在时间lastDynamicId之前的班级动态（根据userType返回不同）
    * */
    public ResponseEntity allClassDynamicDown(Integer userId,
                                              Integer lastDynamicId,
                                              int page){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectLimitClassBeforeTime(userId,(Integer) userType.get("userType"),
                (Integer)userType.get("kidgardenId"),(Integer)userType.get("classId"),lastDynamicId);

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 全部本园长动态
    * */
    public ResponseEntity allMyKindergartenDynamicDown(Integer userId,
                                              Integer lastDynamicId,
                                              int page){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=
                dynamicMapper.selectPrincipalBeforeTime(userId,(Integer)userType.get("kidgardenId"), lastDynamicId);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
    * 在lastDynamicId之前的本园长动态
    * */
    public ResponseEntity allMyKindergartenDynamicUp(Integer userId){

        Map<String,Object> userType=userMapper.findUserType(userId);
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectPrincipalAll(userId,(Integer)userType.get("kidgardenId"));

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 在lastDynamicId前的所有幼儿园长动态
     * */
    public ResponseEntity allDynamicDown(Integer userId,
                                         Integer lastDynamicId,
                                         int page){
        PageHelper.startPage(page,pagesize);
        List<Map<String,Object>> result=
                dynamicMapper.selectAllPrincipalBeforeTime(userId,lastDynamicId);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 全部所有幼儿园长动态
     * */
    public ResponseEntity allDynamicUp(int userId){
        PageHelper.startPage(1,pagesize);
        List<Map<String,Object>> result=dynamicMapper.selectAllPrincipalAll(userId);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }

    /*
     * 在lastDynamicId前的官方长动态
     * */
    public ResponseEntity allOfficialDynamicDown(Integer userId,
                                         Integer lastDynamicId,
                                         int page){
        PageHelper.startPage(page,pagesize);
        List<OfficialDynamicModel> result=
                dynamicMapper.selectOfficialBeforeTime(userId,lastDynamicId);
        for(OfficialDynamicModel i:result){
            if(i.getTransDynamicId()!=0){
                i.setChild(dynamicMapper.selectDynamicById(userId,i.getTransDynamicId()));
            }
        }

        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    /*
     * 全部官方动态
     * */
    public ResponseEntity allOfficialDynamicUp(int userId){
        PageHelper.startPage(1,pagesize);
        List<OfficialDynamicModel> result=dynamicMapper.selectOfficialAll(userId);
        for(OfficialDynamicModel i:result){
            if(i.getTransDynamicId()!=0){
                i.setChild(dynamicMapper.selectDynamicById(userId,i.getTransDynamicId()));
                i.setHasChild(1);
            }
        }
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
    ///////////////////////////////////////////本类调用方法/////////////////////////////////////////////////////////





}
