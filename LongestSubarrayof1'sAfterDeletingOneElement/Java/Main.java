public class Main {
    
    public static void main(String[] args) {
        Solution s = new Solution();
        int[] nums = {1, 1, 0, 1};
        int result = s.longestSubarray(nums);
        System.out.println("Longest subarray of 1's after deleting one element: " + result);
    }
}
