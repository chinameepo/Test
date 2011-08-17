package sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 上午10:34:56
 * @since jdk1.5 
 * 获取指定数据库的所有的表 名字
 */
public class TestGetTableName {
	private SQLBean bean;
	private ResultSet resultSet;
	private String dataBaseName;

	public TestGetTableName(String databaseName) {
		this.dataBaseName = databaseName;
		bean = new SQLBean(databaseName);
	}

	/**
	 * 通过执行SQL语句show tables from dataBaseName，获取它下面的所有表的名称
	 */
	public void getTableNames() {
		bean.connect();
		bean.statement();
		bean.query("show tables from " + this.dataBaseName);
		resultSet = bean.getResultSet();
		System.out.println("一共表的行数："+getRows(resultSet));
		try {
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bean.close();
	}

	/**
	 * 获取这个查询结果集的行数。
	 * 
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
			// 注意一定要把游标跳回到最前面
			resultSet.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}
	public static void main(String[] args) {
		TestGetTableName getTableName = new TestGetTableName("deng");
		getTableName.getTableNames();
	}
}
