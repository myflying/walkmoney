package com.ydys.moneywalk.bean;

public class MessageEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageEvent(String message){
        this.message = message;
    }
}
