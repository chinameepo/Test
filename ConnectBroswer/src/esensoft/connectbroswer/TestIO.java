package esensoft.connectbroswer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class TestIO {

	@Test
	public void testReadFileToBrowser() {
		ConnectBrosewer connectBrosewer  =new ConnectBrosewer(8066);
		File testFile =new File("c://test.txt");
	//	PrintWriter out =new PrintWriter(new OutputStream(testFile));
		//assertEquals(expected, connectBrosewer);
	}
}
