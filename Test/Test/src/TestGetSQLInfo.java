import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-16 下午06:10:36
 * @since   jdk1.5
 * 我要尝试着用DatabaseMetaData 和ResultSetDatabaseMetaData 这两个类来获取数据库所有信息
 * 1，获取数据库的版本号。
 * 2，获取驱动版本号。
 * 3，获取登陆的用户名。
 * 4，获取所有的表和视图。
 */
public class TestGetSQLInfo {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String user="root";
	private String pass="dengchao";
	
	/**
	 * 因为数据库在本地，所以这么做
	 */
	private String serverName ="127.0.0.1:3306/deng";
	private String url="jdbc:mysql://"+serverName;
	private String className="com.mysql.jdbc.Driver";
	public TestGetSQLInfo() {
		// TODO Auto-generated constructor stub
	}
	public void setConnection(){
		try {
			Class.forName(className);
			connection =DriverManager.getConnection(url,user,pass);
			if(connection!=null){
				System.out.println("连接数据库成功！");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获得指定数据库下面的所有的 表的名字
	 */
	public void getTable(){
		setConnection();
		try {
			DatabaseMetaData databaseMetaData =connection.getMetaData();
			System.out.println("数据库名称是："+databaseMetaData.getDatabaseProductName());   //获取数据库名称 
			System.out.println("数据库版本是："+databaseMetaData.getDatabaseProductVersion());   //获取数据库版本号 
			System.out.println("数据库驱动是："+databaseMetaData.getDriverName());   //获取JDBC驱动器名称 
			System.out.println("驱动版本是："+databaseMetaData.getDriverVersion());   //获取驱动器版本号 
			System.out.println("数据库登陆用户是："+databaseMetaData.getUserName());   //获取登录用户名 
			System.out.println("url是："+databaseMetaData.getURL()); 
			System.out.println("指定的容器是："+connection.getCatalog());
			String types[] ={"TABLE","VIEW"};
			resultSet  = databaseMetaData.getTables(null, null,"%", types);
			resultSet.last();
			System.out.println("这个数据库表个数：："+resultSet.getRow());
			resultSet.beforeFirst();
			while(resultSet.next()){
				  String tableName    = resultSet.getString(3);
			      String tableCatalog = resultSet.getString(1);
			      String tableSchema  = resultSet.getString(2);
			      System.out.println(tableName+","+tableCatalog+","+tableSchema);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
				resultSet.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	/**
	 * 获取数据库中某个表的所有数据结构
	 */
	public void getTableConstruct(String table){
		
	}
	
	/**
	 * 获取该数据库中所有的数据库的名字
	 */
	public void getDataBases(){
		setConnection();
		try {
			statement =connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.close();
				statement.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	public static void main(String[] args) {
		TestGetSQLInfo sqlInfo = new TestGetSQLInfo();
		sqlInfo.getTable();
	}
}


