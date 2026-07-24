import java.util.HashMap;

class Solution {
    public int maxOperations(int[] nums, int k) {

        int ops = 0;
        HashMap<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++) {

            Integer need = map.get(k - nums[i]);
            if (need != null && need > 0) {
                map.put(k - nums[i], need - 1);
                ops++;
            } else 
                map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }

        return ops;
    }
}