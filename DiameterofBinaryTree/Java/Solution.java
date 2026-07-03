public class Solution {

    int res;
    
    public int diameterOfBinaryTree(TreeNode root) {
        
        height(root);
        return res;
    }

    int height(TreeNode root) {
        if (root == null) return 0;

        int l = height(root.left); 
        int r = height(root.right);

        res = Math.max(res, l + r);

        return Math.max(l + 1, r + 1);
    }
}