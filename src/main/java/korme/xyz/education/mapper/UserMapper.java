package korme.xyz.education.mapper;

import korme.xyz.education.model.UserLoginModel;
import korme.xyz.education.model.UserTypeModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component(value = "UserMapper")
public interface UserMapper {
    /*
     * 根据userName，返回未过期用户的密码
     * */
    @Select("select passWord,userId,headPortrait,nickName,overdueTime,userType from user " +
            "where userName=#{userName} and overdueTime>#{now}")
    UserLoginModel findPasswordByUserName(@Param("userName")String userName, @Param("now")String now);


    /*
     * 根据userId，返回未过期用户的密码
     * */
    @Select("select passWord from user where userId=#{userId} and overdueTime>#{now}")
    String findPasswordByUserId(@Param("userId")int userId,@Param("now")String now);

    /*
     * 根据userName，返回未过期用户的密码
     * */
    @Select("select passWord from user where userName=#{userName} and overdueTime>#{now}")
    String selectPasswordByUserName(@Param("userName")String userName,@Param("now")String now);

    /*
    * 根据userId，返回用户的基本类型信息
    * */
    @Select("select userName,nickName,kidgardenId,classId,userType,lastActiveTime " +
            "from user where userId=#{userId}")
    Map<String,Object> findUserType(@Param("userId")int userId);

    /*
     * 根据userId，返回用户的基本类型信息
     * */
    @Select("select userName,nickName,kidgardenId,classId,userType,lastActiveTime " +
            "from user where userId=#{userId}")
    UserTypeModel findUserTypeClea(@Param("userId")int userId);

    /*
     * 根据userId，返回用户的详细信息
     * */
    @Select("select userId from user where userName=#{userName}")
    Map<String,Object> findUserAll(@Param("userName")String userName);


    /*
     * 更新用户密码
     * */
    @Update("UPDATE user set `passWord`=#{passWord} where userId=#{userId}")
    void updatePassword(@Param("passWord")String passWord,@Param("userId")int userId);

    /*
     * 更新用户头像
     * */
    @Update("UPDATE user set `headPortrait`=#{headPortrait} where userId=#{userId}")
    void updateHeadPortrait(@Param("headPortrait")String headPortrait,@Param("userId")int userId);

    /*
     * 更新用户头像
     * */
    @Update("UPDATE user set `nickName`=#{nickName} where userId=#{userId}")
    void updateNickName(@Param("nickName")String nickName,@Param("userId")int userId);

    /*
    * 更新活跃时间
    * */
    @Update("UPDATE `user` set lastActiveTime=NOW() where userId=#{userId}")
    void updateLastActiveTime(@Param("userId")int userId);


}
