package cn.com.test.thread.lesson01.part04;

/**
 * 工人类
 * 
 * @author wuliwei
 * 
 */
public class Worker implements Runnable {
	private int num;
	private Workshop ws;
	private int flag;

	/**
	 * @param num
	 *            工号
	 * @param ws
	 *            车间
	 * @param flag
	 *            加工方式标识
	 */
	public Worker(int num, Workshop ws, int flag) {
		this.num = num;
		this.ws = ws;
		this.flag = flag;
	}

	/**
	 * @return boolean 加工是否完成，是：true，否：false
	 */
	private boolean process() {
		// flag为0，用对象锁；1用类锁；其他用类锁
		int proNum = 0 == flag ? ws.next0() : 1 == flag ? Workshop
				.next1(ws) : Workshop.next2(ws);
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
