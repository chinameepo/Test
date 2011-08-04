/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-2 上午9:17:11
 * @since   jdk1.5
 * 看看一些类型初始化之后，不赋值，结果会如何
 * 
 */
public class TestStringInit {
	private String s;
	private int i ;
	private char c;
	
	public String getS() {
		return s;
	}

	public int getI() {
		return i;
	}

	public char getC() {
		return c;
	}

	public static void main(String[] args) {
		
		TestStringInit testStringInit = new TestStringInit();
		System.out.println("char init is :"+testStringInit.getC());
		System.out.println("String init is:"+testStringInit.getS());
		System.out.println("int init is:"+testStringInit.getI());
		
		
		
	}

}


