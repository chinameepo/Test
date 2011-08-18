package testQuery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sql.SQLBean;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-17 下午05:22:18
 * @since   jdk1.6
 * 类说明
 */
public class TestQuery {
	private SQLBean bean;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ResultSet resultSet;
	private String database;
	
	public TestQuery(String database){
		this.database =database;
		bean = new SQLBean(database);
	}
	
	public void getTableInfo(String sql) {
		bean.connect();
		bean.statement();
		bean.query(sql);
		resultSet =bean.getResultSet();
		seeFields(resultSet);
		logger.info("{}语句执行完毕",sql);
	}

	/**
	 * 获取字段名称
	 * 
	 * @param resultSet
	 */
	public void seeFields(ResultSet resultSet) {
		try {
			// 看字段
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cloumes = metaData.getColumnCount();
			StringBuffer buffer = new StringBuffer();
			for (int i = 1; i <= cloumes; i++) {
				buffer.append(metaData.getColumnName(i) + ",    ");
			}
			logger.info(buffer.toString());
			// 看内容
			while(resultSet.next()){
				StringBuffer buffer2 = new StringBuffer();
				for(int i=1;i<=cloumes;i++){
					buffer2.append(resultSet.getString(i)+",	");
				}
				logger.info(buffer2.toString());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.toString());
			return;
		}
	}
	public String[] getTables(String database){
		String [] result =null;
		bean = new SQLBean(database);
		bean.connect();
		bean.statement();
		bean.query("show tables");
		resultSet =bean.getResultSet();
		int rows  =getRows(resultSet);
		result = new String[rows];
		int i=0;
		try {
		while(resultSet.next()){
				//这里错了，要用while.next()
				result[i]=resultSet.getString(1);
				i++;
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		for(i=0;i<rows;i++){
			System.out.println(result[i]);
		}
		bean.close();
		return result;
	}
	/**
	 * 获取这个查询结果集的行数。
	 * 
	 * @param resultSet
	 * @return
	 */
	@SuppressWarnings("finally")
	public int getRows(ResultSet resultSet) {
		if (resultSet == null)
			return 0;
		   int row=0;
		try {
			 resultSet.last();
			 row = resultSet.getRow();
			 // 注意一定要把游标跳回到最前面
			 resultSet.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return row;
		}
	}
	
	public static void main(String[] args) {
		Logger logger =LoggerFactory.getLogger(TestQuery.class);
		TestQuery query =new TestQuery("deng");
		query.getTableInfo("select * from student;");
		query.getTableInfo("select * from firstview;");
		query.getTableInfo("select * from companyview");
		query.getTables("deng");
		logger.info("getTble ends");
		query.getTables("dengchao");
	}
}


