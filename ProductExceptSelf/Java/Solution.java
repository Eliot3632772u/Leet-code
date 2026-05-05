class Solution {

    public int[] productExceptSelf(int[] nums) {
        
        int s = nums.length;
        int[] l = new int[s];
        int[] r = new int[s];

        Arrays.fill(l, 1);
        Arrays.fill(r, 1);

        for(int i = 1, j = s - 2; i < s; i++, j--) {

            l[i] = nums[i - 1] * l[i - 1];
            r[j] = nums[j + 1] * r[j + 1];
        }

        for(int i = 0; i < s; i++) {
            nums[i] = r[i] * l[i];
        }

        return nums;
    }
}