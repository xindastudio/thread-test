package cn.com.test.thread.lesson01.part07;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
	private Object empty;
	private Object notEmpty;
	public List<String> seq;

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
		empty = "empty";
		notEmpty = "notEmpty";
		seq = new ArrayList<String>();
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
		int curSerialNum;
		synchronized (empty) {
			while (!serialNums.isEmpty()) {
				try {
					empty.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			productCount++;
			curSerialNum = productCount;
			seq.add("add " + curSerialNum);
			serialNums.offer(curSerialNum);
		}
		synchronized (notEmpty) {
			notEmpty.notify();
		}
		return curSerialNum;
	}

	/**
	 * 返回该车间当前生产的产品序列号
	 * 
	 * @return int
	 */
	public int next() {
		int curSerialNum;
		synchronized (notEmpty) {
			while (serialNums.isEmpty()) {
				try {
					notEmpty.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			curSerialNum = serialNums.poll();
			seq.add("next " + curSerialNum);
		}
		synchronized (empty) {
			empty.notify();
		}
		return curSerialNum;
	}
}
