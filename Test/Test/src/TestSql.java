import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;



/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-16 下午04:15:04
 * @since   jdk1.5
 * 对于sql中的查询语句，我想要知道我可以查询的行数时多少，然后
 * 我也想要知道关于数据库的更多信息
 */
public class TestSql {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String user="root";
	private String pass="dengchao";
	private String url="jdbc:mysql://localhost:3306/deng";
	private String className="com.mysql.jdbc.Driver";
	private String query ="select * from student";
	
	
	/**
	 *  建立和数据库的连接，操作数据库
	 */
	public void connect2SQL()
	{
		try {
			Class.forName(className);
			connection =DriverManager.getConnection(url,user,pass);
			statement =connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("连接数据库出错");
			e.printStackTrace();
			return ;
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载失败");
			return ;
		}
	}
	public void analysisi(){
		try {
			resultSet.last();
			int row =resultSet.getRow();
			System.out.println("一共是"+row+"行");
			resultSet.beforeFirst();
			while(resultSet.next())
			{
				System.out.println(resultSet.getInt(1)+"   "+resultSet.getString(2)+"   "+resultSet.getString(3));
			}
			resultSet.beforeFirst();
		} catch (SQLException e) {
			System.out.println("从结果集合取出元素出错");
		}
	}
	public void getDateMate(){
		try {
			ResultSetMetaData metaData =resultSet.getMetaData();
			System.out.println("一共的列数是："+metaData.getColumnCount());
			System.out.println("第一列的名字是："+metaData.getColumnName(1));
			System.out.println("所在的数据库是："+metaData.getCatalogName(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getDataBaseInfo(){
		try {
			DatabaseMetaData  metaData=connection.getMetaData();
			System.out.println("分隔符："+metaData.getCatalogSeparator());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		TestSql sqlTest =new TestSql();
		sqlTest.connect2SQL();
		sqlTest.analysisi();
		sqlTest.getDateMate();
		sqlTest.getDataBaseInfo();
	}
}


