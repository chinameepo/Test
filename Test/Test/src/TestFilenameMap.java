import java.io.File;
import java.net.FileNameMap;

import javax.activation.FileTypeMap;

/**
 *All right resrvered esensoft(2011)
 * @author  邓超   deng.369@gmail.com
 * @version 1.0,创建时间：2011-8-8 上午10:24:45
 * @since   jdk1.5
 * 接口filetypeMap的测试，不过是个接口，算了吧。
 */
public class TestFilenameMap {
	public void getType(String filePath)
	{
		FileTypeMap map  =new FileTypeMap() {
			
			@Override
			public String getContentType(String filename) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContentType(File file) {
				// TODO Auto-generated method stub
				return null;
			}
		};

	}

}


