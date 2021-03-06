package korme.xyz.education.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


@Mapper
@Component(value = "PointMapper")
public interface PointMapper {
    /*
     * 点赞是否存在
     * */
    @Select("SELECT COUNT(*) from point where userId=#{userId} and pointedId=#{pointedId} AND pointType=#{pointType} LIMIT 1")
    int pointIsExist(@Param("userId")int userId,@Param("pointedId")int pointedId,@Param("pointType")int pointType);
    /*
    * video点赞
    * */
    @Update("update mainpage SET pointNum=pointNum+1 where videoOrArticleId=#{videoOrArticleId} and type=1")
    void videoPointNumSelfAdd(@Param("videoOrArticleId")int videoOrArticleId);
    /*
     * article点赞
     * */
    @Update("update mainpage SET pointNum=pointNum+1 where videoOrArticleId=#{videoOrArticleId} and type=2")
    void articlePointNumSelfAdd(@Param("videoOrArticleId")int videoOrArticleId);
    /*
     * dynamic点赞
     * */
    @Update("update dynamic SET pointNum=pointNum+1 where dynamicId=#{dynamicId}")
    void dynamicPointNumSelfAdd(@Param("dynamicId")int dynamicId);

    /*
    * 插入点赞数据
    * */
    @Insert("INSERT INTO `point`(`pointedId`, `userId`, `pointType`, `createTime`) " +
            "VALUES (#{pointedId},#{userId},#{pointType},NOW())")
    void addPoint(@Param("pointedId")int pointedId,@Param("userId")int userId,@Param("pointType")int pointType);
    /*
    * 清除点赞数据
    * */
    @Delete("delete from point where pointedId=#{pointedId} and userId=#{userId} and pointType=#{pointType} limit 1")
    void delPoint(@Param("pointedId")int pointedId,@Param("userId")int userId,@Param("pointType")int pointType);

    /*
     * video取消点赞
     * */
    @Update("update mainpage SET pointNum=pointNum-1 where videoOrArticleId=#{videoOrArticleId} and type=1")
    void videoPointNumSelfDec(@Param("videoOrArticleId")int videoOrArticleId);
    /*
     * article取消点赞
     * */
    @Update("update mainpage SET pointNum=pointNum-1 where videoOrArticleId=#{videoOrArticleId} and type=2")
    void articlePointNumSelfDec(@Param("videoOrArticleId")int videoOrArticleId);
    /*
     * dynamic取消点赞
     * */
    @Update("update dynamic SET pointNum=pointNum-1 where dynamicId=#{dynamicId}")
    void dynamicPointNumSelfDec(@Param("dynamicId")int dynamicId);




}
