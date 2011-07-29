package com.esensoft.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 一个聊天程序的客户端，客户端可以建立多个，和同一个服务器通信
 * @version 1.0 ,201-7-19
 * @author 邓超
 * @since jdk1.5
 * */
public class ClientTalk {
	
   /**
    * 主函数，建立一个socket对象，并且向服务器发出请求
    * 等待服务器响应，进行交互式聊天
    * */
	public static void main(String[] args) {
		/*从服务器里面读到的字符内容，存放到一个字符串类里面*/
		String readLine ="";
		try {
			//建立socket对象，指定服务器地址，服务端口
			Socket socket =new Socket("127.0.0.1",8081);
			//由系统标准输入构造的输入缓冲对象，用于客户端在控制台进行输入
			BufferedReader sInBufferedReader =new BufferedReader(new InputStreamReader(System.in));
		    //新建的一个bufferedReader对象,这是从服务器里面读取字符串
			BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    //一个输出对象，将输出返回服务器
			PrintWriter printWriter =new PrintWriter(socket.getOutputStream());
			
			do {
				//读取一行客户端自己在控制台下输入的字符串
				readLine =sInBufferedReader.readLine();
				//立即输出
				printWriter.println(readLine);
				//刷新输出流
				printWriter.flush();
				System.out.println("客户端："+sInBufferedReader.readLine()+(new Date()));
				System.out.println("服务器："+bufferedReader.readLine()+(new Date()));
			} while (!"bye".equals(readLine));
			printWriter.close();
			bufferedReader.close();
			socket.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
