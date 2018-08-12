package com.pend.arena.api;

public class APIError {


    Error error;
    Boolean status;
    String statusCode;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
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

    public class Error{

        String messageCode;
        String message;
        String additionalMessage;

        public String getMessageCode() {
            return messageCode;
        }

        public void setMessageCode(String messageCode) {
            this.messageCode = messageCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getAdditionalMessage() {
            return additionalMessage;
        }

        public void setAdditionalMessage(String additionalMessage) {
            this.additionalMessage = additionalMessage;
        }
    }

}
