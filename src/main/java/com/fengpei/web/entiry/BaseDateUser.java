package com.fengpei.web.entiry;


public class BaseDateUser extends BaseData {
    private User data;

    public BaseDateUser() {

    }

    public BaseDateUser(User data) {
        this.data = data;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
