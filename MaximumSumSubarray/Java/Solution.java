public class Solution {
    public static int maxSubArray(int[] nums) {
        
        int maxSum = nums[0];
        int currMaxSum = nums[0];

        for(int i = 1; i < nums.length; i++) {
            currMaxSum = Math.max(nums[i], nums[i] + currMaxSum);
            maxSum = Math.max(maxSum, currMaxSum);
        }

        return maxSum;
    }
}