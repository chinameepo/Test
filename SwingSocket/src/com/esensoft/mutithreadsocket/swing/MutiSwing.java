package com.esensoft.mutithreadsocket.swing;

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
 * 一个图形界面程序，由服务端和客户端共同使用 包括一个显示信息的界面，
 * 一个输入框和一个提交按钮， 编码UTF-8
 * @version 3.0 ,2011-7-21
 * @author 邓超
 * @since jdk1.5
 * */
public class MutiSwing extends JFrame implements ActionListener {

	private JTextArea textShow;
	private JTextField textInput;
	private JButton submit;
	/* ָ指定的输出流，每个socket对象输出到与之对应的输出流上� */
	private PrintWriter out;
	/* 为了区分，取个名字，例如“服务端”“客户端”，一般会加上编号 */
	private String name;

	/**
	 * 构造函数，第一个是自己的名字，第二个一般标题， 如果是客户端就显示与它连接的服务端的地址和端口 如果是服务端就显示与它连接的客户端编号
	 * */
	public MutiSwing(String name, String title, PrintWriter out) {
		super(name + "  " + title);
		this.setSize(new Dimension(300, 500));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 1));
		/* 显示信息的文本区域 */
		textShow = new JTextArea("");
		textShow.setEditable(false);
		// 有时候如果一行写满了，文本区不会自动换行，所以要设置自动换行
		textShow.setLineWrap(true);
		this.add(textShow);

		/* 信息显示的文本区单独占一个模块，下面的输入框和按钮占另外一块地方� */
		JPanel panelOperate = new JPanel();
		this.add(panelOperate, "South");

		textInput = new JTextField(20);
		textInput.setEditable(true);
		panelOperate.add(textInput);

		submit = new JButton("提交");
		submit.addActionListener(this);
		panelOperate.add(submit);

		this.out = out;
		this.name = name;
		this.setVisible(true);
	}

	/**
	 * 这里是接受消息，将从对方那里接受过来的消息显示
	 * */
	public void recieve(String message) {
		textShow.append(message + "\n");
	}

	/**
	 * 这里是发送消息，将自己要说的话写在自己的文本框同时 也通过输出流Out发送给对方接受
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("提交".equals(e.getActionCommand())) {
			if (this.out != null) {
				// 说的话，发给对方
				out.println(name + "   " + (new Date()) + "\n"
						+ textInput.getText());
				out.flush();
				// 自己说的话，在自己的显示文本框中显示
				textShow.append(name + (new Date()) + "\n"
						+ textInput.getText()+"\n");
				textInput.setText("");
			} else {
				textShow.append("通话结束\n");
			}
		}

	}

}
