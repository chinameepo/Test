package esensoft.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import ch.qos.logback.core.joran.action.NewRuleAction;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-3 上午08:44:10
 * @since   jdk1.5
 * 对response类的测试类
 */
public class TestResponse {
	/**
	 * 从一个输入流里面截取url的方法的测试用例
	 * @Todo 如果第一行有人输出了N多的转义字符///n\\\''''“”“等等，这些如何处理？
	 * */
	@Test
	public final void testGetUrlFromStream() throws FileNotFoundException, UnsupportedEncodingException {
		Response response =new Response(null);
		/*文件的第一行是GET /test.txt HTTP/1.1*/
		assertEquals("test.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-txt.txt"))));
		/*文件的第一行是GET /aa.png HTTP/1.1*/
		assertEquals("aa.png", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-png.txt"))));
		/*文件的第一行是GET /Temp/test HTTP/1.1，是一个目录*/
		assertEquals("Temp/test", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-dir.txt"))));
		/*文件的第一行是GET /c%20s%20s.txt HTTP/1.1*/
		assertEquals("c s s.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-decode%.txt"))));
		/*文件的第一行是GET /c+s+s.txt HTTP/1.1*/
		assertEquals("Temp/c s s.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-decode+.txt"))));
		//这行肯定错了！
		/*assertEquals("c s s%%.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-%%.txt"))));*/
		assertEquals("index.html", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-home.txt"))));
	  /*文件第一行是GET /fwfi39489@#@#^&&**(()()<>?>>LLL:" HTTP/1.1*/	
		assertEquals("fwfi39489@#@#^&&**(()()<>?>>LLL:", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-complex.txt"))));
		//空值
		assertEquals("", response.getUrlFromStream(null));	
		/*这是个空文件*/
		assertEquals("", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-empty.txt"))));
	}
	/**
	 * 确定这个被测试的方法的参数out,sourcname,file对象肯定不为空
	 * */
	@Test
	public final void testFileToBrowser() {
		Response response = new Response(null);
		
	}
	/**
	 * 确定这个被测试的方法的参数out,对象肯定不为空.查看发送的报文头是否正确！
	 * @throws IOException 
	 * */
	@Test
	public final void testSendHead() throws IOException
	{
		OutputStream  out=null;
		BufferedReader br =null;
		Response response = new Response(null);
		try{
			String url ="c://test.txt";
			File file = new File(url);
			out =(OutputStream)(new FileOutputStream("c://test-head.txt"));
			response.sendHead(url, out, file);
			 br =new BufferedReader(new FileReader("c://test-head.txt")) ;
				
			assertEquals("HTTP/1.1 200 OK", br.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT",br.readLine());
			assertEquals("Content-Length: " + file.length(),br.readLine());
			assertEquals("Content-Type: "+response.getMIMEtype(url), br.readLine());
			assertEquals("Cache-Control: private", br.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",br.readLine());
			assertEquals("Connection: Keep-Alive", br.readLine());
			assertEquals("", br.readLine());
			assertEquals(null, br.readLine());
		}finally{
			try {
				out.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try{
			String url ="c://index.html";
			File file = new File(url);
			out =(OutputStream)(new FileOutputStream("c://html-head.txt"));
			response.sendHead(url, out, file);
			 br =new BufferedReader(new FileReader("c://html-head.txt")) ;
				
			assertEquals("HTTP/1.1 200 OK", br.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT",br.readLine());
			assertEquals("Content-Length: " + file.length(),br.readLine());
			assertEquals("Content-Type: "+response.getMIMEtype(url), br.readLine());
			assertEquals("Cache-Control: private", br.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",br.readLine());
			assertEquals("Connection: Keep-Alive", br.readLine());
			assertEquals("", br.readLine());
			assertEquals(null, br.readLine());
		}finally{
			try {
				out.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		try{
			String url ="c://aa.png";
			File file = new File(url);
			out =(OutputStream)(new FileOutputStream("c://png-head.txt"));
			response.sendHead(url, out, file);
			 br =new BufferedReader(new FileReader("c://png-head.txt")) ;
				
			assertEquals("HTTP/1.1 200 OK", br.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT",br.readLine());
			assertEquals("Content-Length: " + file.length(),br.readLine());
			assertEquals("Content-Type: "+response.getMIMEtype(url), br.readLine());
			assertEquals("Cache-Control: private", br.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",br.readLine());
			assertEquals("Connection: Keep-Alive", br.readLine());
			assertEquals("", br.readLine());
			assertEquals(null, br.readLine());
		}finally{
			try {
				out.close();
				br.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
	}
	@Test
	public final void testSendFile()
	{
		
	}
	/**
	 * GetMIMEtpye()的测试方法，目前不能解决的问题是，如果用户输入url中就包含了很多“”号，或者/n/t之类的转义字符
	 * 如何处理？*/
	@Test
	public final void testGetMIMEtpye() {
		Response response = new Response(null);
		//图片类型
		assertEquals("image/jpeg",response.getMIMEtype("a!@#$!%^&*%HRa.jpg"));
		assertEquals("image/jpeg",response.getMIMEtype("a$&*@@!!%^';;';&#$^#$#^@a.jpg"));
		assertEquals("image/jpeg",response.getMIMEtype("xW@@xFERTGE$----=-=YYW%Q.jpeg"));
		assertEquals("image/png",response.getMIMEtype("?klopf%%%%%%%efw jo.png"));
		assertEquals("image/gif",response.getMIMEtype("fei@@@@202-=t394nvi4-+-5+  -.gif"));
		//文字类型
		assertEquals("text/xml",response.getMIMEtype("!!@!@$ASDDFFＥＲＧＥＡ　　.xml"));
		assertEquals("text/plain",response.getMIMEtype("sss....txt"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtype("sss. . . .t   x  t"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtype("Test . j            a       v     a   "));
		assertEquals("text/plain",response.getMIMEtype("c s s s s---ss....c"));
		assertEquals("text/plain",response.getMIMEtype("feffazff fewf fwe@#%@^#$.cpp"));
		assertEquals("text/plain",response.getMIMEtype("Test.java"));
		//在后面留一些空格
		assertEquals("text/plain",response.getMIMEtype("Test.java                        "));
		//在前面留一些空格
		assertEquals("text/plain",response.getMIMEtype("                        Test.java"));
		assertEquals("text/plain",response.getMIMEtype("cpp_h)(942334).txt"));
		assertEquals("text/plain",response.getMIMEtype("index.h"));
		assertEquals("text/plain",response.getMIMEtype("inde x .h           "));
		assertEquals("text/html;charset=gb2312",response.getMIMEtype("index.html"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtype("index.htm"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtype("index. h t m l"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtype("index . h t m l  "));
		//空类型
		assertEquals("text/html;charset=gb2312",response.getMIMEtype(""));
		assertEquals("",response.getMIMEtype(null));
	}
	/**
	 * 为了测试方法服务的方法，读取一个文件的内容，不管是文件还是图片，我们都用字符串返回
	 * @param String filePath 文件的路径*/
	public String readFile(String filePath)
	{
		InputStream in =null;
		StringBuilder builder = new StringBuilder();
		File file = new File(filePath);
		if(file.exists()&&file.isFile())
		{
			try {
				in=(InputStream)(new FileInputStream(file));
				int len;
				byte[] buffer = new byte[512];
				while((len=in.read(buffer))!=-1)
				{
					builder.append(new String (buffer,0,len,"UTF-8"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				try {
					in.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return builder.toString();
	}
}


