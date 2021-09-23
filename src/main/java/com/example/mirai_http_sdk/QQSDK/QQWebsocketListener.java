package com.example.mirai_http_sdk.QQSDK;

import com.alibaba.fastjson.JSONObject;
import com.example.mirai_http_sdk.QQData.QQMassageData;
import com.example.mirai_http_sdk.QQData.QQMassageDataForFriendReacll;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @Description: 配置websocket后台客户端
 */
@Service
public class QQWebsocketListener {

    public int hassession=0;

    WebSocketClient webSocketClient;



    @Bean
    public WebSocketClient webSocketClient() {

        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost.top:8081/all?qq=11111"),new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("[websocket] 连接成功");
                    hassession=0;
                }
                @SneakyThrows
                @Override
                public void onMessage(String messageget) {
                String message=messageget;
                try{
                      QQMassageData qqMassageData= JSONObject.parseObject(message,QQMassageData.class);
                      if (hassession==0) {
                          hassession=1;
                          Main.mysession=qqMassageData.data.session;
                          System.out.println("[登录session]"+qqMassageData.data.session);
                          return;
                      }
                     if (qqMassageData.data.type.equals("GroupMessage")){
                          Main.receiveGroupMessages(qqMassageData);//群消息
                      }
                     else if (qqMassageData.data.type.equals("FriendMessage")){
                            Main.receivePrivateMessages(qqMassageData);//好友消息
                      }
                     else if (qqMassageData.data.type.equals("NudgeEvent")){
                          Main.receiveNudgeEvent(qqMassageData);//收到戳一戳
                      }
                     else if (qqMassageData.data.type.equals("NewFriendRequestEvent")){
                          Main.NewFriendRequestEvent(qqMassageData);//收到好友请求
                      }

                     else if (qqMassageData.data.type.equals("GroupRecallEvent")){

                         Main.GroupRecallEvent(qqMassageData);//群撤回消息
                      }
                     else if(qqMassageData.data.type.equals("GroupAllowAnonymousChatEvent")){
                         Main.receiveGroupMessages(qqMassageData);//匿名消息，不常用，所以先没写
                      }
                     else if (qqMassageData.data.type.equals("MemberJoinEvent")){

                      }
                     else {System.out.println("[未定义解析类型]"+message);}

                  }
                  catch (Exception e){
                      //请不要修改这个bug，本代码依靠此bug运行（好久以前就想这样打一次了）
                      if (JSONObject.parseObject(JSONObject.parseObject(message).getString("data")).getString("type").equals("FriendRecallEvent"))
                      {
                          QQMassageDataForFriendReacll qqMassageDataForFriendReacll= JSONObject.parseObject(message,QQMassageDataForFriendReacll.class);
                          Main.FriendRecallEvent(qqMassageDataForFriendReacll);//好友撤回消息
                          //todo：这边有问题，接收不到根本，构造函数的时候有问题应该是，待解决
                          //todo:临时解决方案
                      }
                  }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("[websocket] 退出连接");

                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("[websocket] 连接错误="+ex);
                }




            };
            webSocketClient.connectBlocking();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void sends(String msg,WebSocketClient webSocketClient){
        webSocketClient.send(msg);
    }

}
