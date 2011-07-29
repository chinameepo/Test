package com.esensoft.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * һ����������� �Ϳͻ��˵�ͨ����
 * ������ÿ�μ�����һ�����󣬾���Ӧ��
 * ���ҽ�����������йܸ������
 * ����ฺ��Ϳͻ��˽��н���
 * @version 1.0,2011-7-19
 * @author �˳�
 * @since jdk1.5
 * */
public class ServerThread extends Thread
{
	/*�뱾�߳���ص�socket����*/
 private Socket socket = null;
    /*�뱾�߳���صĿͻ��˵�����*/
 private int clientTotal;
 
 /**
  * ���캯��
  * ָ��һ��socket�࣬Ȼ��ͻ�����ÿ�½�һ��Ҫ��1*
  */
 public ServerThread (Socket clientSocket ,int number)
 {
	 this.socket =clientSocket;
	 this.clientTotal =number+1;
 }
 
 /**�߳����run�������涨���̵߳Ķ���
  * �߳���������/������Ϳͻ��˽��н���
  * ���������ʾ�ڿ���̨*/
 public void run()
 {
	 String readLine="";
	
	 
	 try {
		  //����̨�µ��ļ�����������������ȡ����̨�·������û��������Ϣ
		 BufferedReader sBufferedReader =new BufferedReader(new InputStreamReader(System.in));
		  //�ļ�����������������ȡ�ͻ��˷���������Ϣ
		BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    PrintWriter printWriter =new PrintWriter(socket.getOutputStream());
	    
	    
	    
	    
	    do 
	    {
	    	//�ӿͻ�����ӵ���Ϣ�ˣ��ڿ���̨����ʾ����
		    System.out.println("�ͻ���"+this.clientTotal+sBufferedReader.readLine());
	    	//���ŷ�����Ҫ�ظ���Ϣ,�ڿ���̨���룬���������ͬ���ڿ���̨���
			readLine =sBufferedReader.readLine();
			printWriter.println(readLine);
			//Ҫˢ��
			printWriter.flush();
			System.out.println("��������"+readLine+(new Date()));
			
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

