package com.raza.mealshare.Database.MessagesFiles;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.raza.mealshare.Models.MessageModel;

import java.util.Calendar;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Messages {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String productId;
    public String MessageId;
    public String content;
    public int messageType;
    public String receiverId;
    public String senderId;
    public long time;
    public int readMessages;

    public String ConversationsId;

    public Messages(MessageModel messageModel) {
        Log.i("messages",new Gson().toJson(messageModel));
        this.productId=messageModel.getProduct_id();
        this.MessageId=messageModel.getMessageId();
        this.content=messageModel.getContent();
        this.messageType=messageModel.getType();
        this.receiverId=messageModel.getReceiver();
        this.senderId=messageModel.getSender();
        if (messageModel.getTime()!=null){
            this.time=messageModel.getTime().getTime();
        }else {
            this.time=Calendar.getInstance().getTimeInMillis();
        }
        if (messageModel.isRead()){
            readMessages=1;
        }else {
            readMessages=0;
        }
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(receiverId)){
                ConversationsId=senderId;
            }else {
                readMessages=1;
                ConversationsId=receiverId;
            }
        }else {
            ConversationsId=senderId;
        }
        if (receiverId.equals(senderId)){
            readMessages=1;
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(int readMessages) {
        this.readMessages = readMessages;
    }

    public String getConversationsId() {
        return ConversationsId;
    }

    public void setConversationsId(String conversationsId) {
        ConversationsId = conversationsId;
    }

    public Messages() {

    }
    public Date GetTimestamp(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.getTime();
    }
}
