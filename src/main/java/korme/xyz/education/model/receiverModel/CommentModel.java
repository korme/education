package korme.xyz.education.model.receiverModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentModel {
    private Integer userId;
    @NotNull private Integer pId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
