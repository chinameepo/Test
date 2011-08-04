package esensoft.server;


import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-2 下午06:39:11
 * @since   jdk1.5
 * 类说明
 */
public class Servers extends Thread{
	/* 服务端初始化时要监听的端口，一个端口不能被多个服务端占用 */
	private int port;
	/* 服务器默认的根目录 */
	private String root;
	
	/* 判断服务器是否在监听，默认值true，没有异常它都在监听 */
	private boolean listenning = true;

	public Servers(int port ,String root)
	{
		this.port =port;
		this.root =root;
	}
	/**
	 * 不建议这么用，c 盘是系统盘，对这个扇区重复擦除写入会缩减硬盘寿命
	 * */
	public Servers(int port )
	{
		this(port,"c://");
	}
	public Servers()
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
		Logger logger = LoggerFactory.getLogger(Servers.class);
		try {
			/**
			 * 建议用工厂方法Executors生成线程池 并且尽量使用有系统已经预设好场景的线程池：
			 * Executors.newCachedThreadPool()（无界线程池，可以进行自动线程回收）
			 * Executors.newFixedThreadPool(int)（固定大小线程池）
			 * Executors.newSingleThreadExecutor()（单个后台线程）
			 * 首选是newCachedThreadPool
			 * */
			ExecutorService  execuor = Executors.newCachedThreadPool();
			 server = new ServerSocket(this.port);
			/* 此处一定要while,不要用if,if只判断一次。*/
			while (listenning) {
				Response response = new Response(server.accept(),
						root);
				execuor.execute(response);
			}
		} catch (Exception e) {
			logger.error("服务器初始化失败！");
			return;
		}finally{
			try {
				server.close();
			} catch (Exception e2) {
			 logger.error("服务器关闭过程出现错误！");
			}
		}
	}
	
	public static void main(String[] args) {
		Servers serve = new Servers(8066);
				serve.start();
	}
	

}


