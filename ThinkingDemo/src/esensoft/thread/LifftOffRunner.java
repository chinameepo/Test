package esensoft.thread;

import java.util.concurrent.BlockingQueue;

/**
 * 一个关于阻塞队列的演示类
 * */
public class LifftOffRunner implements Runnable{
	/*一个阻塞队列，可以容纳实现了runnable接口的对象LiftOff*/
	private BlockingQueue<LiftOff> rockets;
	
	/**
	 * 构造函数
	 * */
	public LifftOffRunner(BlockingQueue<LiftOff> queue)
	{
		rockets =queue;
	}
	/**
	 * 添加任务到队列中
	 *@param LiftOff lOff，要添加如队列的  LiftOff对象
	 *@exception InterruptedException
	 **/
	public void add(LiftOff lOff)
	{
		try {
			rockets.put(lOff);
		} catch (InterruptedException e) {
			System.out.println("Interrupted during put LiftOff to queue");
		}
	}
	/**
	 * 实现接口
	 * @exception InterruptedException，liftOff对象出列的时候可能会有问题
	 * 
	 * */
	public void run()
	{
		try {
			
			while(!Thread.interrupted())
			 {
				LiftOff rockLiftOff = rockets.take(); 
				rockLiftOff.run();
		    }
		  } catch (InterruptedException e) {
				e.printStackTrace();
		  }
		  System.out.println("exiting from take()");
	}
}
