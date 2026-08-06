public class Main {
    public static void main(String args[]) {
            
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(1);
        root.right.left = new TreeNode(1);
        root.right.right = new TreeNode(1);
        root.right.left.left = new TreeNode(1);
        root.right.left.right = new TreeNode(1);
    
        int result = solution.longestZigZag(root);
        System.out.println("Longest ZigZag Path Length: " + result);
    }
}
