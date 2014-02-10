package BinaryTree;

public class RecoverBST {
	
	TreeNode first;
    TreeNode second;
    TreeNode previous;
    
    public void recoverTree(TreeNode root) {
        // Start typing your Java solution below
        // DO NOT write main() function
        previous=null;
        first = null;
        second = null;
        inorder(root);
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
    }
    
    public void inorder(TreeNode root) {
        if(root==null) return;
        inorder(root.left);
        if(previous!=null&&previous.val>root.val) {
            if(first == null){
                first = previous;
            }
            second = root;
        }
        previous = root;
        inorder(root.right);
    }

}
