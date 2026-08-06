public class Main {
    public static void main(String args[]) {
            
            Solution solution = new Solution();
            TreeNode root = new TreeNode(10);
            root.left = new TreeNode(5);
            root.right = new TreeNode(-3);
            root.left.left = new TreeNode(3);
            root.left.right = new TreeNode(2);
            root.right.right = new TreeNode(11);
            root.left.left.left = new TreeNode(3);
            root.left.left.right = new TreeNode(-2);
            root.left.right.right = new TreeNode(1);
    
            int targetSum = 8;
            int result = solution.pathSum(root, targetSum);
            System.out.println("Number of paths that sum to " + targetSum + ": " + result);
    }
}