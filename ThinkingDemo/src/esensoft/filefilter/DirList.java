package esensoft.filefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 一个目录列表器，实现的是文件过滤
 * 获得一个受限制的列表,相当于ls-l
 * */
public class DirList {
	public static void main(String[] args) {
		File pathFile =new File(".");
		String []list;
		if(args.length==0)
			list = pathFile.list();
		else {
			list =pathFile.list(new DirFilter(args[0]));
		}
		Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
		for(String dirItem:list)
			System.out.println(dirItem);
		
	}
	class DirFilter implements FilenameFilter
	{
		private Pattern pattern ;
		public DirFilter(String regex)
		{
			pattern =Pattern.compile(regex);
			
		}
		@Override
		public boolean accept(File dir, String name) {
			return pattern.matcher(name).matches();
		}
		
	}

}
