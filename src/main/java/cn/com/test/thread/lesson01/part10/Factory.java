package cn.com.test.thread.lesson01.part10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 工厂类
 * 
 * @author wuliwei
 * 
 */
public class Factory {
	private Workshop[] workshops;
	private List<Thread> workers;

	private Factory() {

	}

	private void start() {
		for (Thread t : workers) {
			t.start();
		}
	}

	/**
	 * CountDownLatch的应用
	 *
	 * 在所有生产者、消费者都完成后，将输出当前车间的状态
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int wsCount = 1, proCount = 0, wkCount = 2, wkProCount = 5;
		final Factory factory = new Factory();
		factory.workshops = new Workshop[wsCount];
		for (int i = 0; i < wsCount; i++) {
			factory.workshops[i] = new Workshop(i, proCount);
		}
		CountDownLatch cdl = new CountDownLatch(4);
		factory.workers = new ArrayList<Thread>();
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Thread(new Worker(i, factory.workshops[0],
					Worker.PRODUCE, wkProCount, cdl), "worker" + i));
		}
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Thread(new Worker(i + 2,
					factory.workshops[0], Worker.PROCESS, wkProCount, cdl),
					"worker" + i));
		}
		factory.start();
		try {
			cdl.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		factory.workshops[0].stat();
	}
}
