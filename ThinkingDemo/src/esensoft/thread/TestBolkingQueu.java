package esensoft.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

/**
 * 测试阻塞队列的用法，查看输出结果
 * */
public class TestBolkingQueu {
	/**
	 * 在控制台上可以不断输入
	 * */
	static void getKey()
	{
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static void getKey(String messsage)
	{
		System.out.println(messsage);
		getKey();
	}
	/**
	 * 测试
	 * */
	static void test(String mes,BlockingQueue<LiftOff> queue)
	{
		System.out.println(mes);
		LifftOffRunner runner =new LifftOffRunner(queue);
		Thread thread =new Thread(runner);
		thread.start();
		
		for(int i=0;i<5;i++)
			runner.add(new LiftOff(5));
		getKey("press enter ("+mes+")");
		thread.interrupt();
		System.out.println("finish  "+mes+"  test");
		
	}
/**
 * main函数
 * */
	public static void main(String[] args) {
		//不限定大小
		test("LinkedBlockingQueue", new LinkedBlockingDeque<LiftOff>());
		//限定大小是3
		test("ArrayBlockingQueue", new ArrayBlockingQueue<LiftOff>(3));
		//默认大小是1
		test("syschronnizedQueue", new SynchronousQueue<LiftOff>());
		
		
	}
}
