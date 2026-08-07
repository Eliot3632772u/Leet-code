class Solution {
    public TreeNode deleteNode(TreeNode root, int key) {
        TreeNode prev = null, head = root;
        while (root != null) {
            if (root.val == key) {
                
                if (prev == null) {
                    head = root.left;
                    TreeNode rightSub = root.right;
                    root = head;
                    if (root != null) insertRight(root, rightSub);
                    else head = rightSub;
                } else {
                    if (root.val > prev.val) { // update prev.right
                        prev.right = root.left;
                        TreeNode rightSub = root.right;
                        root = root.left;
                        if (root != null) insertRight(root, rightSub);
                        else prev.right = rightSub;
                    } else { // update prev.left
                        prev.left = root.left;
                        TreeNode rightSub = root.right;
                        root = root.left;
                        if (root != null) insertRight(root, rightSub);
                        else prev.left = rightSub;
                    }
                }
                break;
            }
            else if (root.val > key) {
                prev = root;
                root = root.left;
            } else {
                prev = root;
                root = root.right;
            }
        }
        return head;
    }

    public void insertRight(TreeNode root, TreeNode rightSub) {
        if (root == null) return;
        if (root.right == null) {
            root.right = rightSub;
            return;
        }
        insertRight(root.right, rightSub);
    }
}