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
 * 一个用来显示通信信息的图形界面，包括一个文本显示区域
 * 一个文本输入区域，和一个提交信息按钮
 * 这个界面是客户端和服务器通用的
 * 实现接口：ActionListener，监听按钮的动作
 * @version 2.0 ,2011-7-20
 * @author 邓超
 * @since  jdk1.5
 * */
public class ShowMessageFrame extends JFrame implements ActionListener{
     /*三个组件私有变量分别是：显示文本区、输入文本区和提交按钮*/
	private JTextArea textShow ;
	private JTextField textInput ;
	private JButton   submit;
	/*指定输出流，用它来实现通信的信息输出，显示在textShow 上*/
	private PrintWriter out;
	/*称呼，我是服务器,name 就是“服务器”，反之亦然*/
	private String name;
	
	/**
	 * 构造函数，指定这个界面的名字是客户端或者服务器，
	 * 指定标题是IP地址和服务端口，以及要用来将自己的消息发送过去的输出流对象
	 * */
	public ShowMessageFrame(String name,String title ,PrintWriter out)
	{
		super(name+"  "+title);
		this.setSize(new Dimension(300, 500));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 1));
		/*输出文本区的设置*/
		textShow = new JTextArea("");
		//它只是负责显示，不可以编辑
		textShow.setEditable(false);
		//允许自动换行
		textShow.setLineWrap(true);
		this.add(textShow);
		
		/*输入文本区和按钮的设置*/
		JPanel panelOperate =new JPanel();
		this.add(panelOperate,"South");
		
		//设置一次最多输入20个字
		textInput =new JTextField(20);
		textInput.setEditable(true);
		panelOperate.add(textInput);
		
		//给按钮添加监听
		submit =new JButton("提交");
		submit.addActionListener(this);
		panelOperate.add(submit);
		//指定输出流
		this.out =out;
		this.name =name;
		this.setVisible(true);
	}

	/**
	 * 输入流，读取对方发过来的字符串，并且将它显示*/
	public void recieve(String message)
	{
		textShow.append(message+"\n");
	}
	/**
	 * 实现添加的监听接口,这是负责输入的版块
	 * 将自己发的字符串发过去，并且在自己的界面上也显示信息
	 * */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		if ("提交".equals(e.getActionCommand()))
		{
			if(this.out!=null)
			{
			    //通过输出流，将要说的话发给对方
			    out.println(name+"   "+(new Date())+"\n"+textInput.getText());
			    out.flush();
			    //类似QQ，发给对方同时，自己的显示区域也显示自己说的话
			    textShow.append(name+(new Date())+"\n"+textInput.getText());
			    //接着输入框为空
			    textInput.setText("");
			}
			else
			{
				textShow.append("该对象的输出流为空！\n");
			}
		}
		
	}
	
	
}
