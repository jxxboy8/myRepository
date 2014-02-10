package DistinctSubsequence;

public class DistinctSubsequence {
	public int numDistinct(String S, String T) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        int[] dp = new int[T.length()];
		for(int j=0; j<S.length(); j++){
			int last = 0;
			for(int i=0; i<T.length(); i++){
				int tmp = dp[i];
				if(S.charAt(j) == T.charAt(i)){
					if(i == 0)
						dp[0] = dp[0] + 1;
					else{
						dp[i] = dp[i] + last;
					}
				}
				last = tmp;
			}
		}
		return dp[dp.length-1];
    }

}
