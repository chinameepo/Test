package esensoft.thread;
/**
 * 展示runnable接口
 * */
public class LiftOff implements Runnable{
	protected int countDown =10;
	private static int taskCount =0;
	/*每被初始化一次，计数加1*/
	private final int id =taskCount++;
	
	public LiftOff()
	{
		
	}
	public LiftOff(int countDown)
	{
		this.countDown =countDown;
	}
	public String status()
	{
		return "#"+id+"("+(countDown>0 ? countDown:"LiftOff!"+"),");
		
	}
	public void run()
	{
		while (countDown-->0) {
			System.out.println(status());
			//暂停执行当前线程，去执行其他程序
			Thread.yield();
		}
	}

}
