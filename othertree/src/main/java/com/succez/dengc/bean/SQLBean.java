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
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private Logger logger = LoggerFactory.getLogger(getClass());
    private ComboPooledDataSource dataSource;
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
	public void connect() {
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【connect()】"+e.toString());
			return;
		}
	}

	/**
	 * 获取陈述对象，注意这里的两个参数，TYPE_SCROLL_INSENSITIVE,是可以保证来回滚动，而不是一次性滚动
	 * 之后就不能呢个滚回来了。CONCUR_UPDATABLE，持续更新。
	 */
	public void statement() {
		try {
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【statement()】"+e.toString());
			return;
		}
	}

	public ResultSet  query(String sql) {
		try {
			resultSet = statement.executeQuery(sql);
			return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("来自方法：SQLBean【 query(String sql)】"+e.toString());
			return null;
		}
	}
	public void  ecxecute(String sql){
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			logger.info("来自方法：SQLBean【ecxecute(String sql】"+e.toString());
		}
	}

	public void close() {
		try {
			if(resultSet!=null)
			resultSet.close();
			if(statement!=null)
			statement.close();
			if(connection!=null)
			connection.close();
		} catch (Exception e) {
			logger.error("来自方法：SQLBean【close()】"+e.toString());
			return;
		}
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
	}
}

