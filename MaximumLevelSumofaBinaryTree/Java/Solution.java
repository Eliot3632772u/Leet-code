import java.util.*;

class Solution {
    public int maxLevelSum(TreeNode root) {
        int res = 0;
        int level = 1;
        int maxSum = Integer.MIN_VALUE;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            int sum = 0;
            for(int i = 0; i < size; i++) {
                TreeNode currNode = queue.pop();
                if (currNode.left != null) queue.add(currNode.left);
                if (currNode.right != null) queue.add(currNode.right);
                sum += currNode.val;
            }
            if (sum > maxSum) {
                res = level;
                maxSum = sum;
            }
            level++;
        }
        return res;
    }
}