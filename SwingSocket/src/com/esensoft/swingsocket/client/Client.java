package com.esensoft.swingsocket.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.esensoft.swingsocket.swing.ShowMessageFrame;

/**
 * һ��ͻ��˵��࣬��Ҫָ���������IP��ַ�Ͷ˿ںţ� �ͷ��������ͨ��
 * 
 * @version 2.0,201-7-20
 * @author �˳�
 * @since jdk1.5
 * */
public class Client 
{
	/*2�����ֱ���ͨ���õ�ͼ�����Ϳͻ��˺�̨ͨ�ŵ�socket����*/
	private ShowMessageFrame frame;
	private Socket messageClient;
	
	/**
	 * ���캯��Լ���������ַ���˿ںţ�����Ҫȡ������*/
	public Client(String hostAddress,int port,String clientName)
	{
		try 
		{
			//�½�һ��socket����ʵ���Ժͷ���bl��
			messageClient = new Socket(hostAddress,port);
			//��b�������������
			BufferedReader bIn =new BufferedReader(new InputStreamReader(messageClient.getInputStream()));
		    PrintWriter  out =new PrintWriter(messageClient.getOutputStream(),true);
		    //���������Ϳ���������Ϊ���캯�����bһ��ͼ�ν�����
		    frame =new ShowMessageFrame(clientName, "������"+hostAddress+":"+port, out);
		    out.println(clientName);
		    frame.recieve("l��"+bIn.readLine());
		    //��ʼ����
		    String aline="";
		    do
		    {
		    	aline=bIn.readLine();
		    	frame.recieve(aline);
		    }while(!"bye".equals(aline));
		    
		    /*ͨ�����֮�󣬹ر������������socket���󣬶Ͽ�l��*/
		    bIn.close();
		    out.close();
		    messageClient.close();
		    
		}
		catch (Exception e)
		{
		e.printStackTrace();	
		}
	}
	/**
	 * ����bһ��ͻ���ʵ��
	 * */
	public static void main(String[] args) {
		new Client("localhost", 8084, "�ͻ�");
		
	}

}
