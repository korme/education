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
     *查找最新的视频列表
     * *//*
    @Select("select * from (SELECT a.articleId as videoId,2 as type,a.articleTitle as title,a.imgUrl as coverUrl,a.createTime,a.browseNum,a.commentNum,a.pointNum from article a where delState=0  union SELECT v.videoId,1 as type,v.title,v.coverUrl,v.createTime,v.browseNum,v.commentNum,v.pointNum from video as v where v.delState=0) temp ORDER BY temp.createTime desc limit 15")
    List<MainPageModel> selectMainPageUp();*/

    /*
    * 查找最新的视频列表
    * */
    @Select("SELECT v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum from video as v where v.delState=0 ORDER BY v.videoId DESC")
    List<MainPageModel> selectNewVideo(@Param("videoId")int videoId);



    /*
    * 查找 Id 小于 videoId的 视频列表
    * */
    @Select("SELECT v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum  from video as v  where v.videoId<#{videoId} and v.delState=0 ORDER BY v.videoId DESC")
    List<MainPageModel> selectVideoBeforeId(@Param("videoId")int videoId);

    /*
     * 查找 在time前的 文章列表
     * */
    @Select("SELECT 1 as type,v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum  from video as v  where v.createTime<#{createTime} and v.delState=0 ORDER BY v.videoId DESC limit 7")
    List<MainPageModel> selectVideoBeforeTime(@Param("createTime")String createTime);



    /*
    * 查找某一个时间段的文章
    * */
    @Select("SELECT 2 as type,a.articleId as videoId,a.articleTitle as title,a.imgUrl as coverUrl,a.createTime,a.browseNum,a.commentNum,a.pointNum from article a where (a.createTime < #{createTime}}) and delState=0 ORDER BY createTime DESC limit 7")
    List<MainPageModel> selectArticleBefroeTime(@Param("createTime")String createTime);
    /*
    * 查找某个视频的信息
    * */
    @Select("SELECT v.videoId,v.coverUrl,v.createTime,v.title,v.pointNum,v.browseNum,v.commentNum,v.description,v.videoKey,v.videoBucket,a.headPortrait,a.nickName,IFNULL(p.pointId,0) as pointId from video v LEFT JOIN administrator a on v.creatorId=a.id left JOIN (select * from  point  where userId=#{userId} and pointedId=#{videoId} and pointType=1) p on p.pointedId=v.videoId where v.videoId=#{videoId} and v.delState=0")
    VideoModel selectSingleVideo(@Param("userId")int userId,@Param("videoId")int videoId);

    /*
    * 查找文章信息
    * */
    @Select("select ar.articleId,ar.imgUrl,ar.articleTitle,ar.content,ar.createTime,ar.pointNum,ar.browseNum,ar.commentNum,ad.headPortrait,ad.nickName,IFNULL(p.pointId,0) as pointId from article ar LEFT JOIN administrator ad on ar.creatorId=ad.id left JOIN (select * from  point  where userId=#{userId} and pointedId=#{articleId} and pointType=2) p on p.pointedId=ar.articleId where ar.articleId=#{articleId} and ar.delState=0 ")
    ArticleModel selectSingleArticle(@Param("userId")int userId,@Param("articleId")int articleId);

    /*
     * 视频浏览量自增
     * */
    @Select("update mainpage set browseNum=browseNum+1 where videoOrArticleId=#{videoOrArticleId} and type=1 ")
    void addVideoBroswerNum(@Param("videoOrArticleId")int videoOrArticleId);
    /*
     * 文章浏览量自增
     * */
    @Select("update mainpage set browseNum=browseNum+1 where videoOrArticleId=#{videoOrArticleId} and type=2 ")
    void addArticleBroswerNum(@Param("videoOrArticleId")int videoOrArticleId);

    /*
    * mainpage查询
    * */
    @Select("select `id`, videoOrArticleId videoId, `type`, `coverUrl`, `title`, `pointNum`, `browseNum`, `commentNum`,createTime from mainpage where delStatue=0 order by id desc")
    List<MainPageModel>selectMainPageUp();

    @Select("select `id`, videoOrArticleId videoId, `type`, `coverUrl`, `title`, `pointNum`, `browseNum`, `commentNum`,createTime from mainpage where delStatue=0 and id<#{id}  order by id desc")
    List<MainPageModel>selectMainPageDown(@Param("id")int id);

    //找出真实Id
    @Select("select videoOrArticleId from mainpage where id=#{id}")
    Integer selectRealId(@Param("id")int id);


}
