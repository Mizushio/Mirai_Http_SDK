package com.example.mirai_http_sdk.QQData;

import lombok.Data;

@Data
public class data {
    public   int code;
    public String session;
    public String type;
    public sender sender;
    public messageChain[] messageChain;
    public String fromId;
    public String action;
    public String suffix;
    public String target;
    public subject subject;
    public String origin;
    public String current;
    public operator operator;//todo:出大问题
    public String qq;
    public friend friend;
    public Boolean inputting;
    public String from;
    public String to;
    public String durationSeconds;
    public menber invitor;
    public String authorId;
    public String messageId;
    public int time;
    public Boolean isByBot;
    public menber menber;//这很面向对象
    public String honor;
    public String eventId;
    public String groupId;
    public String nick;
    public String message;
    public String kind;
    public client client;
    public args[] args;

}
