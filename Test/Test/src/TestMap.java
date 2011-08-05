import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-4 下午07:53:40
 * @since   jdk1.5
 * 第一次使用MAP这个接口，试试看这东西究竟是怎么用的
 */
public class TestMap {
	private Map<String, String> map;
	/**
	 * 新建一个map，带参数的，不带参数的
	 * */
	public TestMap(HashMap<String, String> hashMap)
	{
		this.map = hashMap;
	}
	public TestMap()
	{
		map  =new HashMap<String, String>();
	}
	/**
	 *对map对象的内容进行初始化，给它赋值
	 * */
	public void initMap(String sourcFile,Map<String,String> map)
	{   
		BufferedReader in =null;
		try {
			in = new BufferedReader(new FileReader(sourcFile));
			String[] mapString = new String[2];
			String aline;
			try {
				do {
				   aline =in.readLine();
				   if(aline!=null)
				   {
				   mapString =aline.split("=");
				   map.put(mapString[0].trim(), mapString[1].trim());
				   }
				   } while (aline!=null);	
			} catch (IOException e) {
				e.printStackTrace();
			}
				                           	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally
		{
			try {
				in.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public void orderMap(Map<String, String> map)
	{
		  Iterator<String> interator = map.keySet().iterator();
		  while (interator.hasNext()) {
			String  key =interator.next();
			System.out.println(key+"的对应值是："+map.get(key));
		}
	}
	public static void main(String[] args) {
		TestMap testMap = new TestMap();
		testMap.initMap("src/mimeMap", testMap.map);
		testMap.orderMap(testMap.map);
		System.out.println("给一个空值，看看map会输出什么null："+testMap.map.get("dengchao"));
	}

}


