package com.succez.dengc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.succez.dengc.bean.TreeBean;
import com.succez.dengc.handle.ExecuteHandle;
import com.succez.dengc.handle.TableProducerHandle;
import com.succez.dengc.handle.TreeHandle;

/**
 * All right Rserved 2011
 * 
 * @author 邓超 E-mail: deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-21 下午3:03:19
 * @since jdk1.6 
 *所有应答来自网络请求的CONTROLLOR都放在这个类里面。他们独自拦截各自对应的请求，并进行处理。
 */
@Controller
public class TreeController {

	private static ArrayList<TreeBean> list;
	private TreeHandle handle;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/free.do")
	public ModelAndView handleRequest() {
		handle = new TreeHandle();
		list =handle.genTree();
		Map rootMap = new HashMap();
		rootMap.put("tree", list);
		rootMap.put("tables", null);
		return new ModelAndView("template", rootMap);
	}
	/**
	 * 点击某个数据库后，增量展开树形结构
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/database.do")
	public ModelAndView openDatabaseTree(@RequestParam("database")String database){
        String[]tables =handle.getOneClume(database, "show tables");
        handle.getMap().get(database).setChild(tables);
        Map rootMap = new HashMap();
        rootMap.put("tree", list);
        rootMap.put("tables", null);
        return new ModelAndView("template",rootMap);
	}

	/**
	 * 点击可以展开某个数据库下面某个表的结构
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/table.do")
	public ModelAndView tableTree(@RequestParam("database") String database,
			@RequestParam("table") String table) {
		TableProducerHandle tablehandle = new TableProducerHandle(database,
				table);
		String tableContent[][] = tablehandle.produceContent();
		Map rootMap = new HashMap();
		rootMap.put("tree", list);
		rootMap.put("tables", tableContent);
		return new ModelAndView("template", rootMap);
	}
	/**
	 * 返回一个带有输入框，可以输入SQL语句的页面
	 * */
	@RequestMapping("/edit.do")
	public ModelAndView editSQL(){
		return new ModelAndView("sqlinput","affect"," ");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/query.do")
	public ModelAndView query(@RequestParam("sqlString") String sql){
		System.out.println(sql);
		ExecuteHandle handle = new ExecuteHandle(sql);
		String[][] content = null;
		String affectRow="";
		if(sql.trim().substring(0,6).equals("select")){
			content=handle.exeQery();
			Map rootMap = new HashMap();
			rootMap.put("tree", list);
			rootMap.put("tables", content);
			rootMap.put("affect", affectRow);
			return new ModelAndView("template",rootMap);
		}else {
			affectRow=handle.exeUpdate();
			Map rootMap = new HashMap();
			rootMap.put("affect", affectRow);
			return new ModelAndView("sqlinput",rootMap);
		}
	}
}
