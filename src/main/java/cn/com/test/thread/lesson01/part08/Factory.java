package cn.com.test.thread.lesson01.part08;

import java.util.ArrayList;
import java.util.List;

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
	 * Condition的应用
	 *
	 * 1个车间，车间内有0个产品<br/>
	 * 2个负责生产的工人，同时往1个车间中生产产品，分别生产5件产品<br/>
	 * 2个负责加工的工人，同时往1个车间中取出产品加工，分别加工5件产品<br/>
	 * 工人之间相互独立，但共用同一个车间的产品，存在生产者与消费者的关系<br/>
	 * 存在资源竞争<br/>
	 * 生产者、消费者之间通过Lock对象进行生产、消费的同步控制<br/>
	 * 通过Lock构造的Condition进行联动
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		int wsCount = 1, proCount = 0, wkCount = 2, wkProCount = 5;
		Factory factory = new Factory();
		factory.workshops = new Workshop[wsCount];
		for (int i = 0; i < wsCount; i++) {
			factory.workshops[i] = new Workshop(i, proCount);
		}
		factory.workers = new ArrayList<Thread>();
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Thread(new Worker(i, factory.workshops[0],
					Worker.PRODUCE, wkProCount)));
		}
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Thread(new Worker(i + 2,
					factory.workshops[0], Worker.PROCESS, wkProCount)));
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
