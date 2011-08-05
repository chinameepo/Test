package com.succez.dengc.serv;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-2 下午07:12:36
 * @since   jdk1.5
 * 浏览器应答类，通过获得的socket对象，利用socket对象的IO流和浏览器通信。
 */
public class Response implements Runnable {
	/*
	 * 这个socket对象必须要从Serve监听对象传过来，和浏览器交互的IO流都是通过它来建立，
	 * 通过这个对象的输入流来获取url，通过这个对象的输出流来向浏览器返回内容。 不可缺少，必须要有
	 */
	private Socket socket;
	/* 服务器的默认根目录，从Serve监听对象中获得 ，可以缺省 */
	private String root;
    private Logger logger = LoggerFactory.getLogger(Response.class);
   
	public Response(Socket response, String root) {
		this.socket = response;
		this.root = root;
	} 
	/**
	 * 这个是系统盘，最好不要将根目录放在这里
	 * */
	public Response(Socket response) {
		this(response, "c:\\");
	}
	/**
	 * 核心方法，规定该线程的执行内容，通过浏览器的输入流得到请求报文头，从中截取文件Url,
	 * 如果文件存在，读取文件，将内容传输到浏览器。不存在，返回404文件
	 * 
	 * @exception IOException
	 * */
	public void run() {
			OutputStream out = null;
			InputStream socketiStream = null;
			try {
				socketiStream = socket.getInputStream();
				String sourceName;
				sourceName = root+getUrlFromStream(socketiStream);
				try {
					out = socket.getOutputStream();
					fileToBrowser(sourceName, out);
				} catch (IOException e) {
					logger.error("输出流对象新建的时候出错！程序终止！,");
					return;
				}
			} catch (IOException e) {
				logger.error("程序运行中出现错误，程序终止！");
				return;
			} finally {
				try {
					out.close();
					socketiStream.close();
					socket.close();
				} catch (IOException e) {
					logger.error("文件流在关闭的时候出现错误！，不能正常关闭");
					return;
				}
			}
	}
	
	/**
	 * 从socket中的输入流，获取请求报文所有内容，如果处理不好，就会在读取报文头的时候卡死，直到等待超时
	 * @return String
	 * @param BufferedReader
	 *            socket获得的输入流，由它获取请求报文
	 * @throws UnsupportedEncodingException ，在用户输入的url中，某个%号后面没有16进制数的话，会抛出此异常，不管它
	 * @exception IOException
	 * */
	public String getUrlFromStream(InputStream inputStream) throws UnsupportedEncodingException {
		if(inputStream==null)
			return "";
		String requestString = getHttpHead(inputStream);
		if ("".equals(requestString))
			return "";
	    /* 截断报文，获取url。请求报文第一行的格式一般都是 GET /urlcontent**** HTTP/1.X*/
		String url  = requestString.substring(5,requestString.lastIndexOf("HTTP/1.")).trim();
		/* 如果对方输入的页面是空的，例如http://localhost:8080/,跳至首页 */
		if ("".equals(url))
		{
			url = "index.html";
			return url;
		}
		/*空格经过浏览器UTF-8编码处理后会变成%20传过来,要进行解码还原*/
		requestString =URLDecoder.decode(url,"UTF-8");
		logger.info("文件名是：{}", url);
		return url;
		}
	
