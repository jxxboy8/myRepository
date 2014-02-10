package BinaryTree;

public class ListToBST {
	ListNode current;
    public TreeNode sortedListToBST(ListNode head) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(head == null)
        	return null;
    	current = head;
        int last = -1;
        while(current != null){
        	last++;
        	current = current.next;
        }
        current = head;
        return buildBST(0, last);
    }
    
    public TreeNode buildBST(int start, int end){
    	if(start > end)
    		return null;
    	int mid = (start + end) / 2;
    	TreeNode left = buildBST(start, mid-1);
    	TreeNode root = new TreeNode(current.val);
    	root.left = left;
    	current = current.next;
    	root.right = buildBST(mid+1, end);
    	return root;
    }

}
