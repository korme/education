package korme.xyz.education.model.seviceModel;

public class ExcelForAppModel {
    private Integer applicationUniqueNumber;
    private String applyTime;
    private Integer mount;
    private Integer currentBalance;
    private String userName;
    private String remark;
    private String cateName;
    private String accountId;

    public ExcelForAppModel(Integer applicationUniqueNumber, String applyTime, Integer mount, Integer currentBalance, String userName, String remark, String cateName, String accountId) {
        this.applicationUniqueNumber = applicationUniqueNumber;
        this.applyTime = applyTime;
        this.mount = mount;
        this.currentBalance = currentBalance;
        this.userName = userName;
        this.remark = remark;
        this.cateName = cateName;
        this.accountId = accountId;
    }

    public ExcelForAppModel() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getApplicationUniqueNumber() {
        return applicationUniqueNumber;
    }

    public void setApplicationUniqueNumber(Integer applicationUniqueNumber) {
        this.applicationUniqueNumber = applicationUniqueNumber;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getMount() {
        return mount;
    }

    public void setMount(Integer mount) {
        this.mount = mount;
    }

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
