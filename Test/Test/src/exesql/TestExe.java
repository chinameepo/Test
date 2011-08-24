package exesql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-22 下午05:26:05
 * @since   jdk1.6
 * 在不知道要执行什么语句的时候，看看会有什么结果。
 */
public class TestExe {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String user = "root";
	private String pass = "dengchao";
	private String url = "jdbc:mysql://127.0.0.1:3306/deng" ;
	private String className = "com.mysql.jdbc.Driver";
	boolean resultSetIsAvailable;
	boolean moreResultsAvailable;
	public void initial(){
		try {
			Class.forName(className);
			connection =DriverManager.getConnection(url,user,pass);
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void exe(String sql){
		try {
			//返回的是一个布尔值，如果为真的话，就有查询集合，假的话，有更新。
			resultSetIsAvailable =statement.execute(sql,Statement.NO_GENERATED_KEYS);
			if(resultSetIsAvailable){
				resultSet =statement.getResultSet();
				while(resultSet.next()){
					System.out.println(resultSet.getString(1));
				}
			}
			else{
				System.out.println("更新的行数："+statement.getUpdateCount());
			}
			//如果有查询结果，就打印。也可能是多个查询结果集合。.
			//moreResultsAvailable =statement.getMoreResults();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
				statement.close();
				if(resultSet!=null)
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void batchExe(){
		try {
			statement.addBatch("insert into student(Id,name,company) values(28,\"yaoming\",\"esen\");");
			int [] array =statement.executeBatch();
			for(int i=0;i<array.length;i++){
				System.out.println(i);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TestExe exe =new TestExe();
		exe.initial();
		//exe.exe("insert into student(Id,name,company) values(16,\"yaoming\",\"esen\");select * from student");
	   // exe.exe("select * from student;select * from student;");
		//exe.batchExe();
		exe.exe("desc student");
	}

}


