import java.util.*;

class Solution {
    public int[] twoSum(int[] nums, int t) {
        
        HashMap<Integer, Integer> map = new HashMap<>();

        int i = 0;
        int j = nums.length - 1;

        while (i <= j)
        {
            Integer c1 = map.get(t - nums[i]);
            if (c1 != null) return new int[]{c1, i};
            else map.put(nums[i], i);


            Integer c2 = map.get(t - nums[j]);
            if (c2 != null) return new int[]{c2, j};
            else map.put(nums[j], j);

            i++;
            j--;
        }

        return nums;
    }
}