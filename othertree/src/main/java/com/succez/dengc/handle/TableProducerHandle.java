package com.succez.dengc.handle; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.succez.dengc.bean.SQLBean;



/** 
 * All right Rserved 2011
 * @author 邓超   E-mail: deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-21 下午4:28:52 
 * @since jdk1.6
 *通过一个数据库和表的名称生成表的结果。获得表字段、内容
 */
public class TableProducerHandle {
	private String database;
	private String table;
	private String  all[][];
	/**
	 * 这个表一共有几列
	 */
	private int clum;
	private ResultSet resultSet;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ApplicationContext aplcxt = new ClassPathXmlApplicationContext(
			"spring.xml");
    private SQLBean bean;
	public TableProducerHandle(String database,String table){
		this.database =database;
		this.table =table;
	}

	public String[][] produceContent() {
		bean= (SQLBean) aplcxt.getBean("sqlBeans");
		getTableContent();
		return all;
	}

	
	/**
	 * 获得表的字段名称/内容，然后组装成Ｈｔｍｌ标签。
	 */
	public void getTableContent() {
		Connection connection =null;
		Statement statement =null;
		try {
		     connection=bean.connect();
			 statement=bean.statement(connection);
			if ((database != null) && (!"".equals(database))) {
				bean.ecxecute(statement,"use  " + database);
			}
			resultSet = bean.query(statement,"select * from " + table);
			ResultSetMetaData metaData = resultSet.getMetaData();
			clum = metaData.getColumnCount();
			int row =getRow(resultSet);
			all =new String[row+1][clum];
			for (int i = 0; i < clum; i++) {
				all[0][i]=metaData.getColumnName(i + 1);
			}
			// 因为第0行被用作字段了，所以我获取内容从第一行开始填装
			int rowCount = 1;
			while (resultSet.next()) {
				for (int i = 0; i < clum; i++) {
					all[rowCount][i]=resultSet.getString(i + 1);
				}
				rowCount++;
			}
		} catch (SQLException e) {
			logger.error(e.toString());
		}finally{
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e2) {
				logger.error(e2.toString());
			}
		}
	}
	
	/**
	 * 获取查询结果集的行数。
	 * @param resultSet
	 * @return
	 */
	public int getRow(ResultSet resultSet){
		if(resultSet==null){
			return 0;
		}
		int row =0;
		try {
			resultSet.last();
			row =resultSet.getRow();
			resultSet.beforeFirst();
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		return row;
	}

}
 
