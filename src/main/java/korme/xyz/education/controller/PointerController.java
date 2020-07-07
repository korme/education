package korme.xyz.education.controller;

import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.PointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;

@RestController
public class PointerController {
    @Autowired
    PointMapper pointMapper;
    @Transactional
    @RequestMapping("/point")
    public ResponseEntity point(@SessionAttribute("userId") Integer userId,
                                @NotNull Integer type,
                                @NotNull Integer id){
        int ispointed=pointMapper.pointIsExist(userId,id,type);
        if(ispointed==1)
            return new ResponseEntity(RespCode.SUCCESS);
        switch (type){
            case 1://video
                pointMapper.videoPointNumSelfAdd(id);
                pointMapper.addPoint(id,userId,type);
                break;
            case 2://article
                pointMapper.articlePointNumSelfAdd(id);
                pointMapper.addPoint(id,userId,type);
                break;
            case 3://dynamic
                pointMapper.dynamicPointNumSelfAdd(id);
                pointMapper.addPoint(id,userId,type);
                break;
                default:
                    return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }
    @Transactional
    @RequestMapping("/delPoint")
    public ResponseEntity delPoint(@SessionAttribute("userId") Integer userId,
                                @NotNull Integer type,
                                @NotNull Integer id){
        Integer ispointed=pointMapper.pointIsExist(userId,id,type);
        if(ispointed==null)
            return new ResponseEntity(RespCode.SUCCESS);
        switch (type){
            case 1://video
                pointMapper.videoPointNumSelfDec(id);
                pointMapper.delPoint(id,userId,type);
                break;
            case 2://article
                pointMapper.articlePointNumSelfDec(id);
                pointMapper.delPoint(id,userId,type);
                break;
            case 3://dynamic
                pointMapper.dynamicPointNumSelfDec(id);
                pointMapper.delPoint(id,userId,type);
                break;
            default:
                return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        return new ResponseEntity(RespCode.SUCCESS);
    }

    @RequestMapping("/isPoint")
    public ResponseEntity isPoint(@SessionAttribute("userId") Integer userId,
                                   @NotNull Integer type,
                                   @NotNull Integer id) {
        Integer ispointed = pointMapper.pointIsExist(userId, id, type);
        return new ResponseEntity(RespCode.SUCCESS,ispointed);
    }


}
