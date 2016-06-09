package folkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Calculator extends RecursiveTask {

	private static final int THRESHOLD = 10;
	private int start;
	private int end;

	public Calculator(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		if ((end - start) < THRESHOLD) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			int middle = (start + end) / 2;
			Calculator left = new Calculator(start, middle);
			Calculator right = new Calculator(middle + 1, end);
			left.fork();
			right.fork();

			sum = (Integer) left.join() + (Integer) right.join();
		}
		return sum;
	}

	public static void main(String[] args) {
		Calculator task = new Calculator(1, 100);
		ForkJoinPool pool = new ForkJoinPool();
		System.out.println(task.getRawResult());
		pool.invoke(task);
		System.out.println(task.getRawResult());
	}

}