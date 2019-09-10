package com.drawsomething.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.drawsomething.entity.MessageTrans;

/**
 * 将web传来的String数据（实质是JSON）解码为MessageTrans类型，用于com.drawsomething.websocket.DrawingServer.onMessage(MessageTrans)
 * @author L
 *
 */
public class MessageDecoder implements Decoder.Text<MessageTrans>{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
	}

	@Override
	public MessageTrans decode(String s) throws DecodeException {
		System.out.println("信息接收中，调用了decode方法……");
		Object o=(new JsonProcess()).jsonDecode(s,"MESSAGE_TRANS");
		if(o!=null&&o instanceof MessageTrans){
			MessageTrans mt=(MessageTrans)o;
			return mt;
		}else{
			return null;
		}
	}

	@Override
	public boolean willDecode(String s) {
		System.out.println("初步验证startWith:"+s.startsWith("mesTypeTrans",2));
		return s.startsWith("mesTypeTrans",2);
	}

	
}
