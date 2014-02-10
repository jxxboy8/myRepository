package SingleNumber;

public class SingleNumber {
	
	public int singleNumber2(int[] A){
		int result = 0;
		for(int i=0; i<32; i++){
			int count = 0;
			for(int j=0; j<A.length; j++){
				if(((1 << i) & A[j]) != 0)
					count++;
			}
			if((count%3) != 0)
				result = result | (1 << i);
			count = 0;
		}
		return result;
	}
	
	public int singleNumber1(int[] A) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(A == null || A.length == 0){
             return 0;
         }
       int result = A[0];
       
       for(int i = 1; i < A.length; i++){
            result = result ^ A[i];
        }
         return result;
    }

}
