package Subsets;

import java.util.ArrayList;
import java.util.Arrays;

public class Subsets {
	
    public ArrayList<ArrayList<Integer>> subsetsWithDup(int[] num) {
    	ArrayList<ArrayList<Integer>> subsets = new
    			ArrayList<ArrayList<Integer>>();
    	ArrayList<Integer> first = new ArrayList<Integer>();
    	subsets.add(first);
    	Arrays.sort(num);
    	if(num.length == 0)
    		return subsets;
    	int start = 0;
    	for(int i=0; i<num.length; i++){
    		if((i > 0) && num[i] != num[i-1])
    			start = 0;
    		int size = subsets.size();
    		for(int j=start; j<size; j++){
    			ArrayList<Integer> newSubset = new ArrayList<Integer>();
    			for(int k=0; k<subsets.get(j).size(); k++)
    				newSubset.add(subsets.get(j).get(k));
    			newSubset.add(num[i]);
    			subsets.add(newSubset);
    		}
    		start = size;
    	}
    	return subsets;
    }

}
