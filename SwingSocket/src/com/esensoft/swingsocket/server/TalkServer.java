package com.esensoft.swingsocket.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.esensoft.swingsocket.swing.ShowMessageFrame;

/**
 * һ��������࣬Լ���˿ں��ϼ���
 * ��������󣬾� ����һ��socket����Ϳͻ���ͨ��
 * */
public class TalkServer {
	/*����˽�б����ֱ���ͼ�ν���ʵ����
	 * ServerSocket��ʵ���Լ����������������� Socketʵ��*/
	private ShowMessageFrame frame;
	private ServerSocket serverSocket;
	private Socket socket;
	
	/**���캯����Լ���˿ںţ�����Ҫȡ������*/
	public TalkServer(int port ,String hostName)
	{
		try 
		{
			//����˵Ķ˿ں��趨
			serverSocket =new ServerSocket(port);
			//����������½�һ��secket����ʵ��
			socket =serverSocket.accept();
			//�������������
			BufferedReader bIn =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
			//�½�һ��ͼ�ν��棬
			frame =new ShowMessageFrame(hostName, "�˿ںţ�"+port, out);
			out.println(hostName);
			frame.recieve("����"+bIn.readLine());
			out.flush();
			//��ʼ����
		    String aline="";
		    do
		    {
		    	aline=bIn.readLine();
		    	frame.recieve(aline);
		    }while(aline!=null);
		    
		    /*ͨ�����֮�󣬹ر��������������socket���Ͽ�����*/
		    bIn.close();
		    out.close();
		    socket.close();
		    serverSocket.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ������������һ�������ʵ��
	 * */
	public static void main(String[] args) {
		new TalkServer( 8084, "�����");
		
	}
	

}
