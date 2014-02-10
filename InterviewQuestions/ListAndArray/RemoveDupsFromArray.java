package ListAndArray;

public class RemoveDupsFromArray {
	public int removeDuplicates(int[] A) {
        // Start typing your Java solution below
        // DO NOT write main() function
        if(A.length == 0)
        	return 0;
		int i = 0;
        int j = 0;
        for(j=1; j<A.length; j++){
        	if(A[j] != A[i]){
        		i++;
        		A[i] = A[j];
        	}
        	else{
        		if((i > 0) && (A[i] != A[i-1])){
        			i++;
        			A[i] = A[j];
        		}
        		if(i == 0){
        			i++;
        			A[i] = A[j];
        		}
        	}
        }
        return i+1;
    }

}
