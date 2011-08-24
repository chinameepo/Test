package com.succez.dengc.handle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.succez.dengc.bean.SQLBean;
import com.succez.dengc.bean.TreeBean;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 下午01:56:50
 * @since   jdk1.6
 * 作用：1，获取数据库名称。2，获取表名称。3，获取字段名称以及内容。
 * 4，将表和数据库，根据javascript的插件dtree的格式组成树形结构，放入list。 
 *  
 */
/**
 * @author Administrator
 * 
 */
public class TreeHandle {
	private ArrayList<TreeBean> list;
	private Map<String, TreeBean> map;
	private String[] database;
	private ResultSet resultSet;
	private ApplicationContext aplcxt = new ClassPathXmlApplicationContext(
			"spring.xml");
	private Logger logger = LoggerFactory.getLogger(getClass());
	private SQLBean bean;

	/**
	 * 生成树的结构
	 * 
	 * @return
	 */
	public ArrayList<TreeBean> genTree() {
		bean = (SQLBean) aplcxt.getBean("sqlBeans");
		list = new ArrayList<TreeBean>();
		map = new HashMap<String, TreeBean>();
		getDataBase();
		return list;
	}

	/**
	 * 获取所有数据库的的名称，将其添加到一个动态数组里面。
	 */
	public void getDataBase() {
		database = getOneClume(null, "show databases");
		for (int i = 0; i < database.length; i++) {
			TreeBean treeBean = (TreeBean) aplcxt.getBean("treeBean");
			treeBean.setName(database[i]);
			// 点击这个数据库的节点，我们不需要它跳转到某个页面，所以就设置成空的。
			list.add(treeBean);
			map.put(database[i], treeBean);
		}
	}

	/**
	 * 获取查询结果集中第一列的所有元素。通过这个函数可以获得数据库名称、数据库下面的表的名称。
	 * 
	 * @param sql是查询的内容
	 *            。datebase就是一个数据库的名称，叫它跳转到这个数据库查询。
	 * @return
	 */
	public String[] getOneClume(String datebase, String sql) {
		String[] resultStrings;
		Connection connection =null;
		Statement statement =null;
		try {
			connection =bean.connect();
			statement =bean.statement(connection);
			if ((datebase != null) && (!"".equals(datebase))) {
				bean.ecxecute(statement,"use  " + datebase);
			}
			// 有可能得不到结果的
			resultSet = bean.query(statement,sql);
			if (resultSet == null)
				return null;
			resultStrings = new String[getRows(resultSet)];
			int i = 0;
			while (resultSet.next()) {
				resultStrings[i] = resultSet.getString(1);
				i++;
			}
			return resultStrings;
		} catch (SQLException e) {
			logger.error(e.toString());
			return null;
		}finally{
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e2) {
				logger.error(e2.toString());
			}
		}
	}

	/**
	 * 获取查询结果集的行数
	 * 
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

	public ArrayList<TreeBean> getList() {
		return list;
	}

	public void setList(ArrayList<TreeBean> list) {
		this.list = list;
	}

	public Map<String, TreeBean> getMap() {
		return map;
	}

}
