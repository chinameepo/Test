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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-3 上午08:44:10
 * @since jdk1.5 
 * 对response类的测试类，请注意。如果你要用自己的文件拖进来做测试，请做好备份。
 */
public class TestResponse {

	/**
	 * 从一个输入流里面截取url的方法的测试用例
	 * 
	 * @TODO 如果第一行有人输出了N多的转义字符///n\\\''''“”“等等，这些如何处理？
	 * */
	@Test
	public final void testGetUrlFromStream() throws FileNotFoundException,
			UnsupportedEncodingException {
		Response response = new Response(null);
		/* 文件的第一行是GET /test.txt HTTP/1.1 */
		assertEquals("test.txt",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-txt.txt"))));
		/* 文件的第一行是GET /aa.png HTTP/1.1 */
		assertEquals("aa.png",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-png.txt"))));
		/* 文件的第一行是GET /Temp/test HTTP/1.1，是一个目录 */
		assertEquals("temp/test",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-dir.txt"))));
		/* 文件的第一行是GET /c%20s%20s.txt HTTP/1.1 */
		assertEquals("c s s.txt",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-decode%.txt"))));
		/* 文件的第一行是GET /c+s+s.txt HTTP/1.1 */
		assertEquals("temp/c s s.txt",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-decode+.txt"))));
		// 这行肯定错了！
		/*
		 * assertEquals("c s s%%.txt",
		 * response.getUrlFromStream((InputStream)(new
		 * FileInputStream("c://Temp/test/request-%%.txt"))));
		 */
		/* 文件的第一行是GET / HTTP/1.1 */
		assertEquals("index.html",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-home.txt"))));
		/* 文件第一行是GET /fwfi39489@#@#^&&**(()()<>?>>LLL:" HTTP/1.1 */
		assertEquals("fwfi39489@#@#^&&**(()()<>?>>lll:",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-complex.txt"))));
		// 空值
		assertEquals("", response.getUrlFromStream(null));
		/* 这是个空文件，里面没有内容 */
		assertEquals("",
				response.getUrlFromStream((InputStream) (new FileInputStream(
						"./Temp/fileForTestCase/request-empty.txt"))));
	}

	/**
	 * 具体的写入头文件、写入文件的，这个被测试方法的子方法已经测试过了
	 * 这个类主要是测试：1，错误的地址是否会返回404页面。2，应答报文头是否写进了文件
	 * 先使用bufferedreader的realine方法出报文头，再接着比较以后的内容。
	 * 不管是文本文件，还是图片，一视同仁
	 * @throws IOException
	 * */
	@Test
	public final void testFileToBrowser() throws IOException {
		Response response = new Response(null);
		OutputStream out = null;
		BufferedReader resultFileReader = null;
		BufferedReader sourceFileReader = null;
		
		try {
			String sourceName = "test.txt";
			File file = new File("./Temp/fileForTestCase/", sourceName);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/test-all.txt"));
			response.fileToBrowser(sourceName, out);
			String sourceString, outString;
			resultFileReader = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/test-all.txt"));
			sourceFileReader = new BufferedReader(new FileReader(
					"./Temp/fileForTestCase/test.txt"));
			// 验证报文头是否正确
			assertEquals("HTTP/1.1 200 OK", resultFileReader.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", resultFileReader.readLine());
			assertEquals("Content-Length: " + file.length(), resultFileReader.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(sourceName),
					resultFileReader.readLine());
			assertEquals("Cache-Control: private", resultFileReader.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					resultFileReader.readLine());
			assertEquals("Connection: Keep-Alive", resultFileReader.readLine());
			assertEquals("", resultFileReader.readLine());
			// 验证文件内容是否正确
			while (((sourceString = sourceFileReader.readLine()) != null)
					&& ((outString = resultFileReader.readLine()) != null)) {
				assertEquals(sourceString, outString);
			}
		} finally {
			try {
				out.close();
				resultFileReader.close();
				sourceFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			String sourceName = "test.html";
			File file = new File("./Temp/fileForTestCase/", sourceName);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/test-all.html"));
			response.fileToBrowser(sourceName, out);
			String sourceString, outString;
			resultFileReader = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/test-all.html"));
			sourceFileReader = new BufferedReader(new FileReader(
					"./Temp/fileForTestCase/test.html"));
			// 验证报文头是否正确
			assertEquals("HTTP/1.1 200 OK", resultFileReader.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", resultFileReader.readLine());
			assertEquals("Content-Length: " + file.length(), resultFileReader.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(sourceName),
					resultFileReader.readLine());
			assertEquals("Cache-Control: private", resultFileReader.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					resultFileReader.readLine());
			assertEquals("Connection: Keep-Alive", resultFileReader.readLine());
			assertEquals("", resultFileReader.readLine());
			// 验证文件内容是否正确
			while (((sourceString = sourceFileReader.readLine()) != null)
					&& ((outString = resultFileReader.readLine()) != null)) {
				assertEquals(sourceString, outString);
			}
		} finally {
			try {
				out.close();
				resultFileReader.close();
				sourceFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//图片的，虽然把报文头一块写进去，图片肯定是坏了，可是里面的内容读取还是没问题的。
		try {
			String sourceName = "bb.jpg";
			File file = new File("./Temp/fileForTestCase/", sourceName);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/bb-all.jpg"));
			response.fileToBrowser(sourceName, out);
			String sourceString, outString;
			resultFileReader = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/bb-all.jpg"));
			sourceFileReader = new BufferedReader(new FileReader(
					"./Temp/fileForTestCase/bb.jpg"));
			// 验证报文头是否正确
			assertEquals("HTTP/1.1 200 OK", resultFileReader.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", resultFileReader.readLine());
			assertEquals("Content-Length: " + file.length(), resultFileReader.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(sourceName),
					resultFileReader.readLine());
			assertEquals("Cache-Control: private", resultFileReader.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					resultFileReader.readLine());
			assertEquals("Connection: Keep-Alive", resultFileReader.readLine());
			assertEquals("", resultFileReader.readLine());
			// 验证文件内容是否正确
			while (((sourceString = sourceFileReader.readLine()) != null)
					&& ((outString = resultFileReader.readLine()) != null)) {
				assertEquals(sourceString, outString);
			}
		} finally {
			try {
				out.close();
				resultFileReader.close();
				sourceFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 给一个不存在的url，查看是否结果就是404页面
		try {
			String sourceName = "t1e2s4t.txt";
			File file = new File("./Temp/fileForTestCase/", sourceName);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/error404.html"));
			response.fileToBrowser(sourceName, out);
			String sourceString, outString;
			resultFileReader = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/error404.html"));
			sourceFileReader = new BufferedReader(new FileReader(
					"./Temp/fileForTestCase/404.html"));
			// 验证报文头是否正确
			assertEquals("HTTP/1.1 200 OK", resultFileReader.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", resultFileReader.readLine());
			// 这里用404页面的长度,来替代这个不存在文件的长度
			assertEquals("Content-Length: "
					+ (new File("./Temp/fileForTestCase/404.html")).length(),
					resultFileReader.readLine());
			// 用404页面的类型来代替不存在文件的类型
			assertEquals(
					"Content-Type: "
							+ response
									.getMIMEtype("./Temp/fileForTestCase/404.html"),
					resultFileReader.readLine());
			assertEquals("Cache-Control: private", resultFileReader.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					resultFileReader.readLine());
			assertEquals("Connection: Keep-Alive", resultFileReader.readLine());
			assertEquals("", resultFileReader.readLine());
			// 验证文件内容是否正确
			while (((sourceString = sourceFileReader.readLine()) != null)
					&& ((outString = resultFileReader.readLine()) != null)) {
				assertEquals(sourceString, outString);
			}
		} finally {
			try {
				out.close();
				resultFileReader.close();
				sourceFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//在windows系统下，文件的名字是不区分大小写的，测试是否正确。如果不正确那么，得到的应该是404文件
		try {
			String sourceName = "TEsT.txt";
			File file = new File("./Temp/fileForTestCase/", sourceName);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/testCase-all.txt"));
			response.fileToBrowser(sourceName, out);
			String sourceString, outString;
			resultFileReader = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/testCase-all.txt"));
			sourceFileReader = new BufferedReader(new FileReader(
					"./Temp/fileForTestCase/TEsT.txt"));
			// 验证报文头是否正确
			assertEquals("HTTP/1.1 200 OK", resultFileReader.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", resultFileReader.readLine());
			assertEquals("Content-Length: " + file.length(), resultFileReader.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(sourceName),
					resultFileReader.readLine());
			assertEquals("Cache-Control: private", resultFileReader.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					resultFileReader.readLine());
			assertEquals("Connection: Keep-Alive", resultFileReader.readLine());
			assertEquals("", resultFileReader.readLine());
			// 验证文件内容是否正确
			while (((sourceString = sourceFileReader.readLine()) != null)
					&& ((outString = resultFileReader.readLine()) != null)) {
				assertEquals(sourceString, outString);
			}
		} finally {
			try {
				out.close();
				resultFileReader.close();
				sourceFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 确定这个被测试的方法的参数out,对象肯定不为空.url,file都是不为空的。查看发送的报文头是否正确！
	 * 
	 * @throws IOException
	 * */
	@Test
	public final void testSendHead() throws IOException {
		Response response = new Response(null);
		OutputStream out = null;
		BufferedReader br = null;
		//查看文本文件
		try {
			String url = "./Temp/fileForTestCase/test.txt";
			File file = new File(url);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/test-head.txt"));
			response.sendHead(url, out, file);
			br = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/test-head.txt"));
			assertEquals("HTTP/1.1 200 OK", br.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", br.readLine());
			assertEquals("Content-Length: " + file.length(), br.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(url),
					br.readLine());
			assertEquals("Cache-Control: private", br.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					br.readLine());
			assertEquals("Connection: Keep-Alive", br.readLine());
			assertEquals("", br.readLine());
			assertEquals(null, br.readLine());
		} finally {
			try {
				out.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        //查看网页文件
		try {
			String url = "./Temp/fileForTestCase/index.html";
			File file = new File(url);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/html-head.txt"));
			response.sendHead(url, out, file);
			br = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/html-head.txt"));

			assertEquals("HTTP/1.1 200 OK", br.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", br.readLine());
			assertEquals("Content-Length: " + file.length(), br.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(url),
					br.readLine());
			assertEquals("Cache-Control: private", br.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					br.readLine());
			assertEquals("Connection: Keep-Alive", br.readLine());
			assertEquals("", br.readLine());
			assertEquals(null, br.readLine());
		} finally {
			try {
				out.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        //查看图片文件
		try {
			String url = "./Temp/fileForTestCase/aa.png";
			File file = new File(url);
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/png-head.txt"));
			response.sendHead(url, out, file);
			br = new BufferedReader(new FileReader(
					"./Temp/fileGeneraByTest/png-head.txt"));

			assertEquals("HTTP/1.1 200 OK", br.readLine());
			assertEquals("Date: Thu, 21 Jul 2011 01:45:42 GMT", br.readLine());
			assertEquals("Content-Length: " + file.length(), br.readLine());
			assertEquals("Content-Type: " + response.getMIMEtype(url),
					br.readLine());
			assertEquals("Cache-Control: private", br.readLine());
			assertEquals("Expires: Thu, 21 Jul 2011 01:45:42 GMT",
					br.readLine());
			assertEquals("Connection: Keep-Alive", br.readLine());
			assertEquals("", br.readLine());
			assertEquals(null, br.readLine());
		} finally {
			try {
				out.close();
				br.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 我认为即便不是文本文档，也可以用字符串来对文件的内容进行比较 前面，待测试的方法已经验证，这个函数的out肯定是不为空的
	 * 
	 * @throws IOException
	 * */
	@Test
	public final void testSendFile() throws IOException {
		Response response = new Response(null);
		OutputStream out = null;
		
		// 文本文档
		try {
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/test-result.txt"));
			File file = new File("./Temp/fileForTestCase/test.txt");
			response.sendFile(out, file);
			
			assertEquals(readFile("./Temp/fileForTestCase/test.txt"),
					readFile("./Temp/fileGeneraByTest/test-result.txt"));
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 文本文档,名字改成大写和小写混在在一起，看是否正确
		try {
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/test-result.txt"));
			File file = new File("./Temp/fileForTestCase/TEsT.txt");
			response.sendFile(out, file);
			
			assertEquals(readFile("./Temp/fileForTestCase/tEST.txt"),
					readFile("./Temp/fileGeneraByTest/test-result.txt"));
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 图片
		try {
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/aa-result.png"));
			File file = new File("./Temp/fileForTestCase/aa.png");
			response.sendFile(out, file);
			
			assertEquals(readFile("./Temp/fileForTestCase/aa.png"),
					readFile("./Temp/fileGeneraByTest/aa-result.png"));
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
          //.jpg图片
		try {
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/bb-result.jpg"));
			File file = new File("./Temp/fileForTestCase/bb.jpg");
			response.sendFile(out, file);
			assertEquals(readFile("./Temp/fileForTestCase/bb.jpg"),
					readFile("./Temp/fileGeneraByTest/bb-result.jpg"));
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//混合类型，将图片读取，用一个一个文本文档存储。
		try {
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/bb-result.txt"));
			File file = new File("./Temp/fileForTestCase/bb.jpg");
			response.sendFile(out, file);
			assertEquals(readFile("./Temp/fileForTestCase/bb.jpg"),
					readFile("./Temp/fileGeneraByTest/bb-result.txt"));
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 网页
		try {
			out = (OutputStream) (new FileOutputStream(
					"./Temp/fileGeneraByTest/index-result.html"));
			File file = new File("./Temp/fileForTestCase/index.html");
			response.sendFile(out, file);
			assertEquals(readFile("./Temp/fileForTestCase/index.html"),
					readFile("./Temp/fileGeneraByTest/index-result.html"));
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * GetMIMEtpye()的测试方法，目前不能解决的问题是，如果用户输入url中就包含了很多“”号，或者/n/t之类的转义字符 如何处理？
	 */
	@Test
	public final void testGetMIMEtpye() {
		Response response = new Response(null);
		// 图片类型
		assertEquals("image/jpeg", response.getMIMEtype("a!@#$!%^&*%HRa.jpg"));
		assertEquals("image/jpeg",
				response.getMIMEtype("a$&*@@!!%^';;';&#$^#$#^@a.jpg"));
		assertEquals("image/jpeg",
				response.getMIMEtype("xW@@xFERTGE$----=-=YYW%Q.jpeg"));
		assertEquals("image/png",
				response.getMIMEtype("?klopf%%%%%%%efw jo.png"));
		assertEquals("image/gif",
				response.getMIMEtype("fei@@@@202-=t394nvi4-+-5+  -.gif"));
		// 文字类型
		assertEquals("text/xml",
				response.getMIMEtype("!!@!@$ASDDFFＥＲＧＥＡ　　.xml"));
		assertEquals("text/plain", response.getMIMEtype("sss....txt"));
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("sss. . . .t   x  t"));
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("Test . j            a       v     a   "));
		assertEquals("text/plain", response.getMIMEtype("c s s s s---ss....c"));
		assertEquals("text/plain",
				response.getMIMEtype("feffazff fewf fwe@#%@^#$.cpp"));
		assertEquals("text/plain", response.getMIMEtype("Test.java"));
		// 在后面留一些空格
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("test.java    "));
		assertEquals("text/html;charset=gb2312", response.getMIMEtype("inde x .h           "));
		// 在前面留一些空格
		assertEquals("text/plain",
				response.getMIMEtype("                        Test.java"));
		assertEquals("text/plain", response.getMIMEtype("cpp_h)(942334).txt"));
		assertEquals("text/plain", response.getMIMEtype("index.h"));
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("index.html"));
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("index.htm"));
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("index. h t m l"));
		assertEquals("text/html;charset=gb2312",
				response.getMIMEtype("index . h t m l  "));
		// 空类型
		assertEquals("text/html;charset=gb2312", response.getMIMEtype(""));
		assertEquals("", response.getMIMEtype(null));
	}

	/**
	 * 为了测试方法服务的方法，读取一个文件的内容，不管是文件还是图片，我们都用字符串返回 。
	 * 不设置成同步方法的话 ，文件可能会在读取过程中损坏
	 * 
	 * @param String
	 *            filePath 文件的路径
	 */
	public synchronized String readFile(String filePath) {
		InputStream in = null;
		StringBuilder builder = new StringBuilder();
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			try {
				in = (InputStream) (new FileInputStream(file));
				int len;
				byte[] buffer = new byte[512];
				while ((len = in.read(buffer)) != -1) {
					builder.append(new String(buffer, 0, len, "UTF-8"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
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
