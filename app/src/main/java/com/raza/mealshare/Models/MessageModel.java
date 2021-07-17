package com.raza.mealshare.Models;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public  class MessageModel {
    public MessageModel() {
    }

    @Expose
    @SerializedName("type")
    private int type;
    @Expose
    @SerializedName("sender")
    private String sender;
    @Expose
    @SerializedName("receiver")
    private String receiver;
    @Expose
    @SerializedName("content")
    private String content;
    @Expose
    @SerializedName("time")
    private Date time;
    @Expose
    @SerializedName("read")
    private boolean read;
    @Expose
    @SerializedName("product_id")
    private String product_id;

    @Exclude
    private String messageId;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public <T extends MessageModel> T withId(String messageId) {
        this.messageId=messageId;
        return (T) this;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
