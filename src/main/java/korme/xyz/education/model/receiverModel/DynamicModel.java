package korme.xyz.education.model.receiverModel;

import korme.xyz.education.service.timeUtil.TimeUtilMPL;
import korme.xyz.education.service.timeUtil.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class DynamicModel {
    //此Model的date在构造时已被注入，若不注入则使用DynamicOrdinaryModel
    private Integer dynamicId;
    private Integer userId;
    private Integer kidgardenId;
    private Integer classId;
    @NotNull private Integer transDynamicId;
    private Integer viewStatus;
    private String content;
    private String excerpt;
    private String date;
    private String Images;


    public DynamicModel() {
        this.date = TimeUtilMPL.NOW();
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
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

    public Integer getTransDynamicId() {
        return transDynamicId;
    }

    public void setTransDynamicId(Integer transDynamicId) {
        this.transDynamicId = transDynamicId;
    }

    public Integer getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Integer viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
