package cn.com.test.thread.lesson01.part09;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

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

	private boolean isFinished() {
		for (Thread t : workers) {
			if (t.isAlive()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * CyclicBarrier的应用
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
		CyclicBarrier cb = new CyclicBarrier(4, new Runnable() {
			public void run() {
				factory.workshops[0].stat();
			}
		});
		factory.workers = new ArrayList<Thread>();
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Thread(new Worker(i, factory.workshops[0],
					Worker.PRODUCE, wkProCount, cb), "worker" + i));
		}
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Thread(new Worker(i + 2,
					factory.workshops[0], Worker.PROCESS, wkProCount, cb),
					"worker" + i));
		}
		factory.start();
		while (!factory.isFinished()) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
