package ListAndArray;

public class ReverseList {
    
	public static ListNode reverse(ListNode head){
		if(head == null)
			return null;
		ListNode tmp = head.next;
		head.next = null;
		ListNode newhead = head;
		ListNode next = null;
		while(tmp != null){
			next = tmp.next;
			tmp.next = newhead;
			newhead = tmp;
			tmp = next;
		}
		return newhead;
	}
	
	public static ListNode reverse1(ListNode head){
		if(head == null)
			return null;
		ListNode next = head.next;
		head.next = null;
		ListNode newhead = reverse1(next);
		if(newhead != null)
			next.next = head;
		else
			newhead = head;
		
		return newhead;
	}
	
	/*
	public static void main(String[] args){
    	ListNode head = new ListNode(1);
    	ListNode two = new ListNode(2);
    	ListNode three = new ListNode(3);
    	ListNode four = new ListNode(4);
    	head.next = two;
    	two.next = three;
    	three.next = four;
    	ListNode tmp = head;
    	while(tmp != null){
    		System.out.print(tmp.val + " ");
    		tmp = tmp.next;
    	}
    	System.out.println("");
    	ListNode newhead = reverse(head);
    	tmp = newhead;
    	while(tmp != null){
    		System.out.print(tmp.val + " ");
    		tmp = tmp.next;
    	}
    	System.out.println("");
    	tmp = reverse1(newhead);
    	while(tmp != null){
    		System.out.print(tmp.val + " ");
    		tmp = tmp.next;
    	}
    }
    */
	
	public static ListNode reverseMod(ListNode head, int n){
		if(head == null)
			return null;
		ListNode newhead = null;
		int count = 0;
		ListNode tmp = head;
		ListNode pre = null;
		ListNode tail = null;
		ListNode pretail = null;
		while(tmp != null){
			count++;
			if(count <= n)
				newhead = tmp;
			ListNode next = tmp.next;
			if((count % n) == 1){ 
				pretail = tail;
				tail = tmp;
				pre = null;
			}
			if(((count % n) == 0) && (pretail != null))
				pretail.next = tmp;
			tmp.next = pre;
			pre = tmp;
			tmp = next;
		}
		if(pretail != null)
			pretail.next = pre;
		
		return newhead;
	}
	
	public static ListNode reversePairs(ListNode head){
		if(head == null)
			return null;
		if(head.next == null)
			return head;
		ListNode newhead = null;
		ListNode pre = null;
		ListNode tmp = head;
		while(tmp != null){
			if(tmp.next != null){
				if(newhead == null)
					newhead = tmp.next;
				if(pre != null)
					pre.next = tmp.next;
				pre = tmp;
				ListNode next = tmp.next.next;
				tmp.next.next = tmp;
				tmp.next = null;
				tmp = next;
			}
			else{
				pre.next = tmp;
				tmp = null;
				if(newhead == null)
					newhead = tmp;
			}
		}
		return newhead;
	}
	
	public static void main(String[] args){
		
		ListNode head = new ListNode(0);
		ListNode pre = head;
		for(int i=1; i < 1; i++){
			ListNode newNode = new ListNode(i);
			pre.next = newNode;
			pre = newNode;
		}
		
		ListNode tmp = head;
    	while(tmp != null){
    		System.out.print(tmp.val + " ");
    		tmp = tmp.next;
    	}
    	System.out.println("");
    	
    	ListNode newhead = reversePairs(head);
    	tmp = newhead;
    	while(tmp != null){
    		System.out.print(tmp.val + " ");
    		tmp = tmp.next;
    	}
    	System.out.println("");
    	
	}
}
