package com.example.mirai_http_sdk.QQSDK;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mirai_http_sdk.QQData.QQMassageData;
//import com.example.mirai_http_sdk.Utils.OKHttpUtils;
import com.example.mirai_http_sdk.Utils.OKHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 核心操作类
 * @author Mizushio
 */
@Slf4j
@Service
public class Core {


	/*服务器的地址，http用*/
	public static String ServerUrl="http://localhost.top:8081/";

	/**
	 * 打包文字聊天内容
	 * @return
	 */
	public static HashMap TextMessageChainMaker(String text)
	{
		HashMap messageChain=new HashMap<String,String>();
		messageChain.put("type","Plain");
		messageChain.put("text",text);
		return messageChain;
	}

	/**
	 * 打包图片聊天内容
	 * @return
	 */
	public static HashMap PicMessageChainMaker(String url)
	{
		HashMap messageChain=new HashMap<String,String>();
		messageChain.put("type","Image");
		messageChain.put("url",url);
		return messageChain;
	}
	/**
	 * 高级图文内容解析
	 */
	public static HashMap PicAndTextMessgameChainMaker(String text)
	{
		HashMap messageChain=new HashMap<String,String>();
		if(text.startsWith("[pic=")){
			messageChain.put("type","Image");
			messageChain.put("url",text.substring(text.indexOf("=")+1,text.indexOf("]")));
		}
		else {
		messageChain.put("type","Plain");
		messageChain.put("text",text);}
		return messageChain;
	}


	/**
	 * 发送私聊消息
	 */
	public static void sendPrivateMessages(String target, HashMap messageChain, String mysession){
		OKHttpUtils okHttpUtils=new OKHttpUtils();
		List<JSONObject> listdata=new ArrayList<>();
		listdata.add(new JSONObject(messageChain));
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(listdata));

		HashMap content=new HashMap<String,String>();
		content.put("sessionKey",mysession);
		content.put("target",target);
		content.put("messageChain",listdata);

		JSONObject json = new JSONObject(content);
		okHttpUtils.postString(ServerUrl+"sendFriendMessage",json.toJSONString());
		//http://localhost:8080/sendGroupMessage
		System.out.println("[发送好友消息]"+array.toString()+"——>"+target);

	}

	/**
	 * 处理好友验证事件
	 */
	public static void handlePrivateEvent(QQMassageData qqMassageData, String mysession){
		OKHttpUtils okHttpUtils=new OKHttpUtils();

		HashMap content=new HashMap<String,String>();
		content.put("sessionKey",mysession);
		content.put("eventId",qqMassageData.data.eventId);
		content.put("fromId",qqMassageData.data.fromId);
		content.put("groupId",qqMassageData.data.groupId);
		content.put("operate","1");
		content.put("message","");


		JSONObject json = new JSONObject(content);
		okHttpUtils.postString(ServerUrl+"NewFriendRequestEvent",json.toJSONString());
		System.out.println("[接受好友请求]"+"——>");
		//todo：还没写完
	}

	/**
	 * {
	 *   "sessionKey":"YourSessionKey",
	 *   "eventId":12345678,
	 *   "fromId":123456,
	 *   "groupId":654321,
	 *   "operate":0,
	 *   "message":""
	 * }
	 *
	 */



	/**
	 * 发送群聊消息
	 */
	public static void sendGroupMessages(String target, HashMap messageChain, String mysession)  {

		OKHttpUtils okHttpUtils=new OKHttpUtils();
		List<JSONObject> listdata=new ArrayList<>();
		listdata.add(new JSONObject(messageChain));
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(listdata));

		HashMap content=new HashMap<String,String>();
		content.put("sessionKey",mysession);
		content.put("target",target);
		content.put("messageChain",listdata);

		JSONObject json = new JSONObject(content);
		okHttpUtils.postString(ServerUrl+"sendGroupMessage",json.toJSONString());
		//http://localhost.top:8080/sendGroupMessage
		System.out.println("[发送群消息]"+array.toString()+"——>"+target);
	}





}
