package UniquePath;

public class UniquePath {
	
	public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        int[] dp = new int[obstacleGrid[0].length];
        for(int i=0; i<obstacleGrid.length; i++){
        	for(int j=0; j<obstacleGrid[0].length; j++){
        		if(obstacleGrid[i][j] == 1){
        			dp[j] = 0;
        			continue;
        		}
        		if(j==0){
        			if(i==0)
        				dp[j] = 1;
        		}
        		else{
        			if(i == 0)
        				dp[j] = dp[j-1];
        			else
        				dp[j] = dp[j] + dp[j-1];
        		}
        	}
        }
        return dp[dp.length-1];
    }

}
