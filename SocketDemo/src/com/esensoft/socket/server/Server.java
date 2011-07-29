package com.esensoft.socket.server;
import java.io.IOException;
import java.net.ServerSocket;

import com.esensoft.socket.server.ServerThread;
/**
 * 一个可以支持多线程的服务器类
 * 创建一个serversocket类对 端口进行监听
 * 如果有请求，那么新建一个线程，
 * 让那个线程代理它对请求的用户进行通信
 * @version 1.0 ,2011-7-19
 * @author 邓超
 * @since jdk1.5
 * */
public class Server 
{
  /*用一个静态变量记录客户的总人数*/
  private static int clientCount;
  
  /**主函数，构造一个serverSocket对象实例，监听端口*/
  public static void main(String[] args) {
	ServerSocket serverSocket =null;
	//判断服务器是否在监听
	boolean listening =true;
	try 
	{
		serverSocket =new ServerSocket(8097);
	} 
	catch (Exception e)
	{
		e.printStackTrace();
		// TODO: handle exception
	}
	//实例化之后，总是处于监听状态
	while (listening)
	{
		try 
		 {
			 //创建一个socket线程的实例，用它做代理
			(new ServerThread(serverSocket.accept(),clientCount)).start();
			clientCount++;
		} 
		 catch (IOException e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//关闭服务器监听
	try
	{
		serverSocket.close();
	} 
	catch (IOException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
  
 
}
