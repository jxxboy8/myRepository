package copyRandomList;

import java.util.Hashtable;
import java.util.LinkedList;

public class RandomListNode {
	
	int label;
	RandomListNode next, random;
	RandomListNode(int x) { this.label = x; }
	
	public RandomListNode copyRandomList1(RandomListNode head) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        RandomListNode newHead = new RandomListNode(head.label);
        Hashtable<RandomListNode, RandomListNode> table = 
        		new Hashtable<RandomListNode, RandomListNode>();
        LinkedList<RandomListNode> queue = new LinkedList<RandomListNode>();
        queue.add(head);
        table.put(head, newHead);
        while(!queue.isEmpty()){
        	RandomListNode curr = queue.removeFirst();
        	if(table.containsKey(curr.next))
        		table.get(curr).next = table.get(curr.next);
        	else{
        		RandomListNode newNext = new RandomListNode(curr.next.label);
        		queue.add(curr.next);
        		table.put(curr.next, newNext);
        		table.get(curr).next = newNext;
        	}
        	if(table.containsKey(curr.random))
        		table.get(curr).random = table.get(curr.random);
        	else{
        		RandomListNode newRandom = new RandomListNode(curr.random.label);
        		queue.add(curr.random);
        		table.put(curr.random, newRandom);
        		table.get(curr).random = newRandom;
        	}
        }
        return newHead;
    }
	
	public RandomListNode copyRandomList(RandomListNode head) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		RandomListNode curr = head;
		while(curr!=null){
			RandomListNode newNode = new RandomListNode(curr.label);
			newNode.next = curr.next;
			curr.next = newNode;
			curr = newNode.next;
		}
		curr=head;
		while(curr!=null){
			curr.next.random = curr.random.next;
			curr = curr.next.next;
		}
		RandomListNode copy = head.next;
		RandomListNode temp = head;
		curr = head;
		while(curr!=null){
			temp = curr.next;
			curr.next = curr.next.next;
			curr = curr.next;
			if(curr==null)
				temp.next = null;
			else
				temp.next = curr.next;
		}
		return copy;
    }

}
