package com.esensoft.swingsocket.swing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * һ��������ʾͨ����Ϣ��ͼ�ν��棬����һ���ı���ʾ����
 * һ���ı��������򣬺�һ���ύ��Ϣ��ť
 * ��������ǿͻ��˺ͷ�����ͨ�õ�
 * ʵ�ֽӿڣ�ActionListener��������ť�Ķ���
 * @version 2.0 ,2011-7-20
 * @author �˳�
 * @since  jdk1.5
 * */
public class ShowMessageFrame extends JFrame implements ActionListener{
     /*�������˽�б����ֱ��ǣ���ʾ�ı����������ı������ύ��ť*/
	private JTextArea textShow ;
	private JTextField textInput ;
	private JButton   submit;
	/*ָ���������������ʵ��ͨ�ŵ���Ϣ�������ʾ��textShow ��*/
	private PrintWriter out;
	/*�ƺ������Ƿ�����,name ���ǡ�������������֮��Ȼ*/
	private String name;
	
	/**
	 * ���캯����ָ���������������ǿͻ��˻��߷�������
	 * ָ��������IP��ַ�ͷ���˿ڣ��Լ�Ҫ�������Լ�����Ϣ���͹�ȥ�����������
	 * */
	public ShowMessageFrame(String name,String title ,PrintWriter out)
	{
		super(name+"  "+title);
		this.setSize(new Dimension(300, 500));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 1));
		/*����ı���������*/
		textShow = new JTextArea("");
		//��ֻ�Ǹ�����ʾ�������Ա༭
		textShow.setEditable(false);
		//�����Զ�����
		textShow.setLineWrap(true);
		this.add(textShow);
		
		/*�����ı����Ͱ�ť������*/
		JPanel panelOperate =new JPanel();
		this.add(panelOperate,"South");
		
		//����һ���������20����
		textInput =new JTextField(20);
		textInput.setEditable(true);
		panelOperate.add(textInput);
		
		//����ť��Ӽ���
		submit =new JButton("�ύ");
		submit.addActionListener(this);
		panelOperate.add(submit);
		//ָ�������
		this.out =out;
		this.name =name;
		this.setVisible(true);
	}

	/**
	 * ����������ȡ�Է����������ַ��������ҽ�����ʾ*/
	public void recieve(String message)
	{
		textShow.append(message+"\n");
	}
	/**
	 * ʵ����ӵļ����ӿ�,���Ǹ�������İ��
	 * ���Լ������ַ�������ȥ���������Լ��Ľ�����Ҳ��ʾ��Ϣ
	 * */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		if ("�ύ".equals(e.getActionCommand()))
		{
			if(this.out!=null)
			{
			    //ͨ�����������Ҫ˵�Ļ������Է�
			    out.println(name+"   "+(new Date())+"\n"+textInput.getText());
			    out.flush();
			    //����QQ�������Է�ͬʱ���Լ�����ʾ����Ҳ��ʾ�Լ�˵�Ļ�
			    textShow.append(name+(new Date())+"\n"+textInput.getText());
			    //���������Ϊ��
			    textInput.setText("");
			}
			else
			{
				textShow.append("�ö���������Ϊ�գ�\n");
			}
		}
		
	}
	
	
}
