package ListAndArray;

public class ReverseLinkedList {
	public static ListNode reverseBetween(ListNode head, int m, int n) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(head == null)
        	return null;
		int count = 1;
        ListNode current = head;
        ListNode newhead = head;
        ListNode beforeStart = null;
        ListNode start = null;
        ListNode last = null;
		while(current != null){
			if(count == (m-1))
				beforeStart = current;
			ListNode tmp = current.next;
			if((count >= m) && (count <= n)){
				current.next = last;
				last = current;
				if(count == m){
					start = current;
					start.next = null;
				}
				if(count == n){
					if(beforeStart != null)
						beforeStart.next = current;
					else
						newhead = current;
				}
			}
			if(count == (n+1))
				start.next = current;
			current = tmp;
			count++;
		}
		return newhead;
    }
	
	public static void main(String[] args){
		ListNode one = new ListNode(1);
		ListNode two = new ListNode(2);
		ListNode three = new ListNode(3);
		ListNode four = new ListNode(4);
		ListNode five = new ListNode(5);
		one.next = two;
		two.next = three;
		three.next = four;
		four.next = five;
		ListNode tmp = reverseBetween(one, 4, 5);
		while(tmp != null){
			System.out.println(tmp.val);
			tmp = tmp.next;
		}
	}

}
