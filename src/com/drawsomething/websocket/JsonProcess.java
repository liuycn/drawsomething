package com.drawsomething.websocket;


import java.io.IOException;

import com.drawsomething.entity.MessageTrans;
import com.drawsomething.entity.Room;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用于JSON字符串与Object之间的转换
 * @author L
 *
 */
public class JsonProcess {
	
	/**
	 * 将一个Object转换为JSON字符串
	 * @param o 待编码的Object
	 * @return 转换后的JSON字符串
	 */
	public String jsonEncode(Object o){
		ObjectMapper mapper=new ObjectMapper();
		try {
			String jsonStr=mapper.writeValueAsString(o);
			return jsonStr;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.println("jsonEncode方法中异常："+e.getMessage());
			return "";
		}
	}
	
	/**
	 * 将一串JSON字符串转换为对应的Object
	 * @param s 待解码的JSON字符串
	 * @param mesType 指定的转换类型
	 * @return 转换后的Object
	 */
	public Object jsonDecode(String s,String mesType){
		ObjectMapper mapper=new ObjectMapper();
		try {
			switch (mesType) {
			case "MESSAGE_TRANS":
				System.out.println("字符串解码中，case：message");
				MessageTrans mt = mapper.readValue(s, MessageTrans.class);
				return mt;
				//break;
			case "USER_MESSAGE":
				System.out.println("字符串解码中，case：userMessage");
				Room roomInfo=mapper.readValue(s, Room.class);
				return roomInfo;
			default:
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("jsonDecode方法中异常："+e.getMessage());
			return null;
		}
	}
}
