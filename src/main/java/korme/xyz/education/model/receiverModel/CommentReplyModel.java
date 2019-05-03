package korme.xyz.education.model.receiverModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentReplyModel {
    private Integer userId;
    @NotNull private Integer pId;
    @NotNull private Integer replyUserId;
    @NotBlank private String content;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Integer replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
