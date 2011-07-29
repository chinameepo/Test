package com.esensoft.mutithreadsocket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.esensoft.mutithreadsocket.swing.MutiSwing;

/**
 *  编码UTF-8。
 *  一个客户端类，每个类都是一个线程
 * 新建一个图形界面和服务端通信
 * @version 3.0 ,2011-7-21
 * @author 邓超
 * @since jdk1.5
 * */

public class Client extends Thread {
	private MutiSwing showframe;
	private Socket client;
	private final String SERVER_ADD = "127.0.0.1";
	private final int PORT = 9000;
	private String name;
	/* 每次新建一个客户端实例，它的编号都不一样，用于客户端自己区分 */
	private static int numberId = 0;

	/**
	 * 构造函数，指定要连接的地址、端口号还有自己的名称
	 * */
	public Client(String name) {
		// 每次新建一个实例，编号都加1
		this.name = "客户端：" + numberId;
		numberId++;
	}

	/**
	 * 线程运行时的执行方法，就是通信
	 * @exception UnknownHostException
	 * @exception IOException
	 * */
	public void run() {

		try {
			client = new Socket(this.SERVER_ADD, this.PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream());
			out.print(name);
			showframe = new MutiSwing(this.name, "与服务端"+numberId + "聊天", out);
			String aline = "";
			do {
				aline = in.readLine();
				if (aline != null)
					showframe.recieve(aline);
			} while (!"bye".equals(aline));

			out.flush();
			out.close();
			in.close();
			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 新建多个客户请求线程,当然，如果请求成功的话，
	 * 就会有相应个数的服务端的图形界面也会显示出来
	 * */
	public static void main(String[] args) {
		int i;
		for(i=0;i<2;i++)
			(new Client("客户端")).start();
	}
}
