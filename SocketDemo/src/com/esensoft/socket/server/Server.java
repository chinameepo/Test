package com.esensoft.socket.server;
import java.io.IOException;
import java.net.ServerSocket;

import com.esensoft.socket.server.ServerThread;
/**
 * һ������֧�ֶ��̵߳ķ�������
 * ����һ��serversocket��� �˿ڽ��м���
 * �����������ô�½�һ���̣߳�
 * ���Ǹ��̴߳�������������û�����ͨ��
 * @version 1.0 ,2011-7-19
 * @author �˳�
 * @since jdk1.5
 * */
public class Server 
{
  /*��һ����̬������¼�ͻ���������*/
  private static int clientCount;
  
  /**������������һ��serverSocket����ʵ���������˿�*/
  public static void main(String[] args) {
	ServerSocket serverSocket =null;
	//�жϷ������Ƿ��ڼ���
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
	//ʵ����֮�����Ǵ��ڼ���״̬
	while (listening)
	{
		try 
		 {
			 //����һ��socket�̵߳�ʵ��������������
			(new ServerThread(serverSocket.accept(),clientCount)).start();
			clientCount++;
		} 
		 catch (IOException e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//�رշ���������
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
