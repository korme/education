package korme.xyz.education.common.response;

public enum RespCode {
    SUCCESS(0, "请求成功"),
    WRONG(1, "请求错误"),
    WARN_ENPTY(3,"所选内容为空"),
    ERROR_INPUT(4,"输入内容有误"),
    ERROR_SESSION(5,"用户未登录或Sisson已过期"),
    ERROR_USER(6,"用户错误"),
    ERROR_NETWORK(7,"网络错误");


    private Integer code;
    private String msg;

    RespCode(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
