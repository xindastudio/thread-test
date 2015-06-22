package cn.com.test.thread.lesson01.part03;

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
	 * synchronized关键字
	 *
	 * 1个车间，车间内有5个产品<br/>
	 * 车间的产品是现成的，在工人取之前已经生产好<br/>
	 * 2个工人，同时从1个车间中取出产品加工<br/>
	 * 工人之间相互独立，但共用同一个车间的产品<br/>
	 * 存在资源竞争，通过车间对象锁使产品加工流程正常进行<br/>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int wsCount = 1, proCount = 5, wkCount = 2;
		Factory factory = new Factory();
		factory.workshops = new Workshop[wsCount];
		for (int i = 0; i < wsCount; i++) {
			factory.workshops[i] = new Workshop(i, proCount);
		}
		factory.workers = new Thread[wkCount];
		for (int i = 0; i < wkCount; i++) {
			factory.workers[i] = new Thread(new Worker(i, factory.workshops[0]));
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
