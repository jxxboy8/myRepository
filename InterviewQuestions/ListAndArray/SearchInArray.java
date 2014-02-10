package ListAndArray;

public class SearchInArray {
	
	public boolean search(int[] A, int target) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        return sub(A, 0, A.length-1, target);
    }
	
	public boolean sub(int A[], int start, int end, int target){
		if(start > end)
			return false;
		int mid = (start + end) / 2;
		if(A[mid] == target)
			return true;
		else if(A[mid] > target){
			if(A[mid] > A[end]){
				if(target > A[end])
					return sub(A, start, mid-1, target);
				else if(target == A[end])
					return true;
				else
					return sub(A, mid+1, end, target);
			}
			else if(A[mid] == A[end])
				return sub(A, start, mid-1, target) || sub(A, mid+1, end, target);
			else
				return sub(A, start, mid-1, target);
		}
		else{
			if(A[mid] > A[start])
				return sub(A, mid+1, end, target);
			else if(A[mid] == A[start])
				return sub(A, start, mid-1, target) || sub(A, mid+1, end, target);
			else{
				if(target > A[start])
					return sub(A, start, mid-1, target);
				else if(target == A[start])
					return true;
				else
					return sub(A, mid+1, end, target);
			}
		}
	}

}
