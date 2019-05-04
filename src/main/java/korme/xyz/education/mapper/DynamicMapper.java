package korme.xyz.education.mapper;

import korme.xyz.education.model.DynamicOrdinaryModel;
import korme.xyz.education.model.OfficialDynamicModel;
import korme.xyz.education.model.receiverModel.DynamicModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "DynamicMapper")
public interface DynamicMapper {
    /*
    * 返回班级动态，时间倒序
    * */
    @SelectProvider(type = DynamicProvider.class,method = "findClassDynamicUp")
    List<Map<String,Object>> selectLimitClassAll(Integer userType, Integer kidgardenId, Integer classId);

    /*
     * 返回班级动态,时间倒序
     * 条件：动态发布时间小于Date
     * */
    @SelectProvider(type = DynamicProvider.class,method = "findClassDynamicDown")
    List<Map<String,Object>> selectLimitClassBeforeTime(Integer userType,Integer kidgardenId,Integer classId,int dynamicId);

    /*
     * 返回班级动态,时间倒序
     * 条件：动态发布时间小于Date
     * *//*
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where d.classId=#{classId} " +
            "and d.date<#{date} and d.delState=0 and u.overdueTime>NOW() order by d.date DESC")
    List<Map<String,Object>> selectLimitParentBeforeTime(@Param("classId")Integer classId,@Param("date")String date);*/

    /*
     * 返回园长发送的所有动态，时间倒序
     * userType:园长
     * */
    @Select("select d.dynamicId,d.images,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.userId=(select userId from user as u2 where u2.kidgardenId=#{kidgardenId} " +
            "and u2.userType=3 limit 1) and d.delState=0 and u.delState=0 order by d.dynamicId DESC")
    List<Map<String,Object>> selectPrincipalAll(@Param("kidgardenId")Integer kidgardenId);

    /*
     * 返回园长发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * userType：园长
     * */
    @Select("select d.dynamicId,d.images,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.userId=(select userId from user as u2 where u2.kidgardenId=#{kidgardenId} and u2.userType=3 limit 1)" +
            " and d.dynamicId<#{dynamicId} and d.delState=0 and u.delState=0 order by d.dynamicId DESC")
    List<Map<String,Object>> selectPrincipalBeforeTime(@Param("kidgardenId")Integer kidgardenId,@Param("dynamicId")Integer dynamicId);

    /*
     * 返回官方发送的所有动态，时间倒序
     * userType:官方
     * */
    @Select("select d.dynamicId,d.images,d.date,d.transDynamicId,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "u.userType=4 and d.delState=0 and u.delState=0 order by d.dynamicId DESC")
    List<OfficialDynamicModel> selectOfficialAll();

    /*
     * 返回官方发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * userType：官方
     * */
    @Select("select d.dynamicId,d.images,d.date,d.transDynamicId,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.dynamicId<#{dynamicId} and u.userType=4  and d.delState=0 and u.delState=0 order by d.dynamicId DESC")
    List<OfficialDynamicModel> selectOfficialBeforeTime(@Param("dynamicId")Integer dynamicId);

    /*
     * 返回  所有  园长发送的所有动态，时间倒序
     * userType:园长
     * */
    @Select("select d.dynamicId,d.images,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "u.userType=3 and d.delState=0 and u.delState=0 order by d.dynamicId DESC")
    List<Map<String,Object>> selectAllPrincipalAll();

    /*
     * 返回  所有  园长发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * userType：园长
     * */
    @Select("select d.dynamicId,d.images,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "u.userType=3 and d.dynamicId<#{dynamicId} and d.delState=0 and u.delState=0 order by d.dynamicId DESC")
    List<Map<String,Object>> selectAllPrincipalBeforeTime(@Param("dynamicId")int dynamicId);

    /*
     * 根据Id选择某条动态
     * */
    @Select("select d.dynamicId,d.images,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType,u.userId, d.commentNum,d.pointNum, d.browseNum,d.content from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where d.dynamicId=#{dynamicId} and d.delState=0")
    DynamicOrdinaryModel selectDynamicById(@Param("dynamicId")int dynamicId);

    /*
     * 根据Id选择某条Offical动态
     * */
    @Select("select d.dynamicId,d.images,d.transDynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType,u.userId, d.commentNum,d.pointNum, d.browseNum,d.content from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where d.dynamicId=#{dynamicId} and d.delState=0 ")
    OfficialDynamicModel selectOffDynamicById(@Param("dynamicId")int dynamicId);

    /*
    * 返回动态创建者Id
    * */
    @Select("select userId from dynamic where dynamicId=#{dynamicId}")
    Integer selectUserIdByDynamicId(@Param("dynamicId")int dynamicId);





    /*
    * 插入动态
    * */
    @Insert("INSERT INTO `education`.`dynamic`(`userId`, `images`,`kidgardenId`, `classId`, " +
            "`transDynamicId`, `excerpt`, `content`, `date`) VALUES (#{d.userId},#{d.images}," +
            "#{d.kidgardenId},#{d.classId},#{d.transDynamicId},#{d.excerpt},#{d.content},#{d.date})")
    void insertDynamic(@Param("d")DynamicModel d);

    /*
    * 删除动态
    * */
    @Update("update dynamic set delstate=1 where dynamicId=#{dynamicId}")
    void delDynamic(@Param("dynamicId")int dynamicId);





    class DynamicProvider{
        private int teacher=1;
        private int parent=2;
        private int principal=3;
        private int official=4;
        public String findClassDynamicUp(Integer userType,Integer kidgardenId,Integer classId){
            SQL sql=new SQL();
            sql.SELECT("d.dynamicId,d.images,d.date,d.excerpt,u.nickName," +
                    "u.headPortrait,u.userType, d.commentNum,d.pointNum,d.browseNum");
            sql.FROM("dynamic as d LEFT JOIN user as u ON d.userId=u.userId");

            if(userType==teacher||userType==parent)
                sql.WHERE("d.classId="+classId+" and u.userType in (1,2) and d.delState=0 and u.delState=0");
            else if(userType==principal)
                sql.WHERE("d.kidgardenId="+kidgardenId+" and d.delState=0 and u.delState=0");
            else
                sql.WHERE("d.delState=0 and u.delState=0");

            sql.ORDER_BY("d.dynamicId");
            return sql.toString()+" DESC";
        }

        public String findClassDynamicDown(Integer userType,Integer kidgardenId,Integer classId,int dynamicId){
            SQL sql=new SQL();
            sql.SELECT("d.dynamicId,d.images,d.date,d.excerpt,u.nickName," +
                    "u.headPortrait,u.userType, d.commentNum,d.pointNum,d.browseNum");
            sql.FROM("dynamic as d LEFT JOIN user as u ON d.userId=u.userId");

            if(userType==teacher||userType==parent)
                sql.WHERE("d.classId="+classId+" and u.userType in (1,2) and d.dynamicId<'"+dynamicId+"' and d.delState=0 and u.delState=0");
            else if(userType==principal)
                sql.WHERE("d.kidgardenId="+kidgardenId+"and d.dynamicId<'"+dynamicId+"' and d.delState=0 and u.delState=0");

            sql.ORDER_BY("d.dynamicId");
            return sql.toString()+" DESC";
        }



    }




}
