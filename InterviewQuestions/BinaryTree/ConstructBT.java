package BinaryTree;

public class ConstructBT {
	public TreeNode buildTree(int[] inorder, int[] postorder) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(inorder.length == 0)
        	return null;
        return build(inorder, postorder, 0, inorder.length-1, 0, postorder.length-1);
    }
	
	public TreeNode build(int[] inorder, int[] postorder, int start1, int end1, int start2, int end2){
		if((start1 > end1) || (start2 > end2))
			return null;
		TreeNode root = new TreeNode(postorder[end2]);
		int i = start1;
		while(inorder[i] != root.val){
			i++;
		}
		root.left = build(inorder, postorder, start1, i-1, start2, start2+i-1-start1);
		root.right = build(inorder, postorder, i+1, end1, end2-1-(end1-(i+1)), end2-1);
		return root;
	}

}
