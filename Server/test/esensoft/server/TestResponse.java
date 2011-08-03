package esensoft.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
	@Test
	public final void testGetUrlFromStream() throws FileNotFoundException, UnsupportedEncodingException {
		Response response =new Response(null);
		assertEquals("test.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-txt.txt"))));
		assertEquals("aa.png", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-png.txt"))));
		assertEquals("", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-empty.txt"))));
		assertEquals("Temp/test", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-dir.txt"))));
		assertEquals("c s s.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-decode%.txt"))));
		assertEquals("Temp/c s s.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-decode+.txt"))));
		//这行肯定错了！
		/*assertEquals("c s s%%.txt", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-%%.txt"))));*/
		assertEquals("index.html", 
				response.getUrlFromStream((InputStream)(new FileInputStream("c://Temp/test/request-home.txt"))));
		//捕获异常，空值异常！
		assertEquals("", response.getUrlFromStream(null));			
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

	@Test
	public final void testFileToBrowser() {
		fail("Not yet implemented"); // TODO
	}
	@Test
	public final void testGetMIMEtpye() {
		Response response = new Response(null);
		assertEquals("image/jpeg",response.getMIMEtpye("a!@#$!%^&*%HRa.jpg"));
		assertEquals("image/jpeg",response.getMIMEtpye("a$&*@@!!%^';;';&#$^#$#^@a.jpg"));
		assertEquals("image/jpeg",response.getMIMEtpye("xW@@xFERTGE$----=-=YYW%Q.jpeg"));
		assertEquals("image/png",response.getMIMEtpye("?klopf%%%%%%%efw jo.png"));
		//这个问题从哪里来的？
		assertEquals("image/gif",response.getMIMEtpye("fei@@@@202-=t394nvi4-+-5+  -.gif"));
		assertEquals("text/xml",response.getMIMEtpye("!!@!@$ASDDFFＥＲＧＥＡ　　.xml"));
		assertEquals("text/plain",response.getMIMEtpye("sss....txt"));
		assertEquals("text/plain",response.getMIMEtpye("c s s s s---ss....c"));
		assertEquals("text/plain",response.getMIMEtpye("feffazff fewf fwe@#%@^#$.cpp"));
		assertEquals("text/plain",response.getMIMEtpye("Test.java"));
		assertEquals("text/plain",response.getMIMEtpye("cpp_h)(942334).txt"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye("index.html"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye("index.htm"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye("index.h"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye("index. h t m l"));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye("index . h t m l  "));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye(""));
		assertEquals("text/html;charset=gb2312",response.getMIMEtpye(null));
	}

}


