package korme.xyz.education.model;

public class ArticleModel {
    private int articleId;
    private int pointNum;
    private int browseNum;
    private int commentNum;
    private String imgUrl;
    private String articleTitle;
    private String content;
    private String createTime;
    private String headPortrait;
    private String nickName;

    public ArticleModel() {//todo:获取官方图片昵称
        this.headPortrait="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555998062050&di=ae54dd7b8a19505db3616e6ce1995193&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn10119%2F81%2Fw428h453%2F20190225%2F67ba-htptaqe3925135.jpg";
        this.nickName="官方";
    }

    public int getArticleId() {
        return articleId;
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

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
