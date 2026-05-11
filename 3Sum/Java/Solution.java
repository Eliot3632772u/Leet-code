import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        
        HashSet<List<Integer>> set = new HashSet();
        Arrays.sort(nums);

        int l;
        int r;
        for(int i = 0; i < nums.length - 2; i++) {
            l = i + 1;
            r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];

                if (sum == 0) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[l]);
                    list.add(nums[r]);
                    list.sort(null);
                    set.add(list);
                }
                if (sum < 0) l++;
                else r--;
            }
        }

        
        return new ArrayList<List<Integer>>(set);
    }
}