	/**
	 * 获取请求报文所有内容。如果说在这里卡死了，等待超时之后再去运行后面的程序就已经毫无意义了
	 * 所以这里一定要保证不能卡死！
	 * @param inputStream
	 * @return String
	 */
	public String getHttpHead(InputStream inputStream) {
		StringBuilder requestStr = new StringBuilder();
		try {
			int len=-1;
			byte[] buff =new byte[1024];
			do{
				len=inputStream.read(buff);
				if(len!=-1)
				{
					requestStr.append(new String(buff,0,len,"UTF-8")).append('\n');
				}
			}while(len==1024);
		} catch (IOException e) {
			logger.error("读取请求报文过程中出错！");
			return "";
		}
		logger.info("请求的报文头是：");
		logger.info(requestStr.toString());
		return requestStr.toString();
	}
	/**
	 * 读取文件，写入浏览器。 如果url指定的文件存在，传到浏览器。如果不存在，就在浏览器上显示404页面
	 * 这里可能出现的问题：如果用户指定的404页面不存在， 那么程序就会陷入死循环。
	 * InputStream会抛出FileNotFoundException异常，is.read(buf)会抛出IOException
	 * @return String
	 * @param sourceName
	 *            ，要读取文件的文件名
	 * @param out
	 *            将文件写入浏览器的输出流
	 * */
	public void fileToBrowser(String sourceName, OutputStream out) {
		if(out==null)
			return;
		File file = new File(sourceName);
		if (file.exists()&&file.isFile()) {
			try {
				sendHead(sourceName, out);
			    sendFile(out, file);
			} catch (IOException e) {
				/*这个异常没必要终止程序，读取写入文件错误，还是有可能把少量信息传到浏览器的*/
				logger.error("程序要找的文件{}能找到，可是文件在读取和写入过程中出错！", sourceName);
				e.printStackTrace();
			}
		} else {
			logger.error("程序要找的文件{}找磁盘上找不到！将会用404页面来替代这个文件返回！", root+sourceName);
             /* 注意这是递归调用，如果说这个指定的404文件不存在，就会陷入死循环*/
			fileToBrowser(root+"404.html", out);
		}
		}
	
	/**
	 * 发送报文头到一个ouputStrem对象，就是socket的outputStream对象
	 * 这代码这么写的前提是：这三个参数都是有意义的，都不为空。
	 * @param sourceName
	 * @param out
	 * @param file
	 * @throws IOException
	 */
	public void sendHead(String sourceName, OutputStream out)
			throws IOException {
		String head = "HTTP/1.1 200 OK" + "\n"
				+ "Date: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
				+ "Content-Length: " + (new File(sourceName)).length() + "\n"
				+ "Content-Type: "+getMIMEtype(sourceName) + "\n"
				+ "Cache-Control: private" + "\n"
				+ "Expires: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
				+ "Connection: Keep-Alive" + "\n" + "\n";
		out.write(head.getBytes());
	}

	/**
	 * 发送文件到一个ouputStrem对象，就是socket的outputStream对象
	 * 这代码这么写的前提是：这三个参数都是有意义的，都不为空。
	 * @param out
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void sendFile(OutputStream out, File file) {
		InputStream is =null;
		try {
			byte[] buf = new byte[1024];
			int len;
			is = (InputStream)(new FileInputStream(file));
			while ((len = is.read(buf))!= -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			logger.error("文件【{}】读取、写入浏览器中出现错误！",file.toString());
			return;
		} finally {
			/* 可能无法正常关闭 */
			try {
				is.close();
			} catch (IOException e2) {
				logger.error("文件读取流无法正常关闭！");
				return;
			}
		}
	}
	
	/**
	 * 根据文件名的后缀类型来确定返回类型。因为执行这段代码之前， 已经知道这个文件肯定存在，所以肯定是有后缀的
	 * 决定报文头“Content-Type”那一行的内容， 例如如果是aa.jpg，返回就是"Content-Type: image/jpeg"
	 * 要注意，文件名中包含了“”/\：*<>等符号都是不合法的。这个里面的判断比较多。
	 * @return String
	 * @param String sourceString ，url的字符串
	 *            
	 */
	public String getMIMEtype(String sourceString) {
		if(sourceString==null)
			return "text/html;charset=gb2312";
		String type=sourceString.substring(sourceString.lastIndexOf('.'));
		Map<String, String> map= new HashMap<String, String>();
		initMap("src/main/java/com/succez/dengc/serv/mimeMap",map);
		String returnType =map.get(type);
		if(returnType==null)
			return "text/html;charset=gb2312";
		return returnType;
	}
	/**
	 *对map对象进行初始化，把一个文件里面的hash关系读取，放入map中
	 *@param String sourcFile map关系保存的文件所在
     *@param Map<String,String> map存放读取出来的关系印射
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
	
}


