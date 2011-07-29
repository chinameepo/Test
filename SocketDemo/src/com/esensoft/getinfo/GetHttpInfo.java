package com.esensoft.getinfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


/**
 *测试类
 *新建一个serversocket对象，监听指定端口
 * 如果有http请求，就创建socekt实例
 * socekt实例的输入流对象来获取http报文
 * 用一个Stringbuffer对象来存取报文
 * 最后输出
 * @version 1.0 2011-7-20
 * @author 邓超
 * @since jdk1.5
 * */
public class GetHttpInfo {

		/**
	     * main函数实现获取报文的功能
	     * */
	  public static void main(String[] args) {
		  //用来存取字符串
		  StringBuffer result= new StringBuffer();
		try 
		{
			//一个serverSocket实例，如果有请求，就建立socket实例
			 ServerSocket serverSocket =new ServerSocket(8088);
			 Socket socket =serverSocket.accept();
			 //socket的输入流对象
			 BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
		     //按行读取报文内容，追加到StringBuffer的末尾
			do {
				result.append(bufferedReader.readLine()+"\n");
			  } while (bufferedReader.readLine()!=null);
			//输出
			System.out.println((new Date())+"\n"+result.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			// TODO: handle excepti
	    }
	}
}
