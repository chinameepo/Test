/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-3 下午03:04:28
 * @since   jdk1.5
 * 测试String类对象的去空格的方法，trim()的使用方法！
 * 结果它只是忽略了前面的空白和尾巴的空白，并没有去空格的功能！
 */
public class TestTrim {
    public void stringTrim(String source)
    {
    	String result;
    	System.out.println("the source string is:"+source);
    	result =source.trim();
    	System.out.println("the trimed string is"+result);
    	System.out.println("using funtion trim:"+source.trim());
    }
    public static void main(String[] args) {
		TestTrim testTrim = new TestTrim();
		testTrim.stringTrim("        sxtew. j   a  vv   a");
		testTrim.stringTrim("          a   a    a        a.t     x             t");
		testTrim.stringTrim("                         %　　％％　　％％ｆｅｉｆｅｎ　　　．　ｗｅ　　ｗｔｅｗ　ｒｗ２　");
	}
}


