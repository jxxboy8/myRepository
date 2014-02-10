package BinaryTree;

public class UniqueBST {
	public int numTrees(int n) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(n == 0)
        	return 0;
		int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = 1;
        for(int i=2; i<n+1; i++){
        	for(int j=1; j<=i; j++){
        		dp[i] += dp[j-1] * dp[i-j];
        	}
        }
        return dp[n];
    }
	
}
