package com.example.connectMates.objects;


public class Response {


    private StatusWrapper statusWrapper;

    private Object data;

    public Response() {
    }

    private Response(String status, int status_code, String status_msg, Object data) {
        statusWrapper=new StatusWrapper(status, status_code, status_msg);
        this.data = data;
    }


    public StatusWrapper getStatusWrapper() {
        return statusWrapper;
    }

    public void setStatusWrapper(StatusWrapper statusWrapper) {
        this.statusWrapper = statusWrapper;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    // Getters and setters

    public class Builder {

        private String status;
        private int status_code;
        private String status_msg;
        private Object data;

        public Builder() {}

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setStatus_code(int status_code) {
            this.status_code = status_code;
            return this;
        }

        public Builder setStatus_msg(String status_msg) {
            this.status_msg = status_msg;
            return this;
        }


        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public Response build() {
            return new Response(status, status_code, status_msg, data);
        }
    }


}

