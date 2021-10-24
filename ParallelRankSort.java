import java.util.Random;

class ParallelRankSort {

	public static  int elemQuantity;
	public static int threadNum;
	public static int blockSize;
	public static double[] resultArray;
	public static double []readArray;
	public ParallelRankSort(int size, int levelofparallelism , double [] Weights)
	{
		elemQuantity=size;
		threadNum=levelofparallelism;
		resultArray = new double[elemQuantity];
		blockSize = elemQuantity / threadNum;
		readArray = Weights.clone();
	}

	public double[] start()
	{
		// Start threads
		ParallelRankThread threads[] = new ParallelRankThread[threadNum];
		for(int i = 0, j = 0; i < threadNum; i++, j += blockSize) {
			threads[i] = new ParallelRankThread(readArray, resultArray, threadNum, i);
			threads[i].start();
		}

		// Join threads
		try {
			for(int i = 0; i < threadNum; i++) {
				threads[i].join();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
/*		System.out.println();
		System.out.println();
		for(int i = 0; i < elemQuantity; i++) {
			System.out.print(resultArray[i] + " ");
			if (i % 10 == 0) {
				System.out.println();
			}
		}
		System.out.println();
		System.out.println();*/
		return resultArray;
	}

}
