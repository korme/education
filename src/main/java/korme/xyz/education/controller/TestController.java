package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.DynamicMapper;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class TestController {
    @Autowired
    DynamicMapper dynamicMapper;
    @Autowired
    ALiYunOssUtil aLiYunOssUtil;

    @GetMapping("/test")
    public ResponseEntity ttt()throws Exception{

        return new ResponseEntity(RespCode.ERROR_SESSION,aLiYunOssUtil.bucketIsExist());
    }
}
