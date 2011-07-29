package com.esensoft.mutithreadsocket.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.esensoft.mutithreadsocket.swing.MutiSwing;
/**
 * 一个服务端的代理，每次有一个请求，它都会被实例化一个对象
 * 与请求通信
 * @version 3.0 ,2011-7-21
 * @author 邓超
 * @since jdk1.5*/
public class ServThread  extends Thread{
	//用来连接客户的socket对象
	private Socket socket;
	//一个静态变量，每次新建一个实例，变量会增加1，用来区分不同服务端
	private static int clientTotal=0;
	private String nameString;
	private MutiSwing showframe;
	
	public ServThread(Socket socket ){
		this.socket =socket;
		nameString ="服务端:"+clientTotal;
		clientTotal++;
	}
	/**
	 * run方法，规定了这个线程的运行方式
	 * 和客户通信，新建一个图形界面
	 * */
	public void run()
	{
		try {
			//新建基于socket的IO流，并基于IO流，建立图形界面
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			showframe = new MutiSwing(this.nameString, "与客户端"+clientTotal  + "聊天", out);
			out.print(nameString);
			//in.readLine()
            /*进行通信，这里只管读取对方发过来的信息，至于自己发过去的信息，
             * 将out对象传给给图形界面，托付给界面处理*/
			String aline = "";
			do {
				aline = in.readLine();
				if (aline != null)
					showframe.recieve(aline);
			} while (!"bye".equals(aline));

			out.flush();
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
}
