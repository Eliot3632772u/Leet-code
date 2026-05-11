class Solution {
    public static int maxArea(int[] height) {
        
        int max = Integer.MIN_VALUE;
        int l = 0;
        int r = height.length - 1;

        while (l < r) {

            int container = Math.min(height[l], height[r]) * (r - l);
            if (max < container) max = container;

            if (height[l] < height[r]) l++;
            else r--;
        }

        return max;
    }
}