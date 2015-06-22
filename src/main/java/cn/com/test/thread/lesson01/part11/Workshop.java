package cn.com.test.thread.lesson01.part11;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 车间类
 * 
 * @author wuliwei
 * 
 */
public class Workshop {
	private int num;
	private int productCount;
	private BlockingQueue<Integer> serialNums;
	private Lock lock;

	/**
	 * @param num
	 *            车间号
	 * @param productCount
	 *            产品数量
	 */
	public Workshop(int num, int productCount) {
		this.productCount = productCount;
		this.num = num;
		serialNums = new LinkedBlockingQueue<Integer>();
		for (int i = 0; i < productCount; i++) {
			serialNums.offer(i);
		}
		lock = new ReentrantLock();
	}

	/**
	 * @return 车间号
	 */
	public int getNum() {
		return num;
	}

	/**
	 * 向该车间添加生产的产品
	 * 
	 * @return int
	 */
	public int add() {
		lock.lock();
		try {
			productCount++;
			serialNums.offer(productCount);
			return productCount;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 返回该车间当前生产的产品序列号
	 * 
	 * @return int
	 */
	public int next() {
		lock.lock();
		try {
			if (serialNums.isEmpty()) {
				return -1;
			}
			int curSerialNum = serialNums.poll();
			return curSerialNum;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 打印该车间的生产状况
	 */
	public void stat() {
		System.out.println(Thread.currentThread().getName()
				+ " : workshop produced "
				+ productCount
				+ " products and "
				+ serialNums.size()
				+ " remains."
				+ (serialNums.size() > 0 ? Arrays
						.toString(serialNums.toArray()) : ""));
	}
}
