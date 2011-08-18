package sql;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-16 下午04:15:04
 * @since   jdk1.5
 * 对于sql中的查询语句，我想要知道我可以查询的行数时多少，然后
 * 我也想要知道关于数据库的更多信息.
 * 1，查询数据库。
 * 2，获取字段名字。
 * 3，获取所在的数据库的名字。
 */
public class TestGetFields {
	private SQLBean bean;
	private String dataBaseName;
	private ResultSet resultSet;
	
	public TestGetFields(String dataBaseName){
		this.dataBaseName =dataBaseName;
		bean =new SQLBean(dataBaseName);
	}
	/**
	 * 获得表的字段名称
	 */
	public void getDateMate(){
		try {
			bean.connect();
			bean.statement();
			bean.query("select * from student");
			resultSet =bean.getResultSet();
			ResultSetMetaData metaData =resultSet.getMetaData();
			System.out.println("一共的列数是："+metaData.getColumnCount());
			System.out.println("一共的行数是："+getRows(resultSet));
			System.out.println("第一列的名字是："+metaData.getColumnName(1));
			System.out.println("第2列的名字是："+metaData.getColumnName(2));
			System.out.println("第3列的名字是："+metaData.getColumnName(3));
			System.out.println("所在的数据库是："+metaData.getCatalogName(1));
			getQueryResult(resultSet);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bean.close();
		}
	}
	public void getQueryResult(ResultSet resultSet){
		try {
			while(resultSet.next()){
				System.out.println(resultSet.getString(1)+"   "+resultSet.getString(2)+"   "+resultSet.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取这个查询结果集的行数。
	 * @param resultSet
	 * @return 
	 */
	public int getRows(ResultSet resultSet) {
		if (resultSet == null)
			return 0;
		int row = 0;
		try {
			resultSet.last();
			row = resultSet.getRow();
			//注意一定要把游标跳回到最前面
			resultSet.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
	
	public static void main(String[] args) {
		TestGetFields sqlTest =new TestGetFields("deng");
		sqlTest.getDateMate();
		
	}
}


