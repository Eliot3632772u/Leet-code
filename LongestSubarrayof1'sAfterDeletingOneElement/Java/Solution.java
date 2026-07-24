class Solution {
    public int longestSubarray(int[] nums) {
        int k = 1;
        int l = 0, r = 0;
        int max = 0;
        while (r < nums.length) {
            if (nums[r] == 1 || k > 0) {
                if (nums[r] == 0) k--;
                max = Math.max(max, r - l);
                r++;
            } else {
                if (nums[l] == 0) k++;
                l++;
            }
        }

        return max;
    }
}