package cn.com.test.thread.lesson01.part11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工厂类
 * 
 * @author wuliwei
 * 
 */
public class Factory {
	private Workshop[] workshops;
	private List<Worker> workers;
	ThreadPoolExecutor executor;

	private Factory() {

	}

	private void start(CountDownLatch cdl) {
		int corePoolSize = 2;
		int maximumPoolSize = 3;
		long keepAliveTime = 0;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(2);
		// RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
		RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
		// RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();
		// RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
		executor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		for (Worker w : workers) {
			try {
				executor.execute(w);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("worker " + w.getNum() + " 中止!");
				cdl.countDown();
			}
			System.out.println("任务数 " + executor.getTaskCount() + ", 已完成任务数 "
					+ executor.getCompletedTaskCount() + ", 执行中任务数 "
					+ executor.getActiveCount() + ", 并发线程数 " + executor.getPoolSize()
					+ ", 等待队列大小 " + executor.getQueue().size());
		}
	}

	/**
	 * ThreadPoolExecutor的应用
	 *
	 * 1个车间，车间内有0个产品<br/>
	 * 3个负责生产的工人，同时往1个车间中生产产品，分别生产5件产品<br/>
	 * 3个负责加工的工人，同时往1个车间中取出产品加工，分别加工5件产品<br/>
	 * 线程池配置：2个核心线程，最大3个线程，队列大小为2，由此可知，线程池的容量为5<br/>
	 *
	 * 若拒绝任务处理策略为AbortPolicy中止当前提交的任务<br/>
	 * 当第6位工人加入队列时，将抛异常，车间的产品将遗留未加工的产品<br/>
	 *
	 * 若拒绝任务处理策略为CallerRunsPolicy在当前线程中直接执行当前任务<br/>
	 * 当第6位工人加入队列时，该工人将立即工作<br/>
	 *
	 * 若拒绝任务处理策略为DiscardOldestPolicy忽略最早的待处理任务<br/>
	 * 当第6位工人加入队列时，将有另外一个工人被静默移出队列，CountDownLatch无法集合，进程无法终结<br/>
	 *
	 * 若拒绝任务处理策略为DiscardPolicy忽略当前提交的理任务<br/>
	 * 当第6位工人加入队列时，将被静默忽略，CountDownLatch无法集合，进程无法终结<br/>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int wsCount = 1, proCount = 0, wkCount = 3, wkProCount = 5;
		final Factory factory = new Factory();
		factory.workshops = new Workshop[wsCount];
		for (int i = 0; i < wsCount; i++) {
			factory.workshops[i] = new Workshop(i, proCount);
		}
		CountDownLatch cdl = new CountDownLatch(wkCount * 2);
		factory.workers = new ArrayList<Worker>();
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Worker(i, factory.workshops[0],
					Worker.PRODUCE, wkProCount, cdl));
		}
		for (int i = 0; i < wkCount; i++) {
			factory.workers.add(new Worker(i + wkCount, factory.workshops[0],
					Worker.PROCESS, wkProCount, cdl));
		}
		factory.start(cdl);
		try {
			cdl.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		factory.workshops[0].stat();
		factory.executor.shutdownNow();
	}
}
