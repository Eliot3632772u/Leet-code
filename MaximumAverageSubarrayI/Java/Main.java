public class Main {
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 12, -5, -6, 50, 3};
        int k = 4;
        double result = Solution.findMaxAverage(nums, k);
        System.out.println("Maximum average subarray of length " + k + ": " + result);
    }
}
