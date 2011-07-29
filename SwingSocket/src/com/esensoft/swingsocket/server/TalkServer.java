package com.esensoft.swingsocket.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.esensoft.swingsocket.swing.ShowMessageFrame;

/**
 * 一个服务端类，约定端口号上监听
 * 如果有请求，就 建立一个socket对象和客户端通信
 * */
public class TalkServer {
	/*三个私有变量分别是图形界面实例、
	 * ServerSocket的实例以及它接受请求后产生的 Socket实例*/
	private ShowMessageFrame frame;
	private ServerSocket serverSocket;
	private Socket socket;
	
	/**构造函数，约定端口号，和想要取的名字*/
	public TalkServer(int port ,String hostName)
	{
		try 
		{
			//服务端的端口号设定
			serverSocket =new ServerSocket(port);
			//如果有请求，新建一个secket对象实例
			socket =serverSocket.accept();
			//创建输入输出流
			BufferedReader bIn =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
			//新建一个图形界面，
			frame =new ShowMessageFrame(hostName, "端口号："+port, out);
			out.println(hostName);
			frame.recieve("连接"+bIn.readLine());
			out.flush();
			//开始聊天
		    String aline="";
		    do
		    {
		    	aline=bIn.readLine();
		    	frame.recieve(aline);
		    }while(aline!=null);
		    
		    /*通信完毕之后，关闭输入流输出流，socket，断开连接*/
		    bIn.close();
		    out.close();
		    socket.close();
		    serverSocket.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 主函数，建立一个服务端实例
	 * */
	public static void main(String[] args) {
		new TalkServer( 8084, "服务端");
		
	}
	

}
