package com.succez.dengc.bean; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 下午02:57:31
 * @since jdk1.6 
 *它数据库的连接的操作，只要调用它就可以完成一些基本操作。目前还不打算使用spring注入。
 */
public class SQLBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
    private static ComboPooledDataSource dataSource;
	/**
	 * 我们只需要指定是在用哪个数据库即可。
	 * 
	 * @param dataBaseName
	 */
	public SQLBean(){
	}

	/**
	 * 连接指定的数据库。其实数据库的名字没有，也可以连接成功。
	 */
	public Connection connect() {
		Connection connection =null;
		try {
			connection = dataSource.getConnection();
			logger.info("new conenction:"+connection.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【connect()】"+e.toString());
			return null;
		}
		return connection;
	}

	/**
	 * 获取陈述对象，注意这里的两个参数，TYPE_SCROLL_INSENSITIVE,是可以保证来回滚动，而不是一次性滚动
	 * 之后就不能滚回来了。CONCUR_UPDATABLE，持续更新。
	 */
	public Statement statement(Connection connection) {
		Statement statement =null;
		try {
			if(connection==null)
				return null;
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【statement()】"+e.toString());
			return null;
		}
		return statement;
	}

	public ResultSet  query(Statement statement,String sql) {
		ResultSet resultSet =null;
		try {
			resultSet = statement.executeQuery(sql);
			//return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【 query(String sql)】"+e.toString());
			return null;
		}
		  return resultSet;
	}
	public boolean ecxecute(Statement statement,String sql){
		boolean result=true;
		try {
			result= statement.execute(sql);
		} catch (SQLException e) {
			logger.info("来自方法：SQLBean【ecxecute(String sql】"+e.toString());
		}
		return result;
	}

	public ComboPooledDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
	}
}

