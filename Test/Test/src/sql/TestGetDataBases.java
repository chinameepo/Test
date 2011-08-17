package sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 上午10:22:01
 * @since jdk1.5 获取所有的数据库的名字的一个测试，实际上就是执行命令：show databases 获取这个用户所能使用的所有的数据库的名称。
 */
public class TestGetDataBases {
	private SQLBean bean;
	private ResultSet reSet;
    
	/**
	 * 获取所有的数据库的名称。
	 */
	public void getDataBase() {
		bean = new SQLBean("deng");
		bean.connect();
		bean.statement();
		bean.query("show databases");
		reSet = bean.getResultSet();
		System.out.println("一共行数：" + getRows(reSet));
		try {
			while (reSet.next()) {
				System.out.println(reSet.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bean.close();
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
		TestGetDataBases getDataBases = new TestGetDataBases();
		getDataBases.getDataBase();
	}
}
