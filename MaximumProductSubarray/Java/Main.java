public class Main {
    public static void main(String[] args) {

        Solution solution = new Solution();
        int[] nums = {2, 3, -2, 4};

        int result = solution.maxProduct(nums);
        System.out.println("Maximum product of a subarray: " + result);
    }
}