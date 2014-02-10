package LongestValidParenthese;

import java.util.Stack;

public class LongestValidParenthese {
	
    public int longestValidParentheses(String s) {
    	Stack<Integer> lefts = new Stack<Integer>();
    	int last = -1;
    	int max = 0;
    	for(int j=0; j<s.length(); j++){
    		if(s.charAt(j) == '(')
    			lefts.push(j);
    		else{
    			if(lefts.isEmpty())
    				last = j;
    			else{
    				lefts.pop();
    				if(lefts.isEmpty())
    					max = Math.max(max, j-last);
    				else
    					max = Math.max(max, j-lefts.peek());
    			}
    		}
    	}
    	return max;
    }

}
