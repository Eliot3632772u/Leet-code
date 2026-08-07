import java.util.*;

class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> lst = new ArrayList<>();
        LinkedList<TreeNode> queue = new LinkedList<>();
        
        if (root == null) return lst;

        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            TreeNode rightView = null;
            for(int i = 0; i < size; i++) {
                rightView = queue.pop();
                if (rightView.left != null) queue.add(rightView.left);
                if (rightView.right != null) queue.add(rightView.right);
            }
            lst.add(rightView.val);
        }
        return lst;
    }
}