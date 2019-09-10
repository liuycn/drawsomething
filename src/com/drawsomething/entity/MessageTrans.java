package com.drawsomething.entity;

/**
 * 用于webSocket传输信息
 * @author L
 *
 */
public class MessageTrans {
	//信息类型标记
	private String mesTypeTrans;
	//信息内容
	private String mesContentTrans;
	
	public MessageTrans(){
		super();
	}
	public MessageTrans(String mesTypeTrans,String mesContentTrans){
		super();
		this.setMesTypeTrans(mesTypeTrans);
		this.setMesContentTrans(mesContentTrans);
	}
	
	/**
	 * 获取当前信息类型
	 * @return 类型信息
	 */
	public String getMesTypeTrans() {
		return mesTypeTrans;
	}
	public void setMesTypeTrans(String mesTypeTrans) {
		this.mesTypeTrans = mesTypeTrans;
	}
	/**
	 * 获取当前信息内容
	 * @return 具体内容
	 */
	public String getMesContentTrans() {
		return mesContentTrans;
	}
	public void setMesContentTrans(String mesContentTrans) {
		this.mesContentTrans = mesContentTrans;
	}
	@Override
	public String toString(){
		return "mesTypeTrans是："+this.getMesTypeTrans()+",mesContentTrans是："+this.getMesContentTrans();
	}
	
}
