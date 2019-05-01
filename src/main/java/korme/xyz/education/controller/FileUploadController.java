package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import korme.xyz.education.service.uuidUTil.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("file")
public class FileUploadController {
    @Autowired
    ALiYunOssUtil aLiYunOssUtil;
    @Value("${oss.imageBucketName}")
    String imageBucketName;
    @Autowired
    UuidUtil uuidUtil;
    @GetMapping("uploadUrl")
    public ResponseEntity getUploadImageUrl() throws Exception {
        String result=aLiYunOssUtil.getTempUploadUrl(imageBucketName);
        return new ResponseEntity(RespCode.SUCCESS,result);
    }
}
