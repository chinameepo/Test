package esensoft.connectbroswer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个对浏览器通信的尝试,端口8066 新建一个serverSocket对象，监听指定端口 如果有请求，建立 socket对象，并且创建和它相关的IO流
 * 通过输入流读取请求的报文内容。 获得浏览器发来的url地址后， 通过新建一个文件输入流读取文件，并且socket的输出流向浏览器传输文件
 * 在浏览器上显示文件内容
 * 
 * @version 1.0,2011-7-20
 * @author 邓超
 * @since jdk1.5
 * */

public class ConnectBrosewer {
	private ServerSocket serverSocket;
	private Socket socket;
	private String sourceName;

	/**
	 * 构造函数，指定端口和浏览器通信 获取浏览器报文内容，返回浏览器信息
	 * */
	public ConnectBrosewer(int port) {
		try {
			// 请求的报文的所有信息
			StringBuffer buffer = new StringBuffer();
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();

			// 建立由socket 产生的IO流
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			getRequest(buffer, bufferedReader);

			/**
			 * 从报文中提取url，并且指定要读取的文件是在c盘
			 * 如果输入的/view/test.txt,加上这个前缀就变成了c:\view\text.txt
			 * 而不是c:\\view\text.txt 不过没关系，文件系统还是可以找到这个文件的
			 */
			this.sourceName = getUrl(buffer.toString());
			this.sourceName = "c:\\" + this.sourceName;
			/* 读取sourceName指定目录的文件，并且利用socket的输出流输出到浏览器 */
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			readFileToBrowser(sourceName, out);
			closeit(bufferedReader);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRequest(StringBuffer buffer, BufferedReader bufferedReader) throws  IOException
			 {
		String aline;
		/* 获取请求报文的内容，并且从中解析得到url地址 
		 * 不要用do-while语句，判断条件也不要用aline!=null
		 * 否则程序会一直卡在这里，直到浏览器断开连接才会有反应
		 * */
		while(!"".equals(aline=bufferedReader.readLine()))	
		{
			buffer.append(aline+"\n");
		}
		System.out.println("报文头是：\n"+buffer.toString());
	}

	private void closeit(BufferedReader bufferedReader) throws IOException {
		bufferedReader.close();
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从Http报文中获得资源的地址url 的函数 参数必须是http报文中get方法发过来的
	 * 
	 * @return String,放回的字符串是參數本身
	 * */
	public static String getUrl(String httpString) {
		// 字符串"HTTP/1.1"开始的位置
		int indexHTTP;
		indexHTTP = httpString.lastIndexOf("HTTP/1.1");
		/**
		 * 报文的格式是固定的，例如： "GET /test/aa.txt HTTP/1.1 Accept-Encoding: gzip,
		 * deflate" url是夹在 GET /和 HTTP/1.1之间。 报文是以"GET"开头的，占了4个位置
		 * 所以从第5个字符开始读取，到HTTP/1.1前面的字符停止
		 */
		httpString = httpString.substring(5, indexHTTP - 1);
		// 将请求报文中所有右斜杠换成左斜杠
		httpString.replaceAll("/", "\\");
		return httpString;
	}

	/**
	 * 指定地址的文件读取，并且通过指定的输出流 把文件输出到浏览器中
	 * 
	 * @param String
	 *            url是指定的文件目录
	 * @param PrintWriter
	 *            out 是指定的输出流对象
	 * */
	public void readFileToBrowser(String url, PrintWriter out) {
		try {
			String aline ;
			StringBuffer buffer = new StringBuffer();
			buffer.append("HTTP/1.1 200 OK" + "\n"
					+ "Date: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
					//+ "Server: BWS/1.0" + "\n" + "Content-Length: 9986" + "\n"
					+ "Content-Type: text/html;charset=gb2312" + "\n"
					+ "Cache-Control: private" + "\n"
					+ "Expires: Thu, 21 Jul 2011 01:45:42 GMT" + "\n"
					//+ "Content-Encoding: gzip" + "\n"
					+ "Connection: Keep-Alive" + "\n" + "\n");

			BufferedReader bReader = new BufferedReader(new FileReader(url));

			while ((aline = bReader.readLine()) != null) {
				// 按行读取，并且添加到字符串末尾
				buffer.append(aline + "\n");
			}

			//out.write(buffer.toString());
			out.println(buffer.toString());
			System.out.println(buffer.toString());
			out.flush();
			// 关闭输入输出流*/
			out.close();
			bReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 主函数，测试查看结果
	 * */
	public static void main(String[] args) {
		ConnectBrosewer connectBrosewer = new ConnectBrosewer(8066);
	}
}
