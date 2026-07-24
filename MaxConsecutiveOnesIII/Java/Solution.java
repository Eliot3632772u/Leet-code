class Solution {
    public static int longestOnes(int[] nums, int k) {
        int l = 0, r = 0;
        int max = 0;
    
        while (r < nums.length) {
            if (nums[r] == 1 || (nums[r] == 0 && k > 0)) {
                if (nums[r] == 0) k--;
                max = Math.max(max, r - l + 1);
                r++;
            } else {
                if (nums[l] == 0) k++;
                l++;
            }
        }
    
        return max;
    }
}    
