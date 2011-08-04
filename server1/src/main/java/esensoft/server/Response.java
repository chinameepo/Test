package esensoft.server;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-2 下午07:12:36
 * @since   jdk1.5
 * 类说明
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
	 * 这个是系统盘，非常反对将根目录放在这里
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
				/*用这个方法获得的名字，有可能不是用户输入的url地址。尤其是在用户输入多个
				 * %或者@号的时候，你就获取不到这些东西了！*/
				String sourceName;
				sourceName = getUrlFromStream(socketiStream);
				try {
					out = socket.getOutputStream();
					fileToBrowser(sourceName, out);
				} catch (IOException e) {
					logger.error("输出流对象新建的时候出错！程序终止！,");
				}
			} catch (IOException e) {
				logger.error("程序运行中出现错误，程序终止！");
			} finally {
				try {
					out.close();
					socketiStream.close();
					socket.close();
				} catch (IOException e) {
					logger.error("文件流在关闭的时候出现错误！，不能正常关闭");
				}
			}
	}
	/**
	 * 从socket中的输入流，获取请求报文所有内容，如果处理不好，就会在读取报文头的时候卡死，直到等待超时
	 * 待完善：这个是从字符串中截取子串，那么要是该字符串长度是1-5，或者字符串中不含有"HTTP/1.1"，就出错了
	 * @param BufferedReader
	 *            socket获得的输入流，由它获取请求报文
	 * @throws UnsupportedEncodingException ，在用户输入的url中，某个%号后面没有16进制数的话，会抛出此异常，不管它
	 * @exception IOException
	 * */
	public String getUrlFromStream(InputStream inputStream) throws UnsupportedEncodingException {
		if(inputStream==null)
			return "";
		else {
			StringBuilder builder = new StringBuilder();
			/*很多问题都是出在这里，如果说在这里卡死了，等待超时之后再去运行后面的程序就已经毫无意义了
			 * 所以这里一定要保证不能卡死！*/
			try {
				int len=-1;
				byte[] buff =new byte[1024];
				do{
					len=inputStream.read(buff);
					if(len!=-1)
					{
						builder.append(new String(buff,0,len,"UTF-8")).append('\n');
					}
				}while(len==1024);
			} catch (IOException e) {
				logger.error("读取请求报文过程中出错！");
			}
			String requestString = builder.toString();
			logger.info("请求的报文头是：");
			logger.info(requestString);
			
			if ("".equals(requestString))
				return "";
			else {
				/*
				 * 截取报文中从第五个开始到“HTTP/1.X”之间的所有字符，因为请求报文第一行的格式一般都是 
				 * GET /urlcontent**** HTTP/1.1
				 */
				requestString = requestString.substring(5,
						requestString.lastIndexOf("HTTP/1.") - 1);
				/* 如果对方输入的页面是空的，即输入的就是服务器的地址，例如http://localhost:8080,那么就跳至默认首页,否则就是查找文件 */
				if ("".equals(requestString))
					{
					requestString = "index.html";
					return requestString;
					}
				/*空格经过浏览器UTF-8编码处理后会变成%20传过来*/
				requestString =URLDecoder.decode(requestString,"UTF-8");
				logger.info("文件名是：{}", requestString);
				/*在windows操作系统中，文件是不区分大小写的。例如ATM.TXT,atm.txt是同一个文件。
				 * 而c://Temp/TeSt，和C：//TEMP/TEST表示的是同一个目录。java的File对象在建立对象的时候
				 * 对于url后面的空格是忽略的，例如c://test.txt    就等于c://test.txt
				 * 去掉前面后面的空格，变成小写的，是方便阅读和查看。*/
				return requestString.toLowerCase().trim();
			}
		}
	}

	/**
	 * 读取文件，写入浏览器 如果组装好的url指定的文件存在，就读取文件，传到浏览器。如果不存在，就在浏览器上显示404页面
	 * 这里可能出现的问题：如果用户指定的404页面不存在， 那么程序就会陷入死循环。
	 * InputStream会抛出FileNotFoundException异常，is.read(buf)会抛出IOException
	 * 
	 * @param sourceName
	 *            ，要读取文件的文件名
	 * @param out
	 *            将文件写入浏览器的输出流
	 * @throws IOException
	 *             ， FileNotFoundException
	 * */
	public void fileToBrowser(String sourceName, OutputStream out)
			throws FileNotFoundException, IOException {
		if(out==null)
			return;
		else {
			File file = new File(root, sourceName);
			if (file.exists()&&file.isFile()) {
				try {
					/*
					 * 自定义的http应答报文的报文头，先把他发给浏览器。格一个空行后， 在这个后面加入要传给浏览器的文件内容
					 */
					sendHead(sourceName, out, file);
				    sendFile(out, file);
				} catch (IOException e) {
					/*这个异常没必要终止程序，读取写入文件错误，还是有可能把少量信息传到浏览器的*/
					logger.error("程序要找的文件{}能找到，可是文件在读取和写入过程中出错！", sourceName);
					e.printStackTrace();
				}
			} else {
				//logger.error("程序要找的文件{}找磁盘上找不到！将会用404页面来替代这个文件返回！", root+sourceName);
				/*
				 * 如果找不到指定的文件，就是资源不存在，就跳至404文件 注意这是递归调用，如果说这个指定的404文件不存在，就会陷入
				 * 死循环，不停调用，这个函数也就会一直执行，没有出口
				 */
				fileToBrowser("404.html", out);
			}
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
	public void sendHead(String sourceName, OutputStream out, File file)
			throws IOException {
		String head = "HTTP/1.1 200 OK" + "\n"
				+ "Date: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
				+ "Content-Length: " + file.length() + "\n"
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
	public void sendFile(OutputStream out, File file)
			throws FileNotFoundException, IOException {
		InputStream is =null;
		try {
			byte[] buf = new byte[1024];
			int len;
			is = (InputStream) (new FileInputStream(file));
			while ((len = is.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			logger.error("文件读取中出现错误！");
		} finally {
			/* 可能无法正常关闭 */
			try {
				is.close();
			} catch (IOException e2) {
				logger.error("文件读取流无法正常关闭！");
			}
		}
		
	}
	
	/**
	 * 根据文件名的后缀类型来确定返回类型。因为执行这段代码之前， 已经知道这个文件肯定存在，所以肯定是有后缀的
	 * 决定报文头“Content-Type”那一行的内容， 例如如果是aa.jpg，返回就是"Content-Type: image/jpeg"
	 * 要注意，文件名中包含了“”/\：*<>等符号都是不合法的。这个里面的判断比较多。
	 * 
	 * @param String
	 *            sourceString ，url的字符串
	 */
	public String getMIMEtype(String sourceString) {
		// TODO 待完善
		String returnType;
		if(sourceString==null)
			returnType ="";
		else {
			/* 图片的返回类型 */
			if (sourceString.endsWith(".jpg")
					|| sourceString.endsWith(".jpeg"))
				returnType = "image/jpeg";
			else if (sourceString.endsWith(".gif"))
				returnType = "image/gif";
			else if (sourceString.endsWith(".png"))
				returnType = "image/png";
			/* 文本文件的返回类型 */
			else if (sourceString.endsWith(".xml"))
				returnType = "text/xml";
			else if (sourceString.endsWith(".txt") 
					|| sourceString.endsWith(".c")
					|| sourceString.endsWith(".cpp")
					|| sourceString.endsWith(".java")
					|| sourceString.endsWith(".h"))
				returnType = "text/plain";
			else {
				returnType = "text/html;charset=gb2312";
			}
		}
		
		return returnType;
	}
}


