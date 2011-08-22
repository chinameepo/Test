package com.succez.dengc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.succez.dengc.bean.TreeBean;
import com.succez.dengc.handle.TableProducerHandle;
import com.succez.dengc.handle.TreeHandle;

/**
 * All right Rserved 2011
 * 
 * @author 邓超 E-mail: deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-21 下午3:03:19
 * @since jdk1.6 类说明
 */
@Controller
public class TreeController {

	private ArrayList<TreeBean> list;
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@RequestMapping("/free.do")
	public ModelAndView handleRequest() {
		TreeHandle handle = new TreeHandle();
		handle.setId(1);
		list =handle.genTree();
		Map rootMap = new HashMap();
		rootMap.put("tree", list);
		rootMap.put("tables", null);
		return new ModelAndView("template", rootMap);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/table.do")
	public ModelAndView dataBaseTree(@RequestParam("database") String database,
			@RequestParam("table") String table) {
		TableProducerHandle tablehandle = new TableProducerHandle(database,
				table);
		String tableContent[][] = tablehandle.produceContent();
		Map rootMap = new HashMap();
		rootMap.put("tree", list);
		rootMap.put("tables", tableContent);
		return new ModelAndView("template", rootMap);
	}
	
}
