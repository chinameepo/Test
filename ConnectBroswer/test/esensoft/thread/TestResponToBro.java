package esensoft.thread;

import static org.junit.Assert.*;

import java.io.IOException;
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

	@Test
	public void testCutUrl() {
		Logger logger = LoggerFactory.getLogger(TestResponToBro.class);
		ServerSocket serverSocket =null;
		Socket socket =null ;
		try {
			 serverSocket =new ServerSocket(8066);
			 socket = serverSocket.accept();
			 ResponTOBroswer responTOBroswer =new ResponTOBroswer(socket);
			assertEquals("",responTOBroswer.cutUrl(""));
			//测试运行有错误
			assertEquals("", responTOBroswer.cutUrl(null));
			
			assertEquals("",responTOBroswer.cutUrl("GET  HTTP/1.1"));
			assertEquals("c s s.txt",responTOBroswer.cutUrl("GET /c s s.txt HTTP/1.1"));
			assertEquals("aa.png", responTOBroswer.cutUrl("GET /aa.png HTTP/1.1"));
			assertEquals("test.txt", responTOBroswer.cutUrl("GET /test.txt HTTP/1.1"));
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

	@Test
	public void testGetContentType() {
		Logger logger = LoggerFactory.getLogger(TestResponToBro.class);
		ServerSocket serverSocket =null;
		Socket socket =null ;
		try {
			serverSocket =new ServerSocket(8066);
			 socket = serverSocket.accept();
			 ResponTOBroswer responTOBroswer =new ResponTOBroswer(socket);
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType(""));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType(null));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType(" "));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("html"));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("xx.html"));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("xx.tx"));
			 assertEquals("Content-Type: text/html;charset=gb2312",responTOBroswer.getContentType("..."));
			 assertEquals("Content-Type: text/plain",responTOBroswer.getContentType("aa.text"));
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


