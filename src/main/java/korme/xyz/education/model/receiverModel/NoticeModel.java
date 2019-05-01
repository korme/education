package korme.xyz.education.model.receiverModel;

public class NoticeModel {
    //noticeTitle,n.content,n.kidgardenId,n.creatorId,u.headPortrait,u.nickName
    private String noticeTitle;
    private String content;
    private int kidgardenId;
    private int userId;
    private String headPortrait;
    private String nickName;

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

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getKidgardenId() {
        return kidgardenId;
    }

    public void setKidgardenId(int kidgardenId) {
        this.kidgardenId = kidgardenId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
