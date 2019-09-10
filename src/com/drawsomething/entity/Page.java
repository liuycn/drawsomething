package com.drawsomething.entity;

/**
 * 删除页面类，用于分页查询
 * @author L
 *
 */
public class Page {
	//当前页数
	private int currentPage;
	//总页数
	private int totalPage;
	//单位记录条数(条数/页)
	private int unitCount;
	//总记录条数
	private int totalCount;
	//当前页的第一条记录在DB中的位置
	private int startPosition;
	
	public Page(int currentPage,int unitCount,int totalCount){
		this.setCurrentPage(currentPage);
		this.setUnitCount(unitCount);
		this.setTotalCount(totalCount);
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * 通过总记录数/每页记录条数得到总页数
	 * @return 总页数
	 */
	public int getTotalPage() {
		System.out.println("计算总页数");
		if(this.getTotalCount()==0){
			return 1;//无数据情况下，将总页数改为1
		}else{
			totalPage=getTotalCount()/getUnitCount();
			return (getTotalCount()%getUnitCount()==0)?totalPage:totalPage+1;
		}
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getUnitCount() {
		return unitCount;
	}
	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * 获取当前页的第一条记录在DB中的位置
	 * @return
	 */
	public int getStartPosition() {
		startPosition= (getCurrentPage()-1)*getUnitCount();
		return startPosition;
	}
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	
	@Override
	public String toString(){
		return "当前页："+this.getCurrentPage()+",总页数："+this.getTotalPage()+",起始点："+this.getStartPosition()+",总记录："+this.getTotalCount();
	}
}
