package korme.xyz.education.model;

public class UserTypeModel {
    //userName,nickName,kidgardenId,classId,userType,lastActiveTime
    //todo: model
    private int kidgardenId;
    private int classId;
    private int userType;
    private String userName;
    private String nickName;
    private String lastActiveTime;

    public int getKidgardenId() {
        return kidgardenId;
    }

    public void setKidgardenId(int kidgardenId) {
        this.kidgardenId = kidgardenId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
}
