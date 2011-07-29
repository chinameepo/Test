package com.esensoft.getinfo;

/**
 * 测试类
 * 尝试着从http报文头里面截获有用的Url http-get方法一般的请求报文格式： GET sourceUrl HTTP/1.1
 * 也就是说只要将“GET”和“HTTP/1.1”之间的字符串截取，就可以获得url 采用方法是String
 * 类的sbuString（indexStart,indexEnd）
 * @version 1.0 ，2011-7-20
 * @author 邓超
 * @since jdk1.5
 */
public class SbuString {
	
	/**
	 * 从报文字符串中截取url地址的函数
	 * @return String
	 */
	public static String  sub(String httpString)
	{
		int indexHTTP;
		indexHTTP =httpString.lastIndexOf("HTTP/1.1");
		//报文都是以GET开头的，所以从第四个字符开始读取，到HTTP/1.1前面的字符停止
		return httpString.substring(4, indexHTTP-1);
	}
	
    /**
     * 主函数作为测试用，输入几个典型字符串，查看输出结果
     * */
	public static void main(String[] args) {
		String test1="GET /test/aa.txt HTTP/1.1Accept-Encoding: gzip, deflate";
		String test2="GET http://www.zhiliaowang.com/toupiaoceshi.asp?name=朱云翔 HTTP/1.1";
		SbuString sbuString =new SbuString();
		String s1 =sbuString.sub(test1);
		String s2 =sbuString.sub(test2);
		System.out.println(s1);
		System.out.println(s2);
	}
}
