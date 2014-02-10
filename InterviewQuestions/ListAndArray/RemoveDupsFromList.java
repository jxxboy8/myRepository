package ListAndArray;

public class RemoveDupsFromList {
	public ListNode deleteDuplicates(ListNode head) {
        // Start typing your Java solution below
        // DO NOT write main() function
        if(head == null)
        	return null;
        if(head.next == null)
        	return head;
		ListNode newhead = null;
        ListNode curr = head;
        ListNode pre = head;
        ListNode last = null;
        while(curr != null){
        	if(pre.val != curr.val){
        		if(pre.next == curr){
        			if(newhead == null)
        				newhead = pre;
        			if(last != null)
        				last.next = pre;
        			last = pre;
        			last.next = null;
        		}
        		pre = curr;
        	}
        	curr = curr.next;
        }
        if(pre.next == null){
        	if(last == null){
        		last = pre;
        		newhead = pre;
        	}
        	else
        		last.next = pre;
        }
        return newhead;
    }

}
