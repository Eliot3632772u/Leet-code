import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(4);

        List<Integer> result = solution.rightSideView(root);
        System.out.println(result); // Output: [1, 3, 4]
    }
}
