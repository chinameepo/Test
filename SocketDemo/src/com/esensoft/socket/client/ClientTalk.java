package com.esensoft.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * һ���������Ŀͻ��ˣ��ͻ��˿��Խ����������ͬһ��������ͨ��
 * @version 1.0 ,201-7-19
 * @author �˳�
 * @since jdk1.5
 * */
public class ClientTalk {
	
   /**
    * ������������һ��socket���󣬲������������������
    * �ȴ���������Ӧ�����н���ʽ����
    * */
	public static void main(String[] args) {
		/*�ӷ���������������ַ����ݣ���ŵ�һ���ַ���������*/
		String readLine ="";
		try {
			//����socket����ָ����������ַ������˿�
			Socket socket =new Socket("127.0.0.1",8081);
			//��ϵͳ��׼���빹������뻺��������ڿͻ����ڿ���̨��������
			BufferedReader sInBufferedReader =new BufferedReader(new InputStreamReader(System.in));
		    //�½���һ��bufferedReader����,���Ǵӷ����������ȡ�ַ���
			BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    //һ��������󣬽�������ط�����
			PrintWriter printWriter =new PrintWriter(socket.getOutputStream());
			
			do {
				//��ȡһ�пͻ����Լ��ڿ���̨��������ַ���
				readLine =sInBufferedReader.readLine();
				//�������
				printWriter.println(readLine);
				//ˢ�������
				printWriter.flush();
				System.out.println("�ͻ��ˣ�"+sInBufferedReader.readLine()+(new Date()));
				System.out.println("��������"+bufferedReader.readLine()+(new Date()));
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
