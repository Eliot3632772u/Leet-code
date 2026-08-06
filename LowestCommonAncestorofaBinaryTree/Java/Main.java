public class Main {
    public static void main(String[] args) {
            
            Solution solution = new Solution();
            TreeNode root = new TreeNode(3);
            root.left = new TreeNode(5);
            root.right = new TreeNode(1);
            root.left.left = new TreeNode(6);
            root.left.right = new TreeNode(2);
            root.right.left = new TreeNode(0);
            root.right.right = new TreeNode(8);
            root.left.right.left = new TreeNode(7);
            root.left.right.right = new TreeNode(4);
    
            TreeNode p = root.left; // Node with value 5
            TreeNode q = root.left.right.right; // Node with value 4
    
            TreeNode lca = solution.lowestCommonAncestor(root, p, q);
            System.out.println("Lowest Common Ancestor of " + p.val + " and " + q.val + ": " + lca.val);
    }    
}
