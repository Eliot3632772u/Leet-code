public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
        int k = 2;
        int result = Solution.longestOnes(nums, k);
        System.out.println("Max consecutive ones after flipping at most " + k + " zeros: " + result);
    }
}
