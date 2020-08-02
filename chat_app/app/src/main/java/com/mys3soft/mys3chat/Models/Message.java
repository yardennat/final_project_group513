package com.mys3soft.mys3chat.Models;


public class Message {

    public String FromMail;
    public String ToMail;
    public String Message;
    public String SentDate;
    public String FriendFullName;

    public int rowid;

    public Message() {
    }

    public Message(String fromMail, String toMail, String message, String sentDate, String friendFullName, int rowid) {
        FromMail = fromMail;
        ToMail = toMail;
        Message = message;
        SentDate = sentDate;
        FriendFullName = friendFullName;
        this.rowid = rowid;
    }

    public String getFromMail() {
        return FromMail;
    }

    public void setFromMail(String fromMail) {
        FromMail = fromMail;
    }

    public String getToMail() {
        return ToMail;
    }

    public void setToMail(String toMail) {
        ToMail = toMail;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSentDate() {
        return SentDate;
    }

    public void setSentDate(String sentDate) {
        SentDate = sentDate;
    }

    public String getFriendFullName() {
        return FriendFullName;
    }

    public void setFriendFullName(String friendFullName) {
        FriendFullName = friendFullName;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }
}
