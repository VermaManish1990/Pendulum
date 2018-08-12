package com.pend.arena;

/**
 * Created by chaudhary on 3/17/2018.
 */

public  abstract class Data<T> {

    Data data;
    Boolean status;
    String statusCode;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
