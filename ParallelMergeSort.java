import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {
    public static int Level;
    /// create a function that takes all data
    public static void parallelMergeSort(double[] array) {
        SortTask mainTask = new SortTask(array); //// the action class
       ForkJoinPool pool = new ForkJoinPool(Level);  //// the pool
        System.out.println(Level);

        pool.invoke(mainTask);                   //// pool invoke action
    }


    private static class SortTask extends RecursiveAction {
        private double[] array;

        public SortTask(double[] array) {
            this.array = array;
        }
        @Override
        protected void compute() {  //// logic
            if(array.length > 1) {
                int mid = array.length/2;

                // Obtain the first half
                double[] firstHalf = new double[mid];
                System.arraycopy(array, 0, firstHalf, 0, mid);

                // Obtain the second half
                double[] secondHalf = new double[array.length - mid];
                System.arraycopy(array, mid, secondHalf, 0, array.length - mid);

                // Recursively sort the two halves
                SortTask firstHalfTask = new SortTask(firstHalf);
                SortTask secondHalfTask = new SortTask(secondHalf);
                // Invoke declared tasks
                invokeAll(firstHalfTask, secondHalfTask);

                //Merge firstHalf with secondHalf into our array
                merge(firstHalf, secondHalf, array);
            }
        }
    }
    public static void merge(double[] firstHalf, double[] secondHalf, double[] array) {
        int currentIndexFirst = 0;
        int currentIndexSecond = 0;
        int currentIndexArray = 0;

        while(currentIndexFirst < firstHalf.length && currentIndexSecond < secondHalf.length) {
            if(firstHalf[currentIndexFirst] < secondHalf[currentIndexSecond]) {
                array[currentIndexArray] = firstHalf[currentIndexFirst];
                currentIndexArray++;
                currentIndexFirst++;
            } else {
                array[currentIndexArray] = secondHalf[currentIndexSecond];
                currentIndexArray++;
                currentIndexSecond++;
            }
        }

        while(currentIndexFirst < firstHalf.length) {
            array[currentIndexArray] = firstHalf[currentIndexFirst];
            currentIndexArray++;
            currentIndexFirst++;
        }

        while(currentIndexSecond < secondHalf.length) {
            array[currentIndexArray] = secondHalf[currentIndexSecond];
            currentIndexArray++;
            currentIndexSecond++;
        }
    }
}
