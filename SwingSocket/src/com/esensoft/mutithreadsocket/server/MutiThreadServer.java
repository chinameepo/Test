package com.esensoft.mutithreadsocket.server;

import java.net.ServerSocket;

/**
 * 一个服务端对象，负责监听端口 如果有请求，与它建立连接 但是只是建立连接，然后新建一个SOCKET作为代理， 把通信的事情交个代理，自己接着监听.
 * 基本上浏览器浏览文件也是如此
 * @version 3.0 ,2011-7-21
 * @author 邓超
 * @since jdk1.5
 */
public class MutiThreadServer {

	/**
	 * 主函数，实现类功能
	 * 
	 * @exception Exception
	 */
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		boolean listenning = true;

		// 新建监听对象
		try {
			serverSocket = new ServerSocket(9000);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果有请求，新建线程
		while (listenning) {
			try {
				(new ServThread(serverSocket.accept())).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 关闭
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
