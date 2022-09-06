package self.study.spring.cloud.common.edm;

public enum ResponseCode {

    OK(200, "Success"),
    // server error code
    DB_ERROR(501, ""),
    GATEWAY_ERROR(504, "Gateway error");

    int code;
    String msg;

    ResponseCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
