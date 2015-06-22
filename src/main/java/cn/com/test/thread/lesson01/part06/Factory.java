package cn.com.test.thread.lesson01.part06;

import cn.com.test.thread.lesson01.part05.Worker;
import cn.com.test.thread.lesson01.part05.Workshop;

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

	private void join() {
		for (int i = 0; i < workers.length; i++) {
			try {
				workers[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * join 方法与 isAlive 方法的应用
	 *
	 * 之前的所有示例中都是通过isAlive来判断生产加工流程是否结束<br/>
	 * 由于isAlive是立即返回的，所以整个判断逻辑都是在不停地循环休眠、调用<br/>
	 *
	 * 现在这个示例是通过join来判断生产加工流程是否结束<br/>
	 * 由于join是阻塞的，所以整个判断逻辑不存在循环休眠的过程<br/>
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
			factory.workers[i].setDaemon(true);
		}
		factory.start();
		factory.join();
	}
}
