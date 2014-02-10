package ListAndArray;

public class InsertionSort {
	
	public ListNode insertionSortList(ListNode head) {
        // IMPORTANT: Please reset any member data you declared, as
        // the same Solution instance will be reused for each test case.
        if(head == null)
        	return null;
        if(head.next == null)
        	return head;
		ListNode pointer = head.next;
		ListNode newhead = head;
		head.next = null;
		while(pointer != null){
			ListNode next = pointer.next;
			ListNode innerpointer = newhead;
			ListNode pre = null;
			while(innerpointer != null){
				if(pointer.val <= innerpointer.val){
					pointer.next = innerpointer;
					if(pre != null)
						pre.next = pointer;
					else
						newhead = pointer;
					break;
				}
				else{
					pre = innerpointer;
				    innerpointer = innerpointer.next;
				}
			}
			if(innerpointer == null){
				if(pre != null)
					pre.next = pointer;
				pointer.next = null;
			}
			pointer = next;
		}
		return newhead;
    }

}
