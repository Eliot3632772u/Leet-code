class Solution {
    int max = 0;
    public int longestZigZag(TreeNode root) {
        dfs(root, true, 0);
        return max;
    }

    public void dfs(TreeNode root, boolean left, int accumul) {
        if (root == null) {
            max = Math.max(accumul - 1, max);
            return;
        }

        if (left == false) dfs(root.right, true, accumul + 1);
        else dfs(root.left, false, accumul + 1);
        if (left == false) dfs(root.left, false, 1);
        else dfs(root.right, true, 1);

        return;
    }
}