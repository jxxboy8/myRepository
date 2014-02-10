package ListAndArray;

public class MergeSortList {
	
	public ListNode sortList(ListNode head) {
        // IMPORTANT: Please reset any member data you declared, as
        // the same Solution instance will be reused for each test case.
        if(head == null)
        	return null;
        if(head.next == null)
        	return head;
        ListNode left = head;
        int count = 0;
        ListNode pointer = head;
        while(pointer != null){
        	count++;
        	pointer = pointer.next;
        }
        pointer = head;
        int mid = count / 2;
        count = 0;
        ListNode last = null;
        while(count < mid){
        	count++;
        	last = pointer;
        	pointer = pointer.next;
        }
        last.next = null;
        ListNode right = pointer;
        
        ListNode leftList = sortList(left);
        ListNode rightList = sortList(right);
        
        ListNode newList = merge(leftList, rightList);
        
        return newList;
    }
	
	public ListNode merge(ListNode left, ListNode right){
		ListNode newhead = null;
		ListNode pre = null;
		while((left != null) && (right != null)){
			if(left.val <= right.val){
				if(newhead == null)
					newhead = left;
				if(pre != null)
					pre.next = left;
				pre = left;
				left = left.next;
			}
			else{
				if(newhead == null)
					newhead = right;
				if(pre != null)
					pre.next = right;
				pre = right;
				right = right.next;
			}
		}
		if(left == null)
			pre.next = right;
		else
			pre.next = left;
		
		return newhead;
	}

}
