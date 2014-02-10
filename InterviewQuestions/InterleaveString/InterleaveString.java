package InterleaveString;

public class InterleaveString {
	public static boolean isInterleave(String s1, String s2, String s3) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        int len1 = s1.length();
        int len2 = s2.length();
        boolean[] dp = new boolean[len1+1];
        dp[0] =true;
        for(int i=0; i<len2+1; i++){
        	for(int j=0; j<len1+1; j++){
        		if((i>0) && (j>0)){
        			if(s2.charAt(i-1) == s3.charAt(i+j-1))
        				dp[j] = dp[j];
        			else
        				dp[j] = false;
        			boolean tmp = false;
        			if(s1.charAt(j-1) == s3.charAt(i+j-1))
        				tmp = dp[j-1];
        			dp[j] = (dp[j] || tmp);
        		}
        		if((j == 0) && (i > 0)){
        			if(s2.charAt(i-1) == s3.charAt(i+j-1))
        				dp[j] = dp[j];
        			else
        				dp[j] = false;
        		}
        		if((i == 0) && (j>0)){
        			if(s1.charAt(j-1) == s3.charAt(i+j-1))
        				dp[j] = dp[j-1];
        			else
        				dp[j] = false;
        		}
        	}
        }
        return dp[len1];
    }
	
	public static void main(String[] args){
		String s1 = "aabcc";
		String s2 = "dbbca";
		String s3 = "aadbbcbcac";
		String s4 = "aadbbbaccc";
		System.out.println(isInterleave(s1, s2, s3));
		System.out.println(isInterleave(s1, s2, s4));
	}

}
