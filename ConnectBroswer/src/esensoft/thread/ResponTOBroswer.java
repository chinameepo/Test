package esensoft.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 浏览器应答类，新建一个socket对象和浏览器通信。首先获取浏览器发过来的报文，
 * 从请求报文中解析出url。然后将浏览器的url印射到本地磁盘文件。将文件读取， 
 * 通过文件流将所读取的文件传送到浏览器
 * 
 * @version 3.3,2011-7-27
 * @author 邓超
 * @since jdk1.5
 * */
public class ResponTOBroswer implements Runnable {
	/* 所有和浏览器交互的IO流都是通过它来建立，文件也通过这个对象传输 */
	private Socket socket;
	/* 文件url */
	private String sourceName;
	/* 服务器的默认根目录 */
	private String root;
	/* 不知道这个文件是否存在，文件长度是0 */
	private long fileLen = 0;

	public ResponTOBroswer(Socket response, String root) {
		this.socket = response;
		this.root = root;
	}

	/**
	 * 核心方法，规定该线程的执行内容，通过浏览器的输入流得到请求报文头，从中截取文件Url,
	 * 如果文件存在，读取文件，将内容传输到浏览器。不存在，返回404文件
	 * 
	 * @exception IOException
	 * */
	public void run() {
		try {
			BufferedReader socketiStream = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			getUrlFromStream(socketiStream);
			OutputStream out = socket.getOutputStream();
			fileToBrowser(sourceName, out);
			socketiStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("程序运行中，文件操作出现错误，程序终止！");
			return;
		}
	}

	/**
	 * 解析请求报文，获取url,并且将它拼装成windows下的指定目录root下的文件
	 * 
	 * @param BufferedReader
	 *            socket获得的输入流，由它获取请求报文
	 * @exception IOException
	 * */
	public void getUrlFromStream(BufferedReader is) throws IOException {
		StringBuilder buffer = new StringBuilder();
		while (!"".equals(sourceName = is.readLine())) {
			buffer.append(sourceName).append('\n');
		}
		sourceName = buffer.toString();
		/* 截取报文中从第五个开始到“HTTP/1.1”之间的字符 */
		sourceName = sourceName.substring(5,
				sourceName.lastIndexOf("HTTP/1.1") - 1);
		/* 拼装目录，如果对方输入的页面是空的，即输入的就是服务器的地址，那么就跳至默认首页 */
		if ("".equals(sourceName))
			sourceName = root + "index.html";
		else
			sourceName = root + sourceName;
		System.out.println(sourceName);
	}

	/**
	 * 读取文件，写入浏览器,为了保证只有一个线程在读取文件，设置成同步方法
	 * 如果组装好的url指定的文件存在，就读取文件，传到浏览器。如果不存在，就在浏览器上显示404页面
	 * InputStream会抛出FileNotFoundException异常，is.read(buf)会抛出IOException
	 * 
	 * @param sourceName
	 *            ，要读取文件的文件名
	 * @param out 将文件写入浏览器的输出流
	 * @throws IOException， FileNotFoundException
	 * */
	public synchronized void fileToBrowser(String sourceName, OutputStream out)
			throws FileNotFoundException, IOException {
		File file = new File(sourceName);
		if (file.exists()) {
			fileLen = file.length();
			InputStream is = (InputStream)(new FileInputStream(file));
			
			/* 自定义的http应答报文的报文头，先把他发给浏览器，
			 * 在这个后面加入要传给浏览器的内容 
			 * */
			String head = "HTTP/1.1 200 OK" + "\n"
					+ "Date: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
					+ "Content-Length: " + fileLen + "\n" 
					+ getContentType(sourceName) + "\n"
					+ "Cache-Control: private" + "\n"
					+ "Expires: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
					+ "Connection: Keep-Alive" + "\n" + "\n";
			out.write(head.getBytes());
			byte[] buf = new byte[1024];
			int count;
			while ((count = is.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			out.flush();
			out.close();
			is.close();
		} else {
			System.out.println("找不到这个文件" + sourceName);
			
			/* 
			 * 如果找不到指定的文件，就是资源不存在，就跳至404文件 
			 * 注意这是递归调用，如果说这个指定的404文件不存在，就会陷入
			 * 死循环，不停调用，这个函数也就会一直执行，没有出口
			 * */
			fileToBrowser("c:\\404.html", out);
		}
	}

	/**
	 * 根据文件名的后缀类型来确定返回类型。因为执行这段代码之前， 已经知道这个文件肯定存在，所以肯定是有后缀的
	 * 决定报文头“Content-Type”那一行的内容， 例如如果是aa.jpg，返回就是"Content-Type: image/jpeg"
	 * 
	 * @param String
	 *            sourceString ，url的字符串
	 */
	private String getContentType(String sourceString) {
		// TODO 待完善
		String returnType;
		/*图片的返回类型*/
		if (sourceString.endsWith(".jpg")||sourceString.endsWith(".jpeg"))
			returnType = "Content-Type: image/jpeg";
		else if (sourceString.endsWith(".jpg"))
			returnType = "Content-Type: image/gif";
		else if (sourceString.endsWith(".png"))
			returnType = "Content-Type: image/png";
		/*文本文件的返回类型*/
		else if (sourceString.endsWith(".xml"))
			returnType = "Content-Type: text/xml";
		else if (sourceString.endsWith(".txt")
				||sourceString.endsWith(".c")
				||sourceString.endsWith(".cpp")
				||sourceString.endsWith(".java")
				||sourceString.endsWith(".h"))
			returnType = "Content-Type: text/plain";
		else {
			returnType = "Content-Type: text/html;charset=gb2312";
		}
		return returnType;
	}
}
