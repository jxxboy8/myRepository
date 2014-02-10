package Triangle;

import java.util.ArrayList;

public class Triangle {
	public int minimumTotal(ArrayList<ArrayList<Integer>> triangle) {
        // Start typing your Java solution below
        // DO NOT write main() function
		int length = triangle.size();
        int[] A = new int[length];
        for(int i=length-1; i>=0; i--){
        	for(int j=0; j<=i; j++){
        		if(i == (length-1))
        			A[j] = triangle.get(length-1).get(j);
        		else{
        			A[j] = ((A[j] > A[j+1]) ? A[j+1]+triangle.get(i).get(j) :
        				A[j]+triangle.get(i).get(j));
        		} 
        	}
        }
        return A[0];
    }
	
	public ArrayList<Integer> getRow(int rowIndex) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0; i<=rowIndex; i++){
			if(i==0)
				result.add(1);
			int tmp = 0;
			int last = 0;
			for(int j=1; j<=i; j++){
				if(j==i)
					result.add(1);
				else{
					tmp = result.get(j);
					if(j == 1)
						last = result.get(0);
					result.set(j, result.get(j) + last);
					last = tmp;
				}
			}
		}
		return result;
    }

}
