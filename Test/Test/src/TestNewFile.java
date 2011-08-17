import java.io.File;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-5 下午01:35:48
 * @since   jdk1.5
 * 测试多个对象同时对一个文件句柄进行引用会不会出问题
 * 
 */
public class TestNewFile {
	public void test()
	{
		for(int i=0;i<2000;i++)
		{
			File file = new File("c://aa.png");
			System.out.println("文件是否存在："+file.exists());
			System.out.println("file :"+i);
		}
	}
	public static void main(String[] args) {
		TestNewFile test = new TestNewFile();
		test.test();
	}

}


