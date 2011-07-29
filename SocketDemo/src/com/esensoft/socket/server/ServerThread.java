package com.esensoft.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 一个处理服务器 和客户端的通信类
 * 服务器每次监听到一个请求，就响应，
 * 并且将服务的内容托管给这个类
 * 这个类负责和客户端进行交换
 * @version 1.0,2011-7-19
 * @author 邓超
 * @since jdk1.5
 * */
public class ServerThread extends Thread
{
	/*与本线程相关的socket对象*/
 private Socket socket = null;
    /*与本线程相关的客户端的数量*/
 private int clientTotal;
 
 /**
  * 构造函数
  * 指定一个socket类，然后客户数量每新建一个要加1*
  */
 public ServerThread (Socket clientSocket ,int number)
 {
	 this.socket =clientSocket;
	 this.clientTotal =number+1;
 }
 
 /**线程类的run方法，规定了线程的动作
  * 线程利用输入/输出流和客户端进行交互
  * 结果还是显示在控制台*/
 public void run()
 {
	 String readLine="";
	
	 
	 try {
		  //控制台下的文件输入流，由它来读取控制台下服务器用户输入的信息
		 BufferedReader sBufferedReader =new BufferedReader(new InputStreamReader(System.in));
		  //文件输入流，有它来读取客户端发过来的信息
		BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    PrintWriter printWriter =new PrintWriter(socket.getOutputStream());
	    
	    
	    
	    
	    do 
	    {
	    	//从客户那里接到信息了，在控制台上显示出来
		    System.out.println("客户："+this.clientTotal+sBufferedReader.readLine());
	    	//接着服务器要回复信息,在控制台输入，并且输入后同样在控制台输出
			readLine =sBufferedReader.readLine();
			printWriter.println(readLine);
			//要刷新
			printWriter.flush();
			System.out.println("服务器："+readLine+(new Date()));
			
		} while (!"bye".equals(readLine));
	    printWriter.close();
	    bufferedReader.close();
	    socket.close();
	    
	 } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 
}

