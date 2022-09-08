package self.study.spring.cloud.common.dto.resp;

import self.study.spring.cloud.common.edm.ResponseCode;

public class ResponseResult<T> {

    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseResult<T> commonResult(int code, String msg, T data){
        return new ResponseResult<>(code, msg, data);
    }

    public static <T> ResponseResult<T> commonResult(ResponseCode responseCode, T data){
        return new ResponseResult<>(responseCode.getCode(), responseCode.getMsg(), data);
    }

    public static <T> ResponseResult<T> commonResult(ResponseCode responseCode, Throwable throwable){
        return new ResponseResult<>(responseCode.getCode(), throwable.getMessage(), null);
    }

    public static <T> ResponseResult<T> commonResult(ResponseCode responseCode, String msg){
        return new ResponseResult<>(responseCode.getCode(), msg, null);
    }
}
