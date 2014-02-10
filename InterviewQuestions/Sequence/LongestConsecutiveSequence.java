package Sequence;
import java.util.Hashtable;

public class LongestConsecutiveSequence {
	public int longestConsecutive(int[] num) {
        // Start typing your Java solution below
        // DO NOT write main() function
        int max = 1;
        Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>();
        for(int n : num){
        	if(table.containsKey(n))
        		continue;
        	table.put(n, 1);
        	if(table.containsKey(n-1))
        		max = Math.max(max, merge(table, n-1, n));
        	if(table.containsKey(n+1))
        		max = Math.max(max, merge(table, n, n+1));
        }
        return max;
    }
	
	public int merge(Hashtable<Integer, Integer> table, int left, int right){
		int leftmost = left-table.get(left)+1;
		int rightmost = right+table.get(right)-1;
		int length = table.get(left)+table.get(right);
		table.put(leftmost, length);
		table.put(rightmost, length);
		return length;
	}

}
