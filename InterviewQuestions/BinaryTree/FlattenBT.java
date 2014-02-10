package BinaryTree;

public class FlattenBT {
	public void flatten(TreeNode root) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(root == null)
        	return;
        TreeNode left = root.left;
        TreeNode right = root.right;
        if(left != null){
        	root.right = root.left;
            root.left = null;
            TreeNode rightmost = left;
            while(rightmost.right != null){
            	rightmost = rightmost.right;
            }
            rightmost.right = right;
        }
        flatten(root.right);
    }

}
