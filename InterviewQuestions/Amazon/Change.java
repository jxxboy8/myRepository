package Amazon;

public class Change {
	/*
    public static int ways = 0;
	
	public static void makeChange(int total, int denom){
		int nextDenom = 0;
		if(denom == 25)
			nextDenom = 10;
		if(denom == 10)
			nextDenom = 5;
		if(denom == 5)
			nextDenom = 1;
		if(denom == 1){
			ways++;
			return;
		}
		
		if(total < 0)
			return;
		else if(total == 0){
			ways++;
			return;
		}
		makeChange(total-denom, denom);
        makeChange(total, nextDenom);	
	}
	*/
	public static int makeChangedyn(int n) {

	      // Take care of simple cases.
	      if (n < 0)
	          return 0;
	      else if ((n>=0) && (n < 5))
	          return 1;
	    
	       // Build table here.
	       else {

	            int[] denominations = {1, 5, 10, 25};
	            int[][] table = new int[4][n+1];
	    
	            // Initialize table
	            for (int i=0; i<n+1;i++)
	                table[0][i] = 1;
	            for (int i=0; i<5; i++) {
	                table[1][i] = 1;
	                table[2][i] = 1;
	                table[3][i] = 1;
	            }
	            for (int i=5;i<n+1;i++) {
	                table[1][i] = 0;
	                table[2][i] = 0;
	                table[3][i] = 0;
	            }

	           // Fill in table, row by row. 
	           for (int i=1; i<4; i++) {
	                for (int j=5; j<n+1; j++) {
	                    for (int k=0; k<=i; k++) {
	                        if ( j >= denominations[k])
	                             table[i][j] += table[k][j - denominations[k]];
	                    } 
	                }
	            }        
	            return table[3][n]; 
	       }
	  }
	
	public static int makeChange(int n){
		int[] denom = {1, 5, 10, 25};
		int[] table = new int[n+1];
		for(int i=0; i<4; i++){
			for(int j=0; j<(n+1); j++){
				if((i==0) || (j==0))
					table[j] = 1;
				else{
					if(j >= denom[i])
						table[j] = table[j] + table[j-denom[i]];
				}
			}
		}
		return table[n];
	}
	
	public static void main(String[] args){
		//makeChange(100, 25);
		System.out.println(makeChange(100));
	}
	
	
	/*
	public static int makeChange2(int n, int[] denoms, int k) {
        if (k + 1 >= denoms.length) return 1;
        int ways = 0;
        for (int i = 0; i * denoms[k] <= n; i++) {
                ways += makeChange2(n - i * denoms[k], denoms, k + 1);
        }
        return ways;
}        

public static int makeChange(int n, int denom) {
        int next_denom = 0;
        switch (denom) {
        case 25:
                next_denom = 10;
                break;
        case 10:
                next_denom = 5;
                break;
        case 5:
                next_denom = 1;
                break;
        case 1:
                return 1;
        }
        int ways = 0;
        for (int i = 0; i * denom <= n; i++) {
                ways += makeChange(n - i * denom, next_denom);
        }
        return ways;
}

        public static int makeChange(int n) {
        int[] denoms = {25, 10, 5, 1};
        int x = makeChange2(n, denoms, 0);
        int y = makeChange(n, 25);
        if (x != y) {
                System.out.println("Error: " + x + " " + y);
        }
        return x;
}

        public static void main(String[] args) {
        for (int i = 100; i <= 100; i++) {
                System.out.println("makeChange(" + i + ") = " + makeChange(i));
        }
}
*/

}
