package com.drawsomething.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.drawsomething.entity.MessageTrans;

/**
 * 将MessageTrans类型的数据编码为String类型用于传输（实质是JSON）
 * @author L
 *
 */
public class MessageEncoder implements Encoder.Text<MessageTrans>{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
	}

	@Override
	public String encode(MessageTrans mt) throws EncodeException {
		System.out.println("信息准备发送，调用了encode方法……");
		String mtStr=new JsonProcess().jsonEncode(mt);
		System.out.println("mtStr:"+mtStr);
		return mtStr.equals("")?"":mtStr;
	}

}
