package cn.com.test.thread.lesson01.part04;

/**
 * 工厂类
 * 
 * @author wuliwei
 * 
 */
public class Factory {
	private Workshop[] workshops;
	private Thread[] workers;

	private Factory() {

	}

	private void start() {
		for (int i = 0; i < workers.length; i++) {
			workers[i].start();
		}
	}

	private boolean isFinished() {
		for (int i = 0; i < workers.length; i++) {
			if (workers[i].isAlive()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 类锁与对象锁的应用
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("-----------");
		test1();
		System.out.println("-----------");
		test2();
	}

	/**
	 * 1个车间，车间内有5个产品<br/>
	 * 车间的产品是现成的，在工人取之前已经生产好<br/>
	 * 2个工人，同时从1个车间中取出产品加工<br/>
	 * 工人之间相互独立，但共用同一个车间的产品<br/>
	 * 存在资源竞争，<br/>
	 * 0号工人通过车间对象锁同步产品加工流程<br/>
	 * 1号工人通过车间类锁同步产品加工流程<br/>
	 * 由于同步锁不一致，导致产品加工流程紊乱<br/>
	 */
	private static void test1() {
		int wsCount = 1, proCount = 5, wkCount = 2;
		Factory factory = new Factory();
		factory.workshops = new Workshop[wsCount];
		for (int i = 0; i < wsCount; i++) {
			factory.workshops[i] = new Workshop(i, proCount);
		}
		factory.workers = new Thread[wkCount];
		for (int i = 0; i < wkCount; i++) {
			factory.workers[i] = new Thread(new Worker(i, factory.workshops[0], i));
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

	/**
	 * 1个车间，车间内有5个产品<br/>
	 * 车间的产品是现成的，在工人取之前已经生产好<br/>
	 * 2个工人，同时从1个车间中取出产品加工<br/>
	 * 工人之间相互独立，但共用同一个车间的产品<br/>
	 * 存在资源竞争，通过车间类锁使产品加工流程正常进行<br/>
	 *
	 */
	private static void test2() {
		int wsCount = 1, proCount = 5, wkCount = 2;
		Factory factory = new Factory();
		factory.workshops = new Workshop[wsCount];
		for (int i = 0; i < wsCount; i++) {
			factory.workshops[i] = new Workshop(i, proCount);
		}
		factory.workers = new Thread[wkCount];
		for (int i = 0; i < wkCount; i++) {
			factory.workers[i] = new Thread(new Worker(i, factory.workshops[0], 1));
			factory.workers[i] = new Thread(new Worker(i, factory.workshops[0], i + 1));
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
