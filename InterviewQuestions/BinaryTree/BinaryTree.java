package BinaryTree;

public class BinaryTree {
	public void connect(TreeLinkNode root) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        TreeLinkNode leftmost = root;
        if(leftmost == null)
        	return;
		while(leftmost.left != null){
			TreeLinkNode iterator = leftmost;
			leftmost = leftmost.left;
			while(iterator != null){
				iterator.left.next = iterator.right;
				if(iterator.next != null)
					iterator.right.next = iterator.next.left;
				iterator = iterator.next;
			}
		}
    }
	
	public void connect1(TreeLinkNode root) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        TreeLinkNode leftmost = root;
		while(leftmost != null){
			TreeLinkNode iterator = leftmost;
			leftmost = null;
			TreeLinkNode prev = null;
			while(iterator != null){
				if(iterator.left != null){
					if(prev != null)
						prev.next = iterator.left;
					prev = iterator.left;
					if(leftmost == null)
						leftmost = iterator.left;
				}
				if(iterator.right != null){
					if(prev != null)
						prev.next = iterator.right;
					prev = iterator.right;
					if(leftmost == null)
						leftmost = iterator.right;
				}
				iterator = iterator.next;
			}
		}
    }

}
