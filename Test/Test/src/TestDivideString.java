/**
 * All right resrvered esensoft(2011)
 * 
 * @author 邓超 deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-18 下午05:08:28
 * @since jdk1.6 把一个字符串分为两部分。
 */
public class TestDivideString {
	private String url = "/newtree/information_schema==CHARACTER_SETS.action";

	public void divide() {
		int key = url.lastIndexOf('/');
		String keyString = url.substring(key + 1);
		System.out.println("截断后的字符串是：" + keyString);
		String[] infoStrings = keyString.split("==");
		System.out.println(infoStrings[0]);
		System.out.println(infoStrings[1]);
	}

	public static void main(String[] args) {
		TestDivideString divideString = new TestDivideString();
		divideString.divide();
	}

}
