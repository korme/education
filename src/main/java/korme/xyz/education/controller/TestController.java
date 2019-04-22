package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.mapper.UserMapper;
import korme.xyz.education.model.receiverModel.DynamicModel;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import korme.xyz.education.service.timeUtil.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
public class TestController {
    @Autowired
    DynamicMapper dynamicMapper;
    @Autowired
    ALiYunOssUtil aLiYunOssUtil;
    @Autowired
    TimeUtils timeUtils;
    @Autowired
    UserMapper userMapper;
    @Value("${wxapp.appid}")
    String appId;

    @GetMapping("/test")
    public ResponseEntity ttt()throws Exception{
        /*INSERT INTO `education`.`dynamic`(`userId`, `images`,`kidgardenId`, `classId`, " +
        "`transDynamicId`, `excerpt`, `content`, `date`) VALUES (#{d.userId},#{d.images}," +
                "#{d.kidgardenId},#{d.classId},#{d.transDynamicId},#{d.excerpt},#{d.content},#{d.date})*/
        Map<String,Object> temp=userMapper.findUserType(2);
        int k=(int)temp.get("classId");

        return new ResponseEntity(RespCode.ERROR_SESSION,k);
    }
}
