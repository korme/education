package korme.xyz.education.model;

public class OfficialDynamic {
    private Integer dynamicId;
    private Integer userId;
    private Integer kidgardenId;
    private String headPortrait;
    private String nickName;
    private String userType;
    private Integer classId;
    private String images;
    private Integer transDynamicId;
    private String excerpt;
    private String content;
    private Integer pointNum;
    private Integer browseNum;
    private Integer commentNum;
    private Integer viewStatus;
    private String date;
    private String delState;
    private OfficialDynamic child;


    public OfficialDynamic() {
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public OfficialDynamic getChild() {
        return child;
    }

    public void setChild(OfficialDynamic child) {
        this.child = child;
    }

    public Integer getTransDynamicId() {
        return transDynamicId;
    }

    public void setTransDynamicId(Integer transDynamicId) {
        this.transDynamicId = transDynamicId;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getKidgardenId() {
        return kidgardenId;
    }

    public void setKidgardenId(Integer kidgardenId) {
        this.kidgardenId = kidgardenId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPointNum() {
        return pointNum;
    }

    public void setPointNum(Integer pointNum) {
        this.pointNum = pointNum;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Integer viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDelState() {
        return delState;
    }

    public void setDelState(String delState) {
        this.delState = delState;
    }
}
