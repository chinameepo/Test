package dom4j;

import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-24 下午02:42:13
 * @since   jdk1.6
 * dom4j的一个测试类，操作dom4j的一些元素。
 */

public class Testdom {
	
	/**
	 * 漫无目的的测试
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void test(){
		//一个管道流，用一个流的方式，把xml文件读出来
		SAXReader reader = new SAXReader();
		try {
			Document document =reader.read(new File("./src/dom4j/pom.xml"));
			//获得根节点
			Element root =document.getRootElement();
			List<Element> rootList =root.content();
			Element dependencies =root.element("dependencies");
			System.out.println(dependencies.toString());
		/*	注意啊这里是elements，多了一个S，获得的是一个结果集。取得父节点下遍历名为"的所有子节点.
		 *  我这里是在遍历，获得节点dependencies下面的所有子节点。因为这些节点都被dependencies保卫了
		 *  所以通过root是获取不到它的。
		 * */
			List<Element> dependency =dependencies.elements("dependency");
			for(Iterator iterator =dependency.iterator();iterator.hasNext();){
				Element element =(Element) iterator.next();
				System.out.println(element.toString());
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 新建一个xml文件。
	 */
	public Document  creatDocuent(){
		//这个莫非是在创建一个临时文件？嗯哼？
		Document document =DocumentHelper.createDocument();
		Element root =document.addElement("root");
		//root下面添加节点
		Element userElement =root.addElement("user");
		userElement.addAttribute("name", "deng");
		userElement.addAttribute("sex", "man");
		userElement.addText("ss");
		
		Element pass =root.addElement("pass");
		pass.addAttribute("string", "deng");
		pass.addAttribute("encode", "man");
		return document;
	}
	public void writeXml(){
		Document document =creatDocuent();
		//先测试一个，如果没有意外输出的是user ,man
		Element root=document.getRootElement();
		Element userElement =root.element("user");
		System.out.println(userElement.getName());
		System.out.println(userElement.attributeValue("sex"));
		try {
			FileWriter outFileWriter = new FileWriter("testdom.xml");
			System.out.println("the output stream :"+outFileWriter.toString());
			document.write(outFileWriter);
			outFileWriter.flush();
			outFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Testdom testdom = new Testdom();
		testdom.test();
		testdom.writeXml();
	}

}


