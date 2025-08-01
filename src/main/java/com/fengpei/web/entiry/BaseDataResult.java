package com.fengpei.web.entiry;

public class BaseDataResult extends BaseData{

    private Object data;

    public BaseDataResult() {
    }

    public BaseDataResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                "," + data.toString() +
                '}';
    }
    // 当接受到错误时进行error
    public static BaseDataResult error(String msg) {
        return new BaseDataResult(0, msg);
    }

    //当接受到正确是进行success
    public static BaseDataResult success(Client data) {
        return new BaseDataResult(1, "success");
    }


}
