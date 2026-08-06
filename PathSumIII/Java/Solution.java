import java.util.*;

class Solution {
    HashMap<Long, Integer> map = new HashMap<>();
    int count = 0;
    public int pathSum(TreeNode root, int t) {
        map.put(0l, 1);
        dfs(root, t, 0l);
        return count;
    }

    public void dfs(TreeNode root, int t, Long curr) {
        if (root == null) return;
        curr += root.val;
        Integer freq = map.getOrDefault(curr - t, 0);
        count += freq;
        map.put(curr, map.getOrDefault(curr, 0) + 1);
        dfs(root.left, t, curr);
        dfs(root.right, t, curr);
        map.put(curr, map.get(curr) - 1);
        return;
    }
}