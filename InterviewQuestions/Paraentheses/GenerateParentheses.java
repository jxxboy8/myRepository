package Paraentheses;

import java.util.ArrayList;

public class GenerateParentheses {
    public ArrayList<String> generateParenthesis(int n) {
        ArrayList<String> result = new ArrayList<String>();
        char[] charList = new char[2 * n];
        sub(result, charList, 0, 0, 0, n);
        return result;
    }

    public void sub(ArrayList<String> result, char[] charList, int index, 
    		int left, int right, int total){
    	if((left == total) && (right == total)){
    		String item = new String(charList);
    		result.add(item);
    	}
    	else{
    		if(left < total){
    			charList[index] = '(';
    			sub(result, charList, index+1, left+1, right, total);
    		}
    		
    		if(left > right){
    			charList[index] = ')';
    			sub(result, charList, index+1, left, right+1, total);
    		}
    	}
    }
}
