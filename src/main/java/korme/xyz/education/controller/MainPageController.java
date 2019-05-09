package korme.xyz.education.controller;

import com.github.pagehelper.PageHelper;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import korme.xyz.education.mapper.VideoAndArticleMapper;
import korme.xyz.education.model.ArticleModel;
import korme.xyz.education.model.MainPageModel;
import korme.xyz.education.model.VideoModel;
import korme.xyz.education.service.ALiYunOssUtil.ALiYunOssUtil;
import korme.xyz.education.service.sortUtil.ListSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Validated
@RestController
public class MainPageController {
    @Autowired
    VideoAndArticleMapper vaMapper;
    @Autowired
    ListSort listSort;
    @Autowired
    ALiYunOssUtil oss;
    @Value("${pagehelp-size}")
    int pagesize;
    int page=1;
    @RequestMapping(value = "mainPage")
    public ResponseEntity mainPage(@SessionAttribute("userId") Integer userId,
                                   @NotNull Integer id){
        List<MainPageModel> result;
        if(id==0){
            result = vaMapper.selectMainPageUp(userId);
            if(result.isEmpty())
                return new ResponseEntity(RespCode.SUCCESS,result);

            while(result.size()!=0){
                if(result.get(result.size()-1).getType()==2)//2-article
                    result.remove(result.size()-1);
                else
                    return new ResponseEntity(RespCode.SUCCESS,result);
            }
            PageHelper.startPage(1,pagesize);
            result = vaMapper.selectNewVideo(userId);
        }

        else{
            PageHelper.startPage(1,pagesize);
            result=vaMapper.selectVideoBeforeId(userId,id);
        }
        if(result.isEmpty()||result.size()<2)
            return new ResponseEntity(RespCode.SUCCESS,result);
        List<MainPageModel> temp=
                vaMapper.selectArticleBetweenTime(userId,result.get(0).getCreateTime(),result.get(result.size()-1).getCreateTime());
        if(!temp.isEmpty()){
            for(MainPageModel i:temp)
                i.setType(2);//1:video,2:article
            result=listSort.mainPageListSort(result,temp);
        }
        return new ResponseEntity(RespCode.SUCCESS,result);
    }

    @RequestMapping(value = "/findVideoOrAticle")
    public ResponseEntity findVideoOrAticle(@SessionAttribute("userId") Integer userId,
                                            @NotNull Integer id,
                                            @NotNull Integer type){
        if(type==1){//video
            VideoModel result=vaMapper.selectSingleVideo(userId,id);
            if (result==null)
                return new ResponseEntity(RespCode.ERROR_INPUT,"视频不存在！");
            String url=oss.getDownloadUrl(result.getVideoBucket(),result.getVideoKey(),new Date(new Date().getTime() + 60 * 1000* 10));
            if(url==null)
                return new ResponseEntity(RespCode.ERROR_INPUT,"视频不存在！");
            result.setVideoUrl(url);
            return new ResponseEntity(RespCode.SUCCESS,result);
        }
        else if(type==2){//article
            ArticleModel result=vaMapper.selectSingleArticle(userId,id);
            if(result==null)
                return new ResponseEntity(RespCode.ERROR_INPUT,"文章不存在！");
            return new ResponseEntity(RespCode.SUCCESS,result);
        }
        return new ResponseEntity(RespCode.ERROR_INPUT);
    }


}
