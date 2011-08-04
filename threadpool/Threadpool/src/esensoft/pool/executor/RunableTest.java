package esensoft.pool.executor;
/**
 *一个类，算是一个任务，实现了Runnable接口
 *显示当前任务的运行状态
 *@version 1.0 ,2011-7-22
 *@author 邓超
 *@since jdk1.5
 */
public class RunableTest implements Runnable {
    private String nameString;
    /**
     * 带参数的构造函数和无参数的构造函数
     * */
	public RunableTest(String name)
	{
		this.nameString =name;
	}
	public RunableTest()
	{
		this.nameString ="runNableTest";
	}
	/**
	 * 输出当前任务的运行信号
	 * */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"正在执行");
	}
 
}
