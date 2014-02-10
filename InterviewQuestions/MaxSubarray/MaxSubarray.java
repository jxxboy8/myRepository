package MaxSubarray;

public class MaxSubarray {
	public static void maxSubarray(int[] a){
		if(a.length == 0)
			return;
		int sum = a[0], max = a[0];
		int start = 0;
		int max_start = 0, max_end = 0;
		for(int i=1; i<a.length; i++){
			sum = sum + a[i];
			if(sum < a[i]){
				sum = a[i];
				start = i;
			}
			if(sum > max){
				max = sum;
				max_start = start;
				max_end = i;
			}
		}
		System.out.println(max + ": ");
		for(int i=max_start; i<=max_end; i++){
			System.out.print(a[i] + " ");
		}
	}
	
	public static void main(String[] args){
		int[] a = {-2, -3, -4, 1, -2, -1, -5, -3};
		maxSubarray(a);
	}
	
	public int maxSubArray(int[] A) {
        // IMPORTANT: Please reset any member data you declared, as
        // the same Solution instance will be reused for each test case.
        if(A.length == 0)
        	return 0;
        int sum = A[0], max = A[0];
        for(int i=1; i<A.length; i++){
        	sum = sum + A[i];
        	if(sum < A[i])
        		sum = A[i];
        	if(sum > max)
        		max = sum;
        }
        return max;
    }

}
