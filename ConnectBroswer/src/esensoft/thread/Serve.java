package esensoft.thread;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心的服务器线程，线程启动后之后会循环监听指定端口。 如果有请求，就新建一个ResponTOBroswer应答对象，它继承了Runnable接口。
 * 把它扔给线程池，让它来处理和浏览器的交互，自己接着监听。 2.0版本是单线程的，3.0在此基础上加入线程池，实现多线程操作
 * 
 * @version 3.4,2011-7-27
 * @author 邓超
 * @since jdk1.5
 * */
public class Serve extends Thread {
	/* 服务端初始化时要监听的端口，一个端口不能被多个服务端占用 */
	private int port;
	/* 服务器默认的根目录 */
	private String root;
	
	/* 判断服务器是否在监听，默认值true，没有异常它都在监听 */
	private boolean listenning = true;

	public Serve(int port ,String root)
	{
		this.port =port;
		this.root =root;
	}
	/**
	 * 不建议这么用，c 盘是系统盘，对这个扇区重复擦除写入会缩减硬盘寿命
	 * */
	public Serve(int port )
	{
		this(port,"c://");
	}
	public Serve()
	{
		this(8080);
	}

/**
	 * 继承了Runnable接口，新建一个指定接口监听的ServerSocket对象
	 * 然后监听到请求，新建一个ResponTOBroswer应答线程放入线程池
	 * 
	 * @exception Exception
	 * */
	public void run() {
		ServerSocket server =null;
		ExecutorService execuor =null;
		Logger logger = LoggerFactory.getLogger(Serve.class);
		try {
			
			/**
			 * 使用的是带缓冲区的线程池。建议用工厂方法Executors生成线程池 并且尽量使用有系统已经预设好场景的线程池：
			 * Executors.newCachedThreadPool()（无界线程池，可以进行自动线程回收）
			 * Executors.newFixedThreadPool(int)（固定大小线程池）
			 * Executors.newSingleThreadExecutor()（单个后台线程）
			 * 首选是newCachedThreadPool
			 * */
			 execuor = Executors.newCachedThreadPool();
			 server = new ServerSocket(this.port);
			// 此处一定要while,不要用if,if只判断一次
			while (listenning) {
				ResponTOBroswer response = new ResponTOBroswer(server.accept(),
						root);
				execuor.execute(response);
			}
			
		} catch (Exception e) {
			logger.error("服务器初始化失败！");
			return;
		}finally{
			try {
				execuor.shutdown();
				server.close();
			} catch (Exception e2) {
			  logger.error("服务器关闭过程出现错误！");
			}
		}
	}
	public static void main(String[] args) {
		// 监听8066端口，并且默认服务器根目录是c盘
		Serve serve = new Serve(8066);
				serve.start();
	}
	
}
