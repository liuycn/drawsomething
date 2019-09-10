package com.drawsomething.entity;

/**
 * 绘图主题词汇信息类
 * @author L
 *
 */
public class GameSubject {
	//词汇Id
	private int subjectId;
	//题目
	private String wordInfo;
	//提示信息
	private String additionInfo;
	//该提示信息在页面的聊天框内展示,以"提示1+提示2+……"存储
	private String otherAdditionInfo;
	//该词汇状态，1为可用，0为删除
	private int subjectStatus;
	
	public GameSubject(){
		super();
	}
	public GameSubject(int subjectId,String wordInfo,String additionInfo,String otherAdditionInfo,int subjectStatus){
		super();
		this.setSubjectId(subjectId);
		this.setWordInfo(wordInfo);
		this.setAdditionInfo(additionInfo);
		this.setOtherAdditionInfo(otherAdditionInfo);
		this.setSubjectStatus(subjectStatus);
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getWordInfo() {
		return wordInfo;
	}
	public void setWordInfo(String wordInfo) {
		this.wordInfo = wordInfo;
	}
	public String getAdditionInfo() {
		return additionInfo;
	}
	public void setAdditionInfo(String additionInfo) {
		this.additionInfo = additionInfo;
	}
	public String getOtherAdditionInfo() {
		return otherAdditionInfo;
	}
	public void setOtherAdditionInfo(String otherAdditionInfo) {
		this.otherAdditionInfo = otherAdditionInfo;
	}
	public int getSubjectStatus() {
		return subjectStatus;
	}
	public void setSubjectStatus(int subjectStatus) {
		this.subjectStatus = subjectStatus;
	}
	
	@Override
	public String toString(){
		return "subjectId:"+this.getSubjectId()+",wordInfo:"+this.getWordInfo()+",additionInfo:"+this.getAdditionInfo()+",otherAdditionInfo:"+this.getOtherAdditionInfo()+",subjectStatus:"+this.getSubjectStatus();
	}
	
}
