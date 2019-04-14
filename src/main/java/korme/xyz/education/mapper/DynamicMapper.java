package korme.xyz.education.mapper;

import korme.xyz.education.model.OfficialDynamic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Value;
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
    List<Map<String,Object>> selectLimitClassAll(Integer userType, Integer kidgardenId, Integer classsId);

    /*
     * 返回班级动态,时间倒序
     * 条件：动态发布时间小于Date
     * */
    @SelectProvider(type = DynamicProvider.class,method = "findClassDynamicDown")
    List<Map<String,Object>> selectLimitClassBeforeTime(Integer userType,Integer kidgardenId,Integer classId,String date);

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
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.userId=(select userId from user as u2 where u2.kidgardenId=#{kidgardenId} " +
            "and u2.userType=3 limit 1) and d.delState=0 and u.delState=0 order by d.date DESC")
    List<Map<String,Object>> selectPrincipalAll(@Param("kidgardenId")Integer kidgardenId);

    /*
     * 返回园长发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * userType：园长
     * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.userId=(select userId from user as u2 where u2.kidgardenId=#{kidgardenId} and u2.userType=3 limit 1)" +
            " and d.date<#{date} and d.delState=0 and u.delState=0 order by d.date DESC")
    List<Map<String,Object>> selectPrincipalBeforeTime(@Param("kidgardenId")Integer kidgardenId,@Param("date")String date);

    /*
     * 返回官方发送的所有动态，时间倒序
     * userType:官方
     * */
    @Select("select d.dynamicId,d.date,d.transDynamicId,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "u.userType=4 and d.delState=0 and u.delState=0 order by d.date DESC")
    List<OfficialDynamic> selectOfficialAll();

    /*
     * 返回官方发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * userType：官方
     * */
    @Select("select d.dynamicId,d.date,d.transDynamicId,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.date<#{date} and u.userType=4  and d.delState=0 and u.delState=0 order by d.date DESC")
    List<OfficialDynamic> selectOfficialBeforeTime(@Param("date")String date);

    /*
     * 返回  所有  园长发送的所有动态，时间倒序
     * userType:园长
     * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "u.userType=3 and d.delState=0 and u.delState=0 order by d.date DESC")
    List<Map<String,Object>> selectAllPrincipalAll();

    /*
     * 返回  所有  园长发送的动态,时间倒序
     * 条件：动态发送时间小于Date
     * userType：园长
     * */
    @Select("select d.dynamicId,d.date,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "u.userType=3 and d.date<#{date} and d.delState=0 and u.delState=0 order by d.date DESC")
    List<Map<String,Object>> selectAllPrincipalBeforeTime(@Param("date")String date);

    /*
     * 根据Id选择某条评论
     * */
    @Select("select d.dynamicId,d.date,d.transDynamicId,d.excerpt,u.nickName,u.headPortrait,u.userType, d.commentNum,d.pointNum," +
            "d.browseNum from dynamic as d LEFT JOIN user as u ON d.userId=u.userId where " +
            "d.dynamicId=#{dynamicId} and d.delState=0 and u.delState=0")
    OfficialDynamic selectDynamicById(@Param("dynamicId")int dynamicId);

    /*
     * 选取某条动态的全部一级评论
     * todo：
     * */
    //@Select("")





    class DynamicProvider{
        private int teacher=1;
        private int parent=2;
        private int principal=3;
        private int official=4;
        public String findClassDynamicUp(Integer userType,Integer kidgardenId,Integer classsId){
            SQL sql=new SQL();
            sql.SELECT("d.dynamicId,d.date,d.excerpt,u.nickName," +
                    "u.headPortrait,u.userType, d.commentNum,d.pointNum,d.browseNum");
            sql.FROM("dynamic as d LEFT JOIN user as u ON d.userId=u.userId");

            if(userType==teacher||userType==parent)
                sql.WHERE("d.classId="+classsId+" and d.delState=0 and u.delState=0");
            else if(userType==principal)
                sql.WHERE("d.kidgardenId="+kidgardenId+" and d.delState=0 and u.delState=0");
            else
                sql.WHERE("d.delState=0 and u.delState=0");

            sql.ORDER_BY("d.date");
            return sql.toString()+" DESC";
        }

        public String findClassDynamicDown(Integer userType,Integer kidgardenId,Integer classsId,String date){
            SQL sql=new SQL();
            sql.SELECT("d.dynamicId,d.date,d.excerpt,u.nickName," +
                    "u.headPortrait,u.userType, d.commentNum,d.pointNum,d.browseNum");
            sql.FROM("dynamic as d LEFT JOIN user as u ON d.userId=u.userId");

            if(userType==teacher||userType==parent)
                sql.WHERE("d.classId="+classsId+" and d.date<'"+date+"' and d.delState=0 and u.delState=0");
            else if(userType==principal)
                sql.WHERE("d.kidgardenId="+kidgardenId+"and d.date<'"+date+"' and d.delState=0 and u.delState=0");

            sql.ORDER_BY("d.date");
            return sql.toString()+" DESC";
        }



    }




}
