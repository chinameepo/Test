package esensoft.pool.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 试验线程池。类中建立了多个不同的线程池，并且对每个线程池做了测试
 * 有固定大小的线程池，单个线程的线程池，大小可以改变带缓冲的线程池、自定义线程池等
 * excutor接口的用法 可以建立一个类似数据库连接池的线程池来执行任务。
 * 接口提供一种将任务提交与每个任务将如何运行的机制
 * （包括线程使用的细节、调度等）分离开来的方法。
 * @version 1.0 ,2011-7-22
 * @author 邓超
 * @since jdk1.5
 * */
public class ExcutorTest {
	/**
	 * 创建固定大小的线程池，大小都是固定的，
	 * 当要加入的池的线程（或者任务）超过池最大尺寸时候，则入此线程池需要排队等待。
	 * 注意，Executor是没有关闭线程池的shutdown方法的
	 * @param nThread
	 *            ，规定这个线程池的大小
	 * */
	public void fixedSize(int nThread) {
		// 使用JDK内置的线程池，固定大小的，创建具有5个内置线程的池
		Executor executor = Executors.newFixedThreadPool(nThread);
		for (int i = 0; i < 50; i++) {
			RunableTest test = new RunableTest(i + "");
			executor.execute(test);
		}
	}
	/**
	 * 创建单任务线程池，一次只能进入一个线程，
	 * 要加入一个以上的线程的时候，后面的线程要放在队列里面等待
	 * */
	public void singleThread() {
        
        /*Executors.newCachedThreadPool()方法，返回一个static ExecutorService的接口对象
         *ExecutorService以及其父接口Executor，都有shoutdown方法 
         **/
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			RunableTest test = new RunableTest(i + "");
			executor.execute(test);
		}
		executor.shutdown();
	}

	/**
	 * 创建大小可以改变的线程池大小，可以根据情况对线程池大小进行改变
	 * 
	 * */
	public void cachedThread() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			RunableTest test = new RunableTest(i + "");
			executor.execute(test);
		}
		executor.shutdown();
	}

	/**
	 * 创建带延迟的线程池，运行结果就是1-2可以运行，3-4在 等待，不显示
	 * */
	public void secheduleThread() {
		// 创建一个可以设置延时的对象
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
		RunableTest test1 = new RunableTest(1 + "");
		RunableTest test2 = new RunableTest(2 + "");
		RunableTest test3 = new RunableTest(3 + "");
		RunableTest test4 = new RunableTest(4 + "");
		// 非延时的线程
		pool.execute(test1);
		pool.execute(test2);
		// 延时的线程
		pool.execute(test3);
		pool.execute(test4);
		pool.schedule(test3, 20, TimeUnit.MILLISECONDS);
		pool.schedule(test4, 20, TimeUnit.MILLISECONDS);
		// 关闭线程池
		pool.shutdown();
	}
	/**
	 * 创建自定义线程池
	 * 使用队列容纳线程，采用先进先出的规则
	 * 这里是默认2个核心线程，就这两个 线程会把所有的任务都做完
	 * 输出显示里就这两个线程在运行
	 * */
	public void customizeThread()
	{
		//创建等待队列,容量是20.BlockingQueue是个接口类型
		BlockingQueue<Runnable> bQueue =new ArrayBlockingQueue<Runnable>(20);
		/*
		 * 创建一个线程池，4个参数是：核心线程数，最大线程数，
		 * 当线程数大于核心时，终止目前多余的空闲线程等待新任务的最长时间
		 * 时间单位、线程存放的队列
		 * */
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,3,2,TimeUnit.MILLISECONDS,bQueue);
		//创建实现了Runnable接口的对象，包括thread线程对象
		RunableTest test1 = new RunableTest(1 + "");
		RunableTest test2 = new RunableTest(2 + "");
		RunableTest test3 = new RunableTest(3 + "");
		RunableTest test4 = new RunableTest(4 + "");
		//放入线程池，就用两个线程来执行四个任务
		poolExecutor.execute(test1);
		poolExecutor.execute(test2);
		poolExecutor.execute(test3);
		poolExecutor.execute(test4);
		System.out.println(poolExecutor.getCorePoolSize());
		poolExecutor.shutdown();
	}
	
	public static void main(String[] args) {
		ExcutorTest test = new ExcutorTest();
		// test.fixedSize(5);
		// test.singleThread();
		//test.cachedThread();
		// test.secheduleThread();
		test.customizeThread();
	}

}
