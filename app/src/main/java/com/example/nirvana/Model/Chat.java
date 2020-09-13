package com.example.nirvana.Model;

public class Chat {
    private String sender;
    private String reciever;
    private String message;
    private String type;

    public Chat(String sender, String reciever, String message,String type) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.type=type;
    }

    public Chat() {

    }

    public String getSender() {
        return sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
