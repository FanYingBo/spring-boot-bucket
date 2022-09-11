package self.study.spring.cloud.common.edm;

import self.study.spring.cloud.common.dto.resp.ResponseResult;

public enum ResponseCode {

    OK(200, "Success"),
    REQUEST_PARAM_ERROR(417, "Request Param Exception"),
    // server error code
    DB_ERROR(501, "Database Operation Exception"),
    GATEWAY_ERROR(504, "Gateway error"),
    TOO_MANY_REQUEST(508, "TOO MANY REQUEST"),
    REQUEST_TIME_OUT(506, "Request Time Out");

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

    public <T> ResponseResult<T> responseResult(T data){
        return this.responseResult(this.getCode(), this.getMsg(), data);
    }

    public <T> ResponseResult<T> responseResult(String msg, T data){
        return this.responseResult(this.getCode(), msg, data);
    }

    public <T> ResponseResult<T> responseResult(int code, String msg, T data){
        return ResponseResult.commonResult(code, msg, data);
    }
}
