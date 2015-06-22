package cn.com.test.thread.lesson01.part07;

/**
 * 工人类
 * 
 * @author wuliwei
 * 
 */
public class Worker implements Runnable {
	/**
	 * 工人类型：生产
	 */
	public static final String PRODUCE = "0";
	/**
	 * 工人类型：加工
	 */
	public static final String PROCESS = "1";
	private int num;
	private Workshop ws;
	private String type;
	private int count;

	/**
	 * @param num
	 *            工号
	 * @param ws
	 *            车间
	 * @param type
	 *            工人类型
	 * @param count
	 *            可生产或加工的量
	 */
	public Worker(int num, Workshop ws, String type, int count) {
		this.num = num;
		this.ws = ws;
		this.type = type;
		this.count = count;
	}

	/**
	 * @return boolean 加工是否完成，是：true，否：false
	 */
	private boolean process() {
		int proNum = ws.next();
		System.out.println("worker" + num + " 加工 workshop" + ws.getNum()
				+ " 's product" + proNum);
		count--;
		return 0 >= count;
	}

	/**
	 * @return boolean 生产是否完成，是：true，否：false
	 */
	private boolean produce() {
		int proNum = ws.add();
		System.out.println("worker" + num + " 生产 workshop" + ws.getNum()
				+ " 's product" + proNum);
		count--;
		return 0 >= count;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Runnable#run()
	 */
	public void run() {
		if (PRODUCE.equals(type)) {
			while (!produce()) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (PROCESS.equals(type)) {
			while (!process()) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
