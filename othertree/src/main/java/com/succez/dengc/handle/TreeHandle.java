package com.succez.dengc.handle; 

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.succez.dengc.bean.SQLBean;
import com.succez.dengc.bean.TreeBean;


/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 下午01:56:50
 * @since   jdk1.6
 * 作用：1，获取数据库名称。2，获取表名称。3，获取字段名称以及内容。
 * 4，将表和数据库，根据javascript的插件dtree的格式组成树形结构，放入list。 
 *  id      ：节点自身的id
    pid     ：节点的父节点的id
    name    ：节点显示在页面上的名称
    url     ：节点的链接地址

 */
public class TreeHandle {
	private ArrayList<TreeBean> list ;
	private Map<String, TreeBean> map;
	private String[]database ;
	private ResultSet resultSet;
	private static int id =1;
	private ApplicationContext aplcxt = new ClassPathXmlApplicationContext("spring.xml");
	private Logger logger =LoggerFactory.getLogger(getClass());
	
	public ArrayList<TreeBean> genTree(){
		list = new ArrayList<TreeBean>();
		map = new HashMap<String, TreeBean>();
		getDataBase();
	    for(int i=0;i<database.length;i++){
			getTable(database[i]);
		}
	    return list;
	}
	
	/**
	 * 获取所有数据库的的名称，将其添加到一个动态数组里面。
	 */
	public void getDataBase(){
		database =getOneClume(null,"show databases");
		for(int i=0;i<database.length;i++){
			TreeBean bean =(TreeBean)aplcxt.getBean("treeBean");
			bean.setId(id);
			bean.setPrentid(0);
			bean.setName(database[i]);
			//点击这个数据库的节点，我们不需要它跳转到某个页面，所以就设置成空的。
			bean.setUrl("");
			list.add(bean);
			map.put(database[i],bean);
			id++;
		}
	}
	


	/**
	 * 获得指定数据下面的所有表名称.获取它们的父亲ID，然后并且给它们编号，存在一个treeBean里面。
	 * @param dataBase
	 */
	public void getTable(String dataBase){
		String[] tables =getOneClume(dataBase, "show tables ");
		TreeBean bean =map.get(dataBase);
		int parentId =bean.getId();
		for(int i=0;i<tables.length;i++){
			TreeBean tableBean =(TreeBean)aplcxt.getBean("treeBean");
			tableBean.setId(id);
			id++;
			tableBean.setPrentid(parentId);
			tableBean.setName(tables[i]);
			//我这么命名的原因，是因为到时候我要从这个Url的请求里面获取数据库的名字和数据库的表名。
			tableBean.setUrl("table.do?database="+dataBase+"&table="+tables[i]);
			list.add(tableBean);
		}
	}
	
	/**
	 * 获取查询结果集中第一列的所有元素。通过这个函数可以获得数据库名称、数据库下面的表的名称。
	 * @param sql是查询的内容。datebase就是一个数据库的名称，叫它跳转到这个数据库查询。
	 * @return 
	 */
	public String[] getOneClume(String datebase,String sql){
		String[] resultStrings;
		SQLBean bean=(SQLBean)aplcxt.getBean("sqlBeans");
		bean.connect();
		bean.statement();
		if((datebase!=null)&&(!"".equals(datebase))){
			bean.ecxecute("use  "+datebase);
		}
		resultSet = bean.query(sql);
		if (resultSet == null)
			return null;
		try {
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
			bean.close();
		}
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
	public ArrayList<TreeBean> getList() {
		return list;
	}

	public void setList(ArrayList<TreeBean> list) {
		this.list = list;
	}
	public static void setId(int id) {
		TreeHandle.id = id;
	}

}

