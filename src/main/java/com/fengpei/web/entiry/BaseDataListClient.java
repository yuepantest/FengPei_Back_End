package com.fengpei.web.entiry;

import java.util.List;

public class BaseDataListClient extends BaseData{

    private List<Client> data;

    public BaseDataListClient() {

    }

    public BaseDataListClient(Integer code, String msg, List<Client> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public void setData(List<Client> data) {
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
    public static BaseDataListClient error(String msg) {
        return new BaseDataListClient(0, msg, null);
    }

    //当接受到正确是进行success
    public static BaseDataListClient success(List<Client> data) {
        return new BaseDataListClient(1, "success", data);
    }


}
