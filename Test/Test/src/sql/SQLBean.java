package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 上午10:09:24
 * @since jdk1.5
 *  一个可以连接数据库的bean，一次性注入，永久好用。这个现在只是支持查询的而已，暂时先不用spring注入。
 * 
 */
public class SQLBean {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String user = "root";
	private String pass = "dengchao";
	private String url = "jdbc:mysql://127.0.0.1:3306/" ;
	private String className = "com.mysql.jdbc.Driver";
    private Logger logger =LoggerFactory.getLogger(getClass());
	/**
	 * 我们只需要指定是在用哪个数据库即可。
	 * @param dataBaseName
	 */
	public SQLBean(String dataBaseName) {
		
		this.url = this.url + dataBaseName;
		try {
			Class.forName(className);
			connection = DriverManager.getConnection(url , user, pass);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			logger.error("驱动加载失败");
			return;
		} catch (SQLException e) {
			logger.error("连接数据库出错");
			return;
		}
	}
	public SQLBean(){
		this("");
	}
	public void connect(){
		try {
			connection = DriverManager.getConnection(url , user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void statement(){
		try {
			statement =connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void query(String sql){
		try {
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * getter &&setter*/
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

}
