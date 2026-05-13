class Solution {
    public static int trap(int[] height) {
        int[] hlm = new int[height.length];
        int[] hrm = new int[height.length];

        hlm[0] = 0;
        hrm[hrm.length - 1] = 0;
        int maxl = 0;
        int maxr = 0;

        for(int i = 0; i < height.length; i++) {
            int curr = height[i];
            hlm[i] = maxl;
            maxl = Math.max(maxl, curr);
        }

        for(int j = height.length - 1; j >= 0; j--) {
            int curr = height[j];
            hrm[j] = maxr;
            maxr = Math.max(maxr, curr);
        }
        
        int sum = 0;
        
        for(int i = 0; i < height.length; i++) {
          int currSum = Math.min(hlm[i], hrm[i]) - height[i];
          sum += currSum > 0 ? currSum : 0;
        }
        return sum;
    }
}