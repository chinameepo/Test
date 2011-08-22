package com.succez.dengc.bean; 

	/**
	 * All right resrvered esensoft(2011)
	 * 
	 * @author 邓超 deng.369@gmail.com
	 * @version 1.0,创建时间：2011-8-18 上午08:49:56
	 * @since jdk1.6 一个用来在tTree中表示一个节点的bean。
	 */
public class TreeBean {
	private String name;
	private String[] child;

	public TreeBean( String name) {
		this.name = name;
		String[] test={"aa"};
		child =test;
	}

	public TreeBean() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getChild() {
		return child;
	}

	public void setChild(String[] child) {
		this.child = child;
	}
}
