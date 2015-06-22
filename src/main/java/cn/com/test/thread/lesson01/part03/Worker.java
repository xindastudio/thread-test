package cn.com.test.thread.lesson01.part03;

/**
 * 工人类
 * 
 * @author wuliwei
 * 
 */
public class Worker implements Runnable {
	private int num;
	private Workshop ws;

	/**
	 * @param num
	 *            工号
	 * @param ws
	 *            车间
	 */
	public Worker(int num, Workshop ws) {
		this.num = num;
		this.ws = ws;
	}

	/**
	 * @return boolean 加工是否完成，是：true，否：false
	 */
	private boolean process() {
		int proNum = ws.next();
		if (1 > proNum) {
			return true;
		}
		System.out.println("worker" + num + " process workshop" + ws.getNum()
				+ " 's product" + proNum);
		return 1 >= proNum;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Runnable#run()
	 */
	public void run() {
		while (!process()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
