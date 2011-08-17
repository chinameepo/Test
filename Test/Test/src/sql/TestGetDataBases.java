package sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 上午10:22:01
 * @since   jdk1.5
 * 获取所有的数据库的名字的一个测试，实际上就是执行命令：show databases
 */
public class TestGetDataBases {
	private SQLBean bean;
	private ResultSet reSet;
	public void getDataBase(){
		bean = new SQLBean("deng");
		bean.connect();
		bean.statement();
		bean.query("show databases");
		reSet =bean.getResultSet();
		System.out.println("一共行数："+getRows(reSet));
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
	public int getRows(ResultSet resultSet){
		if (resultSet==null)
			return 0;
		int row=0;
		try {
			resultSet.last();
		    row =resultSet.getRow();
			resultSet.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
	public static void main(String[] args) {
		TestGetDataBases getDataBases =new TestGetDataBases();
		getDataBases.getDataBase();
	}
}


