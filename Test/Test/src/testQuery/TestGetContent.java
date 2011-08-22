package testQuery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 下午05:49:35
 * @since   jdk1.6
 * 获取一个表里面的内容。
 */
public class TestGetContent {
	    private String database;
	    private String table;
	    /**
	     * 这个表一共有几行几列
	     */
	    private int clum;
	    private int row;
	    /**
	     * 别人发过来的请求，要求看我的数据库表的内容，我就通过这个url来获取我要的数据库名字和表名字。
	     */
	    private String url = "/newtree/deng==student.action";
	    /**
	     * 所有的字段我先保存在这里面
	     */
	    private String [] fields;
	    private String [][] content;
	    private ResultSet resultSet;
	    private Logger logger = LoggerFactory.getLogger(getClass());
	    

		/**
		 * 这样可以获得数据库名字，表名字。其实最关键的是url中包含了==号。
		 * @param url
		 * @return
		 */
		public void getDataBaseInfo(){
			int key = url.lastIndexOf('/');
			String keyString = url.substring(key + 1);
			String[] infoStrings = keyString.split("==");
			database =infoStrings[0];
			int indexOfDot =infoStrings[1].indexOf('.');
			table =infoStrings[1].substring(0,indexOfDot);
		}

		/**
		 * 获取查询结果集的行数
		 * @param resultSet
		 * @return
		 */
		private int getRows(ResultSet resultSet) {
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
		/**
		 * 获得表的字段名称
		 */
		public void getDateMate(){
			try {
				SQLBean bean = new SQLBean(database);
				bean.connect();
				bean.statement();
				if((database!=null)&&(!"".equals(database))){
					bean.ecxecute("use  "+database);
				}
				resultSet =bean.query("select * from "+table);
				ResultSetMetaData metaData =resultSet.getMetaData();
				clum=metaData.getColumnCount();
				row =getRows(resultSet);
				fields =new String[clum];
				content = new String[row][clum];
				int rowCount =0;
				while(resultSet.next()){
					for(int i=0;i<clum;i++){
						fields[i]=metaData.getColumnName(i+1);
						content[rowCount][i]=resultSet.getString(i+1);
					}
		            rowCount++;
				}
				resultSet.beforeFirst();
				bean.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void print(){
			StringBuffer buffer1 = new StringBuffer();
			for(int i=0;i<clum;i++){
				buffer1.append(fields[i]+",	");
			}
			System.out.println(buffer1.toString());
			for(int i=0;i<row;i++){
				StringBuffer buffer = new StringBuffer();
				for(int j=0;j<clum;j++){
					buffer.append(content[i][j]+",	");
				}
				System.out.println(buffer);
			}
		}
		public static void main(String[] args) {
			TestGetContent getContent = new TestGetContent();
			getContent.getDataBaseInfo();
			getContent.getDateMate();
			getContent.print();
		}
}


