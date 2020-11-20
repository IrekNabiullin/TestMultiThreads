import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinPool01 {
    public static void main(String[] args) {
        int[] nums = new int[] {5, 4, 6, 2, 8, 7, 9, 1, 3};

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Void> t = pool.submit(new ForkJoinFastSort(nums, 0, 8));
        t.join();
        for (int i : nums) {
            System.err.print(i);
        }
    }

    /*
     * fork/join
     */
    @SuppressWarnings("serial")
    static class ForkJoinFastSort extends RecursiveAction {
        private int[] nums;
        private int start;
        private int end;

        ForkJoinFastSort(int[] nums, int start, int end) {
            this.nums = nums;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            int i = start;
            int j = end;
            int swap = -1;

            while (i < j) {
                while (i < j && nums[i] <= nums[j]) j--;
                if (i < j) {
                    swap = nums[i];
                    nums[i] = nums[j];
                    nums[j] = swap;
                }
                while (i < j && nums[i] < nums[j]) i++;
                if (i < j) {
                    swap = nums[i];
                    nums[i] = nums[j];
                    nums[j] = swap;
                }
            }
            List<ForkJoinTask<Void>> tasks = new LinkedList<>();

            if (i - start > 1) {
                tasks.add(new ForkJoinFastSort(nums, 0, i - 1).fork());
            }
            if (end - j > 1) {
                tasks.add(new ForkJoinFastSort(nums, j + 1, end).fork());
            }
            for (ForkJoinTask<Void> task : tasks) {
                task.join();
            }
        }
    }

    public static void display(int[] nums) {
        for (int i : nums) {
            System.err.print(i);
        }
        System.err.println();
    }
}

