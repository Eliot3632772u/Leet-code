class Solution {
    public int[] searchRange(int[] nums, int target) {
        
        int l = 0;
        int r = nums.length - 1;
        int m;
        
        while (l <= r) {
            m = (r + l) / 2;

            if (nums[m] == target) {
                int i = m - 1;
                int j = m + 1;
                while (i >= 0 && nums[i] == target) i--;
                while (j <= nums.length - 1 && nums[j] == target) j++;
                return new int[]{i + 1, j - 1};
            }

            if (l == r) break;

            if (nums[m] > target) r = m;
            else l = m + 1;
        }

        return new int[]{-1,-1};
    }
}