import java.util.*;

public class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++) {

            Integer oldIndex = map.get(nums[i]);
            if (oldIndex != null) {
                if (i - oldIndex <= k) return true;
                map.replace(nums[i], i);
            }
            else map.put(nums[i], i);
        }

        return false;
    }
}