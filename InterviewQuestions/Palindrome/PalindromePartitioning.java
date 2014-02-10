package Palindrome;

public class PalindromePartitioning {
	public int minCut(String s){
		int length = s.length();
		boolean[][] isPal = new boolean[length][length];
		int[] dp = new int[length];
		for(int i=0; i<length; i++){
			dp[i] = i;
		}
		
		for(int i=0; i<length; i++){
			for(int j=0; j<=i; j++){
				if((s.charAt(i) == s.charAt(j)) && ((j+2>i) || isPal[j+1][i-1])){
					isPal[j][i] = true;
					if(j>0){
					    dp[i] = Math.min(dp[i], 1+dp[j-1]);
					}
					else{
					    dp[i] = 0;
					}
				}
			}
		}
		return dp[length-1];
	}
}
