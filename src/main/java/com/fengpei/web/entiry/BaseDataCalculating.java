package com.fengpei.web.entiry;

public class BaseDataCalculating extends BaseData{

    private CalculateData data;

    public BaseDataCalculating() {

    }

    public CalculateData getData() {
        return data;
    }

    public void setData(CalculateData data) {
        this.data = data;
    }

    public BaseDataCalculating(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
