package com.example.mirai_http_sdk.QQData;

import lombok.Data;

@Data
public class nodeList {
    public String senderId;
    public String time;
    public String senderName;
    public String messageId;
    public messageChain[] messageChains;
}
