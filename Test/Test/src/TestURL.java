import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;



/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-1 下午03:41:24
 * @since   jdk1.5
 * url中，涉及到地址，会把空格转换成20%，这个类是测试是否有解决方法可行的
 * 
 */
public class TestURL {
   private String url;
   public TestURL(String urlString)
   {
	   this.url =urlString;
   }
   public void seeTrim() throws URISyntaxException
   {
	   System.out.println(TestURL.class.getResource("").toURI().getPath());
	   System.out.println(url);
	   try {
	        System.out.println(URLEncoder.encode("Hello World","UTF-8"));
	        System.out.println(URLDecoder.decode("Hello+World","UTF-8"));
	        System.out.println(URLDecoder.decode("Hello%20World","UTF-8"));
	        System.out.println(URLDecoder.decode(" c%","UTF-8"));
	        System.out.println(URLDecoder.decode(" c%20s%20s.txt","UTF-8"));
	        System.out.println(URLDecoder.decode("@%%%%!!!@@@@30100","UTF-8"));
	  
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
   }
   /**
    * 把异常跑出去看看是什么结果呢
 * @throws UnsupportedEncodingException */
   public void throException() throws UnsupportedEncodingException
   {
	   
	        System.out.println(URLEncoder.encode("Hello World","UTF-8"));
	        System.out.println(URLDecoder.decode("Hello+World","UTF-8"));
	        System.out.println(URLDecoder.decode("Hello%20World","UTF-8"));
	        System.out.println(URLDecoder.decode(" c%","UTF-8"));
	        System.out.println(URLDecoder.decode("c%20s%20s.txt","UTF-8"));
	        System.out.println(URLDecoder.decode("@%%%%!!!@@@@30100","UTF-8"));
   }
   public void testFile(String name)
   {
	  try {
		InputStream inputStream  =(InputStream)(new FileInputStream(name));
		byte [] buff = new byte[1024];
		int len;
		while((len=inputStream.read(buff))!=-1)
		{
			System.out.write(buff,0,len);
		}
		inputStream.close();
		System.out.println("over1");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(IOException e){
		e.printStackTrace();
	}
	  
   }
   public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException
   ,IllegalArgumentException{
	TestURL testURL = new TestURL("c x x x .txt");
	/*testURL.seeTrim();
	String name  =URLDecoder.decode("c:\\c%20s%20s.txt","UTF-8");
	testURL.testFile(name);
	System.out.println("name is:"+name);
	testURL.testFile("c://css.txt");*/
	testURL.throException();
}
	
}



