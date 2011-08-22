package c3p0Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-22 上午09:26:47
 * @since   jdk1.6
 * 一个对数据连接池c3p0的操作
 */
public class TestC3p0 {
	public void test(){
		ComboPooledDataSource source =new ComboPooledDataSource();
		try {
			source.setDriverClass("com.mysql.jdbc.Driver");
			source.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/deng");
			source.setUser("root");
			source.setPassword("dengchao");
			source.setMaxPoolSize(2);
			source.setMaxStatements(20);
			//这是从一个包的类型转化成另外的包的类型
			DataSource dataSource =source;
			Connection connection =dataSource.getConnection();
			Statement statement =connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from student");
			while(resultSet.next()){
				System.out.println(resultSet.getString(2));
			}
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		TestC3p0 test =new TestC3p0();
		test.test();
	}

}


