package chouc.jvm.memory;

import java.util.ArrayList;

/**
 *  64kb/50毫秒
 *	argu: -Xms100m -Xmx100m -XX:+UseSerialGC +PrintGCDetails
 *
 */
public class TestMemory {
	static class OOMObject {
		public byte[] placeholder = new byte[64 * 1024*40];
	}

	public static void fillHeap(int num) throws Exception {
		ArrayList<OOMObject> list = new ArrayList<OOMObject>();
		for (int i = 0; i < num; i++) {
			Thread.sleep(50);
			list.add(new OOMObject());
		}
		System.gc();
	}

	public static void main(String[] args) throws Exception {
		Thread.sleep(2000);
		System.out.println("start ......");
		fillHeap(100);
		Thread.sleep(2000);

		Runtime runtime = Runtime.getRuntime();

		System.out.println(runtime.freeMemory());
		// -Xmx100m
		System.out.println(runtime.maxMemory());
		// -Xms100m
		System.out.println(runtime.totalMemory());
	}
}
