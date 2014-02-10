package BinaryTree;

public class ValidateBST {
	
	public boolean isValidBST(TreeNode root) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        return BST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

	public boolean BST(TreeNode root, int min, int max){
		if(root == null)
			return true;
		if((root.val < min) || (root.val > max))
			return false;
		return BST(root.left, min, root.val) && BST(root.right, root.val, max);
	}
}
