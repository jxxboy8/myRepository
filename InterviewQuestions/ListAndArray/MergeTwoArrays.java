package ListAndArray;

public class MergeTwoArrays {

	public void merge(int A[], int m, int B[], int n) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		int index = m+n-1;
		while((m>0) && (n>0)){
			if(A[m-1] > B[n-1]){
				A[index] = A[m-1];
				m--;
			}
			else{
				A[index] = B[n-1];
				n--;
			}
			index--;
		}
		if(n > 0){
			for(int i=0; i<n; i++){
				A[i] = B[i];
			}
		}
    }
}
