/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-8 上午09:10:06
 * @since   jdk1.5
 * 测试String对象的splite方法，如果表达式正确会怎样
 */
public class TestStringSplit {
	public void splite(String s,String key)
	{
		String [] sub =new String[10];
		sub = s.split(key);
		printString(sub);
	}
	public void printString(String [] array)
	{
		for(String string : array)
		{
			System.out.println(string);
		}
	}
	public static void main(String[] args) {
		TestStringSplit split = new TestStringSplit();
		split.splite("aa:bb:cc", ":");
		split.splite("fefw", "==");
		split.splite("aa:::bcx",":");
	}
}


