package esensoft.ioputstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于用printWriter和bufferedReader，获取浏览器的请求资源的地址 ， 并且通过流将C盘根目录下的同名文件读取，
 * 输出到浏览器，要求可以查看图片 尝试着使用inputSream&outputSream做同样的事情. 多线程的事情先不管，只是单线程试验
 * @version 2.0,2011-7-22
 * @author 邓超
 * @since jdk1.5
 */
public class UsingIOStream {
	/* serverSocket监听的端口、文件名 */
	private int port;
	private String sourceName;
	private int fileLen = 0;

	/**
	 * 构造函数
	 * */
	public UsingIOStream(int port) {
		this.port = port;
	}

	/**
	 * 连接浏览器，获取浏览器请求报文，解析Url。读取指定目录的文件，并传送到浏览器
	 * */
	public void linkBrowser() {
		try {
			ServerSocket server = new ServerSocket(this.port);
			Socket socket = server.accept();
			BufferedReader socketiStream = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			analysisUrl(socketiStream);
			OutputStream out = socket.getOutputStream();
			fileToBrowser(sourceName, out);
			socketiStream.close();
			socket.close();
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析请求报文，获取url,并且将它拼装成windows下的指定目录
	 * 
	 * @param BufferedReader
	 *            socket获得的输入流，由它获取请求报文
	 * */
	public void analysisUrl(BufferedReader is) {
		/*
		 * 获取Http请求报文内容，使用的是BufferedReader 如果用InputSream来获取， 那么的话要用到一个
		 * StringBuffer buffer,然后通过方法buffer.append(new String(buff,0,count))
		 * 新建很多临时变量 还不如用BufferedReader
		 */
		StringBuffer buffer = new StringBuffer();
		try {
			while (!"".equals(sourceName = is.readLine())) {
				buffer.append(sourceName + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("报文头是：\n" + buffer.toString());
		sourceName = buffer.toString();
		// 截取报文中从第五个开始到“HTTP/1.1”之间的字符
		sourceName = sourceName
				.substring(5, sourceName.lastIndexOf("HTTP/1.1"));
		sourceName.replaceAll("/", File.separator);
		sourceName = "c://" + sourceName;
	}

	/**
	 * 读取文件，写入浏览器
	 * */
	public void fileToBrowser(String sourceName, OutputStream out) {
		try {
			String head = "HTTP/1.1 200 OK"
					+ "\n"
					+ "Date: Thu, 21 Jul 2011 01:45:42 GMT"
					+ "\n"
					// + "Server: BWS/1.0" + "\n"
					// + "Content-Length: 9986" +"\n"
					+ "Content-Type: text/html;charset=gb2312" + "\n"
					+ "Cache-Control: private" + "\n"
					+ "Expires: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
					// + "Content-Encoding: gzip" + "\n"
					+ "Connection: Keep-Alive" + "\n" + "\n";
			out.write(head.getBytes());
			InputStream is = (InputStream) (new FileInputStream(sourceName));
			byte[] buf = new byte[1024];
			int count;
			while ((count = is.read(buf)) != -1) {
				out.write(buf, 0, count);
				fileLen = fileLen + count;
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 主函数
	 * */
	public static void main(String[] args) {
		UsingIOStream test = new UsingIOStream(8066);
		test.linkBrowser();
	}

}
