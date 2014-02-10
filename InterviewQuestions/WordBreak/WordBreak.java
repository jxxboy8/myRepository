package WordBreak;
import java.util.Set;

public class WordBreak {
	public boolean wordBreak(String s, Set<String> dict){
		boolean[] dp = new boolean[s.length()];
		for(int i=0; i<s.length(); i++){
			for(int j=0; j<=i; j++){
				if(dict.contains(s.substring(j, i+1))){
					if(j>0){
						if(dp[j-1]){
							dp[i] = true;
							break;
						}
					}
					else{
						dp[i]=true;
						break;
					}
				}
			}
		}
		return dp[dp.length-1];
	}

}
