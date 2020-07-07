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
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Value("${oss.videosDomain}")
    String videosDoamin;
    @Value("${oss.imgDomain}")
    String imgDomain;
    int page=1;
    /*@RequestMapping(value = "mainPage")
    public ResponseEntity mainPage(@SessionAttribute("userId") Integer userId,
                                   @NotNull Integer id,
                                    String lastType){//1-video,2-article
        Integer type;
        List<MainPageModel> result=new LinkedList<>();
        if(id==0){
            result = vaMapper.selectMainPageUp();
        }

        else{
            try{
                type=Integer.parseInt(lastType);
            }
            catch (Exception e){
                return new ResponseEntity(RespCode.SUCCESS);
            }
            if(type!=null&&type==1){
                VideoModel videoModel=vaMapper.selectSingleVideo(userId,id);
                if(videoModel==null)
                    return new ResponseEntity(RespCode.SUCCESS);
                List<MainPageModel> temp1=vaMapper.selectArticleBefroeTime(videoModel.getCreateTime());
                List<MainPageModel> temp2=vaMapper.selectVideoBeforeTime(videoModel.getCreateTime());
                if(temp1==null){
                    if(temp2==null)
                        return new ResponseEntity(RespCode.SUCCESS,result);
                    else
                        return new ResponseEntity(RespCode.SUCCESS,temp2);
                }
                else if(temp2==null){
                    return new ResponseEntity(RespCode.SUCCESS,temp1);
                }
                temp1.addAll(temp2);
                result = temp1.stream().sorted(Comparator.comparing(MainPageModel::getCreateTime))
                        .collect(Collectors.toList());
            }
            else{
                ArticleModel videoModel=vaMapper.selectSingleArticle(userId,id);
                if(videoModel==null)
                    return new ResponseEntity(RespCode.SUCCESS);
                List<MainPageModel> temp1=vaMapper.selectArticleBefroeTime(videoModel.getCreateTime());
                List<MainPageModel> temp2=vaMapper.selectVideoBeforeTime(videoModel.getCreateTime());
                if(temp1==null){
                    if(temp2==null)
                        return new ResponseEntity(RespCode.SUCCESS,result);
                    else
                        return new ResponseEntity(RespCode.SUCCESS,temp2);
                }
                else if(temp2==null){
                    return new ResponseEntity(RespCode.SUCCESS,temp1);
                }
                temp1.addAll(temp2);
                result = temp1.stream().sorted(Comparator.comparing(MainPageModel::getCreateTime))
                        .collect(Collectors.toList());

            }
        }

        return new ResponseEntity(RespCode.SUCCESS,result);
    }*/

    @RequestMapping(value = "mainPage")
    public ResponseEntity mainPage(@SessionAttribute("userId") Integer userId,
                                   @NotNull Integer id,
                                   String lastType) {//1-video,2-article
        if(userId==null)
            return new ResponseEntity(RespCode.SUCCESS,"cnm");
        List<MainPageModel> result=new LinkedList<>();
        if(id==0){
            PageHelper.startPage(1,pagesize);
            result = vaMapper.selectMainPageUp();
        }
        else
        {
            PageHelper.startPage(1,pagesize);
            result=vaMapper.selectMainPageDown(id);
        }


        return new ResponseEntity(RespCode.SUCCESS,result);
    }

    @RequestMapping(value = "/findVideoOrAticle")
    public ResponseEntity findVideoOrAticle(@SessionAttribute("userId") Integer userId,
                                            @NotNull Integer id,
                                            @NotNull Integer type){
        id=vaMapper.selectRealId(id);
        if(id==null){
            return new ResponseEntity(RespCode.ERROR_INPUT);
        }
        if(type==1){//video
            VideoModel result=vaMapper.selectSingleVideo(userId,id);
            vaMapper.addVideoBroswerNum(id);
            if (result==null)
                return new ResponseEntity(RespCode.ERROR_INPUT,"视频不存在！");
            String url="http://"+videosDoamin+"/"+result.getVideoKey();
            //String url=oss.getDownloadUrl(result.getVideoBucket(),result.getVideoKey(),new Date(new Date().getTime() + 60 * 1000* 10));
            if(url==null)
                return new ResponseEntity(RespCode.ERROR_INPUT,"视频不存在！");
            result.setVideoUrl(url);
            return new ResponseEntity(RespCode.SUCCESS,result);
        }
        else if(type==2){//article
            ArticleModel result=vaMapper.selectSingleArticle(userId,id);
            vaMapper.addArticleBroswerNum(id);
            if(result==null)
                return new ResponseEntity(RespCode.ERROR_INPUT,"文章不存在！");
            return new ResponseEntity(RespCode.SUCCESS,result);
        }
        return new ResponseEntity(RespCode.ERROR_INPUT);
    }


}
