package korme.xyz.education.mapper;

import korme.xyz.education.model.ArticleModel;
import korme.xyz.education.model.MainPageModel;
import korme.xyz.education.model.VideoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "VideoAndArticleMapper")
public interface VideoAndArticleMapper {
    /*
    * 查找最新的视频列表
    * */
    @Select("SELECT v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum from video as v where delState=0 ORDER BY videoId DESC")
    List<MainPageModel> selectNewVideo();
    /*
    * 查找 Id 小于 videoId的 视频列表
    * */
    @Select("SELECT v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum from video as v where v.videoId<#{videoId} and delState=0 ORDER BY videoId DESC")
    List<MainPageModel> selectVideoBeforeId(@Param("videoId")int videoId);
    /*
    * 查找某一个时间段的文章
    * */
    @Select("SELECT articleId as videoId,articleTitle as title,imgUrl as coverUrl,createTime,browseNum,commentNum,pointNum from article where (createTime BETWEEN #{creaTimeA} and #{creaTimeB}) and delState=0 ORDER BY createTime DESC")
    List<MainPageModel> selectArticleBetweenTime(@Param("creaTimeA")String creaTimeA,@Param("creaTimeB")String creaTimeB);
    /*
    * 查找某个视频的信息
    * */
    @Select("SELECT v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum,v.description,v.videoKey,v.videoBucket,a.headPortrait,a.nickName from video v LEFT JOIN administrator a on v.creatorId=a.id where v.videoId=#{videoId} and v.delState=0")
    VideoModel selectSingleVideo(@Param("videoId")int videoId);

    /*
    * 查找文章信息
    * */
    @Select("select ar.articleId,ar.imgUrl,ar.articleTitle,ar.content,ar.createTime,ar.pointNum,ar.browseNum,ar.commentNum,ad.headPortrait,ad.nickName from article ar LEFT JOIN administrator ad on ar.creatorId=ad.id where ar.articleId=#{articleId} and ar.delState=0 ")
    ArticleModel selectSingleArticle(@Param("articleId")int articleId);
}
