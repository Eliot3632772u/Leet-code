class Solution {
    public static double findMaxAverage(int[] nums, int k) {
        double max = -Double.MAX_VALUE;
        double sum = 0;
        int l = 0, r = 0;
        while (r < nums.length) {
            while (r - l < k)
                sum += nums[r++];
            max = Math.max(max, sum / k);
            sum -= nums[l];
            l++;
        }

        return max;
    }
}