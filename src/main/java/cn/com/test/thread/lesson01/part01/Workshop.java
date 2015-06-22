package cn.com.test.thread.lesson01.part01;

/**
 * 车间类
 * 
 * @author wuliwei
 * 
 */
public class Workshop {
	private int num;
	private int productCount;

	/**
	 * @param num
	 *            车间号
	 * @param productCount
	 *            产品数量
	 */
	public Workshop(int num, int productCount) {
		this.productCount = productCount;
		this.num = num;
	}

	/**
	 * @return 车间号
	 */
	public int getNum() {
		return num;
	}

	/**
	 * 返回该车间当前生产的产品序列号
	 * 
	 * @return int
	 */
	public int next() {
		int num = productCount;
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productCount--;
		return num;
	}
}
