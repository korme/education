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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        List<List<String>> result=new ArrayList<>();
        List<String> test=new ArrayList<>();
        test.add("你真是个傻逼");
        test.add("你写代码像蔡徐坤");
        test.add("我透你妈");
        long start=System.currentTimeMillis();

        for(int i=0;i<100;i++){
            result.add(aLiYunOssUtil.textScan(test));
        }
        long end=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(end-start)+"ms");

        return new ResponseEntity(RespCode.ERROR_SESSION,result);
    }
}
