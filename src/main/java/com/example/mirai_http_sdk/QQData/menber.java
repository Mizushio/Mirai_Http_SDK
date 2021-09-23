package com.example.mirai_http_sdk.QQData;

import lombok.Data;

@Data
public class menber {
    public String id;
    public String memberName;
    public String permission;
    public String specialTitle;
    public String joinTimestamp;
    public String lastSpeakTimestamp;
    public String muteTimeRemaining;
    public group group;

}
