package cn.com.test.thread.lesson01.part07;

import java.util.ArrayList;
import java.util.Arrays;
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
	 * wait-notify机制应用<br/>
	 * 调用对象的wait-notify-notifyAll，当前线程必须拥有该对象的锁<br/>
	 *
	 * 之前的示例，车间中的产品在加工前已经生产好，现在这个示例，产品都是未生产的<br/>
	 *
	 * 1个车间，车间内有0个产品<br/>
	 * 2个负责生产的工人，同时往1个车间中生产产品，分别生产5件产品<br/>
	 * 2个负责加工的工人，同时往1个车间中取出产品加工，分别加工5件产品<br/>
	 * 工人之间相互独立，但共用同一个车间的产品，存在生产者与消费者的关系<br/>
	 * 存在资源竞争<br/>
	 *
	 * 本示例的目的是为了演示wait-notify机制，同时为了简化示例，<br/>
	 * 车间中的产品生产、消费同步采用BlockingQueue来处理<br/>
	 * 生产者之间通过生产锁同步，消费者之间通过消费锁同步<br/>
	 * 生产者与消费者之间通过生产锁、消费锁进行联动<br/>
	 * 生产者生产1个产品，消费者消费1个产品，依次进行<br/>
	 *
	 * 实际应用中，<br/>
	 * 生产者与消费者只要一把锁就足够了，如果资源是由BlockingQueue接管，连这一把锁都可以免了，<br/>
	 * 而在wait-notify机制中，<br/>
	 * 生产者与消费者必须拥有不同的锁用于联动，因为notify机制唤醒的线程是随机的，<br/>
	 * 如果只有一把锁，每次唤醒的都是生产者或者每次唤醒的都是消费者，那么整个流程就假死了<br/>
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
		System.out.println(Arrays.toString(factory.workshops[0].seq.toArray()));
	}
}
