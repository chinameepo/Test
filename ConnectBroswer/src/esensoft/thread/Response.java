package esensoft.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javaws.exceptions.ExitException;

/**
 * 浏览器应答类，通过获得的socket对象，利用IO流和浏览器通信。
 * 
 * @version 3.4,2011-7-27
 * @author 邓超
 * @since jdk1.5
 * */
public class Response implements Runnable {
	/*
	 * 这个socket对象必须要从Serve监听对象传过来，和浏览器交互的IO流都是通过它来建立，
	 * 通过这个对象的输入流来获取url，通过这个对象的输出流来向浏览器返回内容。 不可缺少，必须要有
	 */
	private Socket socket;
	/* 服务器的默认根目录，从Serve监听对象中获得 ，可以缺省 */
	private String root;
	/* 文件的目录 */
	private String sourceName;
    
	public Response(Socket response, String root) {
		this.socket = response;
		this.root = root;
	}

	/**
	 * 这个是系统盘，非常反对这么用
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
	
			Logger logger = LoggerFactory.getLogger(Response.class);
			OutputStream out = null;
			BufferedReader socketiStream = null;
			try {
				socketiStream = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				/*用这个方法获得的名字，有可能不是用户输入的url地址。尤其是在用户输入多个
				 * %或者@号的时候，你就获取不到这些东西了！*/
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
	 * 从socket中的输入流，获取请求报文所有内容
	 * 
	 * @param BufferedReader
	 *            socket获得的输入流，由它获取请求报文
	 * @exception IOException
	 * */
	public String getUrlFromStream(BufferedReader is) throws IOException {
		Logger logger = LoggerFactory.getLogger(Response.class);
		StringBuilder builder = new StringBuilder();
		String aline;
		while (!"".equals(aline = is.readLine())) {
			builder.append(aline).append('\n');
		}
		/*注意这一行，因为builder被初始化过了，所以aline 被赋值过后，肯定不等于Null*/
		aline = builder.toString();
		logger.info("请求的报文头是：{}", aline);
		
		return cutUrl(aline);
	}

	/**
	 * 截断请求报文，在第一行抽取url。把这一段代码抽取出来，方便测试
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	public String cutUrl(String requestString) {
		Logger logger = LoggerFactory.getLogger(Response.class);
		if ("".equals(requestString))
			return "";
		else if (requestString == null) {
			return "";
		} else {
			/*
			 * 截取报文中从第五个开始到“HTTP/1.1”之间的所有字符，因为请求报文第一行的格式一般都是 
			 * GET /********* HTTP/1.1
			 */
			requestString = requestString.substring(5,
					requestString.lastIndexOf("HTTP/1.1") - 1);
			/*
			 * 如果对方输入的页面是空的，即输入的就是服务器的地址，例如http://localhost:8080,那么就跳至默认首页,否则就是查找文件
			 */
			if ("".equals(requestString))
				requestString = "index.html";
			/*
			 * 默认情况，URL在编码的时候会自动把空格转换成%20，例如http://c s
			 * s.txt，通过浏览器传过来就是http://c20%s20%s.txt ，所以要用编码把它转换回来，输入的是空格截获的还是空格。
			 * 这里遇到的问题是，假如有用户输入了%%%@@@@等符号的时候，这个URLDecoder.decode方法是会报错的！
			 */
			try {
				requestString = URLDecoder.decode(requestString, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("客户输到浏览器中的url包含了@%等符号，导致不能解析，程序终止！");
			
			}
			logger.info("文件名是：{}", requestString);
			return requestString;
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
		Logger logger = LoggerFactory.getLogger(Response.class);
		File file = new File(root, sourceName);
		if (file.exists()) {
			InputStream is = null;
			try {
				is = (InputStream) (new FileInputStream(file));
				/*
				 * 自定义的http应答报文的报文头，先把他发给浏览器。格一个空行后， 在这个后面加入要传给浏览器的文件内容
				 */
				String head = "HTTP/1.1 200 OK" + "\n"
						+ "Date: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
						+ "Content-Length: " + file.length() + "\n"
						+ getContentType(sourceName) + "\n"
						+ "Cache-Control: private" + "\n"
						+ "Expires: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
						+ "Connection: Keep-Alive" + "\n" + "\n";
				out.write(head.getBytes());
				byte[] buf = new byte[1024];
				int len;
				while ((len = is.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				out.flush();
			} catch (IOException e) {
				/*这个异常没必要终止程序*/
				logger.error("程序要找的文件{}能找到，可是文件在读取和写入过程中出错！", sourceName);
			} finally {
				/* 可能无法正常关闭 */
				try {
					is.close();
				} catch (IOException e2) {
					logger.error("{}文件读取流无法正常关闭！", sourceName);
				}
			}
		} else {
			logger.error("程序要找的文件{}找磁盘上找不到！将会用404页面来替代这个文件返回！", sourceName);

			/*
			 * 如果找不到指定的文件，就是资源不存在，就跳至404文件 注意这是递归调用，如果说这个指定的404文件不存在，就会陷入
			 * 死循环，不停调用，这个函数也就会一直执行，没有出口
			 */
			fileToBrowser("404.html", out);
		}
	}

	/**
	 * 根据文件名的后缀类型来确定返回类型。因为执行这段代码之前， 已经知道这个文件肯定存在，所以肯定是有后缀的
	 * 决定报文头“Content-Type”那一行的内容， 例如如果是aa.jpg，返回就是"Content-Type: image/jpeg"
	 * 
	 * @param String
	 *            sourceString ，url的字符串
	 */
	public String getContentType(String sourceString) {
		// TODO 待完善
		String returnType;
		/* 图片的返回类型 */
		if (sourceString.endsWith(".jpg") || sourceString.endsWith(".jpeg"))
			returnType = "Content-Type: image/jpeg";
		else if (sourceString.endsWith(".jpg"))
			returnType = "Content-Type: image/gif";
		else if (sourceString.endsWith(".png"))
			returnType = "Content-Type: image/png";
		/* 文本文件的返回类型 */
		else if (sourceString.endsWith(".xml"))
			returnType = "Content-Type: text/xml";
		else if (sourceString.endsWith(".txt") || sourceString.endsWith(".c")
				|| sourceString.endsWith(".cpp")
				|| sourceString.endsWith(".java")
				|| sourceString.endsWith(".h"))
			returnType = "Content-Type: text/plain";
		else {
			returnType = "Content-Type: text/html;charset=gb2312";
		}
		return returnType;
	}
}
