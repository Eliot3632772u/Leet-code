public class Main {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);          //       {1}
                                              //      /   \
        root.right = new TreeNode(3);         //    {2}   {3}
                                              //    / \ 
        root.left.left = new TreeNode(4);     //  {4} {5}
        root.left.right = new TreeNode(5);

        Solution solution = new Solution();
        int diameter = solution.diameterOfBinaryTree(root);
        System.out.println("Diameter of the binary tree: " + diameter);
    }
}