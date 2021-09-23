package com.example.mirai_http_sdk.QQSDK;

import com.example.mirai_http_sdk.QQData.QQMassageData;
import com.example.mirai_http_sdk.QQData.QQMassageDataForFriendReacll;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 食大便了，大人！
 * main
 * @author mizushio
 */
@Service
public class Main {
	public static String mysession;
	public static String getMysession(){
		return mysession;
	}

	/**
	 * 消息解析器
	 */
	public static String MsgResolver(QQMassageData qqMassageData){
		String msg="";
		for (int i=1;i<qqMassageData.data.messageChain.length;i++) {
			if (qqMassageData.data.messageChain[i].type.equals("Image")){
				msg+="[pic="+qqMassageData.data.messageChain[i].url+"]";}
			if (qqMassageData.data.messageChain[i].type.equals("Plain")){
				msg+=qqMassageData.data.messageChain[i].text;}
			if (qqMassageData.data.messageChain[i].type.equals("At")){
				msg+="[@"+qqMassageData.data.messageChain[i].target+"]";
			}

		}
		return msg;
	}


	/**
	 * 收到好友消息与事件
	 */
	public static void receivePrivateMessages(QQMassageData qqMassageData) {

		String msg=MsgResolver(qqMassageData);
		System.out.println("[收到好友消息]"+qqMassageData.data.sender.id+":"+qqMassageData.data.sender.nickname+"->"+msg);
			Core.sendPrivateMessages(qqMassageData.data.sender.id, Core.TextMessageChainMaker(msg), mysession);

	}



	/**
	 * 收到群聊消息
	 *
	 * @param
	 */
	public static void receiveGroupMessages(QQMassageData qqMassageData) throws IOException {

		String msg=MsgResolver(qqMassageData);
		System.out.println("[收到群消息]"+qqMassageData.data.sender.group.id+":"+qqMassageData.data.sender.memberName+"->"+msg);
		if(msg.equals("?")) {
			Core.sendGroupMessages(qqMassageData.data.sender.group.id, Core.TextMessageChainMaker("?"), mysession);
		}
	}

	/**
	 * 收到戳一戳消息
	 */
	public static void receiveNudgeEvent(QQMassageData qqMassageData) throws IOException {
		if(qqMassageData.data.subject.kind.equals("Friend")){
			System.out.println("[收到好友戳一戳]"+qqMassageData.data);
			return;
		}
		else {
			System.out.println("[收到群戳一戳]"+qqMassageData.data);
			return;
		}
	}

	/**
	 * 收到好友请求
	 * @param qqMassageData
	 */
	public static void NewFriendRequestEvent(QQMassageData qqMassageData){
		System.out.println("[收到好友请求]"+qqMassageData.data.fromId);

	}
	/**
	 * 好友撤回消息
	 */
	public static void FriendRecallEvent(QQMassageDataForFriendReacll qqMassageDataForFriendReacll){
		System.out.println("[好友消息撤回]"+qqMassageDataForFriendReacll.data.operator);
	}
	/**
	 * 群消息撤回
	 */
	public static void GroupRecallEvent(QQMassageData qqMassageData){
		System.out.println("[群消息撤回]"+qqMassageData.data.operator.id);
	}
}
