package BinaryTree;

public class MinDepth {
	
	public int minDepth(TreeNode root) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(root == null)
        	return 0;
        int left = minDepth(root.left);
        int right = minDepth(root.right);
        if((left == 0) || (right == 0))
        	return Math.max(left, right) + 1;
        return Math.min(left, right) + 1;
    }

}
