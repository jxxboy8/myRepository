package Amazon;
import java.util.Arrays;
import java.util.ArrayList;

public class ThreeSum {
    public ArrayList<ArrayList<Integer>> threeSum(int[] num) {
    	ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    	if(num.length < 3)
    		return result;
    	Arrays.sort(num);
    	for(int i=0; i<num.length; i++){
    		if((i > 0) && (num[i] == num[i-1]))
    			continue;
    		int start = i+1;
    		int end = num.length-1;
    		while(start < end){
    			int sum = num[i] + num[start] + num[end];
    			if(sum > 0)
    				end--;
    			else if(sum < 0)
    				start++;
    			else{
    				ArrayList<Integer> temp = new ArrayList<Integer>();
    				temp.add(num[i]);
    				temp.add(num[start]);
    				temp.add(num[end]);
    				result.add(temp);
    				int index = start;
    				while((start < end) && (num[index] == num[start]))
    					start++;
    				index = end;
    				while((start < end) && (num[index] == num[end]))
    					end--;
    			}
    		}
    	}
    	return result;
    }

}
