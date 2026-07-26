import java.util.*;

class Solution {
    public int equalPairs(int[][] grid) {
        
        HashMap<List<Integer>, Integer> map = new HashMap<>();

        for(int i = 0; i < grid.length; i++) {
            List<Integer> lst = new ArrayList<>();
            for(int j = 0; j < grid.length; j++) {
                lst.add(grid[i][j]);
            }
            map.put(lst, map.getOrDefault(lst, 0) + 1);
        }

        int count = 0;
        for(int i = 0; i < grid.length; i++) {
            List<Integer> lst = new ArrayList<>();
            for(int j = 0; j < grid.length; j++) {
                lst.add(grid[j][i]);
            }
            count += map.getOrDefault(lst, 0);
        }

        return count;
    }
}