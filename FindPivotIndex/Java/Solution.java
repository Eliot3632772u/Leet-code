class Solution {
    public int pivotIndex(int[] nums) {
        int[] l = new int[nums.length];
        int[] r = new int[nums.length];
        l[0] = nums[0];
        r[nums.length - 1] = nums[nums.length - 1];
        for(int i = 1, j = nums.length - 2; i < nums.length  && j >= 0; i++, j--) {
          l[i] = l[i - 1] + nums[i];
          r[j] = r[j + 1] + nums[j];
        }

        for(int i = 0; i < nums.length; i++) {
            if (l[i] == r[i]) return i;
        }

        return -1;
    }
}