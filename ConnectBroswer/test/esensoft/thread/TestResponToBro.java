package esensoft.thread;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-1 下午03:03:01
 * @since   jdk1.5
 * TestResponToBroser类的测试类
 */
public class TestResponToBro extends TestCase{

	/**
	 * 对于字符串截断函数的测试
	 * */
	@Test
	public void testCutUrl() {
		Logger logger = LoggerFactory.getLogger(TestResponToBro.class);
		ServerSocket serverSocket =null;
		Socket socket =null ;
		try {
			 serverSocket =new ServerSocket(8066);
			 socket = serverSocket.accept();
			 Response responTOBroswer =new Response(socket);
			assertEquals("",responTOBroswer.cutUrl(""));
			//截断为空的字符串，测试运行有错误
			assertEquals("", responTOBroswer.cutUrl(null));
			assertEquals("index.html",responTOBroswer.cutUrl("GET / HTTP/1.1"));
			assertEquals("c s s.txt",responTOBroswer.cutUrl("GET /c s s.txt HTTP/1.1"));
			assertEquals("aa.png", responTOBroswer.cutUrl("GET /aa.png HTTP/1.1"));
			assertEquals("test.txt", responTOBroswer.cutUrl("GET /test.txt HTTP/1.1"));
			//注意这里输入空格，使用的是“%20”，而不是“20%”！
			assertEquals("c s s.txt",responTOBroswer.cutUrl("GET /c%20s%20s.txt HTTP/1.1"));
			assertEquals("c s s.txt",responTOBroswer.cutUrl("GET /c+s+s.txt HTTP/1.1"));
			//这一行有问题，这个怎么搞啊？如果要用decoder的话，这些@%号就出错了，如果不用的话，空格就出错了！
			assertEquals("!!!@#%$@%#^#^$&^^$##@",responTOBroswer.cutUrl("GET /!!!@#%$@%#^#^$&^^$##@ HTTP/1.1"));
			assertEquals("....................",responTOBroswer.cutUrl("GET /.................... HTTP/1.1"));
			
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException");
		} catch (IOException e) {
			logger.error("IOExcptpion with socket1");
		}
		finally{
			try {
				serverSocket.close();
				socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 对于获取报文函数的测试
	 * */
	@Test
	public void testGetUrlFromStream()
	{
		Logger logger = LoggerFactory.getLogger(TestResponToBro.class);
		ServerSocket serverSocket =null;
		Socket socket =null ;
		try {
			serverSocket =new ServerSocket(8066);
			 socket = serverSocket.accept();
			 Response responTOBroswer =new Response(socket);
			 BufferedReader socketiStream = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			 
			 
			 assertEquals("", responTOBroswer.getUrlFromStream(null));
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对于获取MIME类型函数的测试
	 * */
	@Test
	public void testGetContentType() {
		Logger logger = LoggerFactory.getLogger(TestResponToBro.class);
		ServerSocket serverSocket =null;
		Socket socket =null ;
		try {
			serverSocket =new ServerSocket(8066);
			 socket = serverSocket.accept();
			 Response responTOBroswer =new Response(socket);
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType(""));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType(null));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType(" "));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("html"));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("xx.html"));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("xx.tx"));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("..."));
			 assertEquals("Content-Type: text/plain",responTOBroswer.getContentType("aa.text"));
			 assertEquals("Content-Type: image/jpeg",responTOBroswer.getContentType("xx.jpg"));
			 assertEquals("Content-Type: image/gif",responTOBroswer.getContentType("xx.gif"));
			 assertEquals("Content-Type: image/png",responTOBroswer.getContentType("xx.png"));
		}  catch (UnknownHostException e) {
			logger.error("UnknownHostException");
		} catch (IOException e) {
			logger.error("IOExcptpion with socket init");
		}finally{
			try {
				serverSocket.close();
				socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}


