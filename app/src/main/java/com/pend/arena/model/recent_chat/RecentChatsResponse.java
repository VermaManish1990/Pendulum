package com.pend.arena.model.recent_chat;

import java.util.List;

public class RecentChatsResponse {

     private boolean status;
     private String statusCode;
     private Data Data;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public RecentChatsResponse.Data getData() {
        return Data;
    }

    public void setData(RecentChatsResponse.Data data) {
        Data = data;
    }

    public  class Data{
        public List<ResponseData> responseData ;
        public Boolean hasNextPage;

        public List<ResponseData> getResponseData() {
            return responseData;
        }

        public void setResponseData(List<ResponseData> responseData) {
            this.responseData = responseData;
        }

        public Boolean getHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(Boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }
    }


}
