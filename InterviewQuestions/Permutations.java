import java.util.ArrayList;

public class Permutations {
	public ArrayList<ArrayList<Integer>> permute(int[] num) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		sub(result, num, 0, num.length-1);
		return result;
    }
	
	public boolean swap(int[] num, int start, int end){
		for(int i=start; i < end; i++){
			if(num[i] == num[end])
				return false;
		}
		return true;
	}
	
	public void sub(ArrayList<ArrayList<Integer>> result, int[] num, int start, int 
			end){
		if(start == end){
			ArrayList<Integer> permutation = new ArrayList<Integer>();
			for(int i = 0; i < num.length; i++){
				permutation.add(num[i]);
			}
			result.add(permutation);
		}
		else{
			for(int i=start; i <= end; i++){
				int temp = num[start];
				num[start] = num[i];
				num[i] = temp;
				sub(result, num, start+1, end);
				int temp1 = num[start];
				num[start] = num[i];
				num[i] = temp1;
			}
		}
	}

}
