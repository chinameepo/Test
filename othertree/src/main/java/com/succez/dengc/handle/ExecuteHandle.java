package com.succez.dengc.handle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.succez.dengc.bean.SQLBean;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-23 下午02:39:04
 * @since jdk1.6 执行复杂的SQL语句，如果有结果就返回结果集合就用表格返回，或者返回影响的行数。
 */
public class ExecuteHandle {
	private String sql;
	private String[][] queryResult;
	private SQLBean bean;
	private ApplicationContext aplcxt = new ClassPathXmlApplicationContext(
	"spring.xml");
	
	public ExecuteHandle(String sql) {
		this.sql = sql;
	}

	/**
	 * 执行SQL语句，并且返回二维数组。
	 * 
	 * @return
	 */
	public String[][] exeQery() {
		bean = (SQLBean) aplcxt.getBean("sqlBeans");
		query();
		return queryResult;
	}
	
	/**
	 * 如果是更新语句，就执行更新。
	 * @return
	 */
	@SuppressWarnings("finally")
	public String exeUpdate(){
		bean = (SQLBean) aplcxt.getBean("sqlBeans");
		Connection connection = null;
		Statement statement = null;
		connection = bean.connect();
		statement = bean.statement(connection);
		String countResult ="";
		try {
			statement.executeUpdate(sql);
			countResult ="更新成功，影响行数："+statement.getUpdateCount();
		} catch (SQLException e) {
			return e.toString();
		}finally{
			try {
				statement.close();
				connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			return countResult;
		}
	}

	/**
	 * 执行查询语句，然后将结果集合返回。这里调用一个函数，让结果集变成二维数组。
	 */
	public void query() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		connection = bean.connect();
		statement = bean.statement(connection);
		resultSet= bean.query(statement, sql);
		getResult(resultSet);
	}

	/**
	 * 将查询结果集转换成二维数组。
	 * @param resultSet
	 */
	public void getResult(ResultSet resultSet) {
		ResultSetMetaData metaData;
		try {
			metaData = resultSet.getMetaData();
			int clum = metaData.getColumnCount();
			queryResult =new String[getRow()+1][clum];
			for (int i = 0; i < clum; i++) {
				queryResult[0][i]=metaData.getColumnName(i + 1);
			}
			// 因为第0行被用作字段了，所以我获取内容从第一行开始填装
			int rowCount = 1;
			while (resultSet.next()) {
				for (int i = 0; i < clum; i++) {
					queryResult[rowCount][i]=resultSet.getString(i + 1);
				}
				rowCount++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过select  count * from table，获取结果集合的行数。
	 * @return
	 */
	@SuppressWarnings("finally")
	public int getRow(){
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		connection = bean.connect();
		statement = bean.statement(connection);
		int row =0;
		try {
		   resultSet =statement.executeQuery("select count(*)  "+getTableName(sql)) ;
			while(resultSet.next()){
				row =resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			return row;
		}
	}
	
	/**
	 * 从一个sql查询语句中找到表的名称，select * from table，那么就在from后面的就是表名。
	 * @param sql
	 * @return
	 */
	public String getTableName(String sql){
		String sqlString =sql.toLowerCase();
		int indexOfFrom =sqlString.lastIndexOf("from");
		if(indexOfFrom==-1)
			return "";
		String table =sqlString.substring(indexOfFrom);
		return table;
	}
	
}
