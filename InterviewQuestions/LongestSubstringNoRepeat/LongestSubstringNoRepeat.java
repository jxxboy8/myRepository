package LongestSubstringNoRepeat;
import java.util.ArrayList;

public class LongestSubstringNoRepeat {
	
	public int LongestSubstringNoRepeat(String s){
		int max = 0;
		int i = 0, j = 0;
		boolean[] exist = new boolean[256];
		while(i < s.length()){
			if(exist[s.charAt(i)]){
				max = Math.max(max, i-j);
				while(s.charAt(j) != s.charAt(i)){
					exist[s.charAt(j)] = false;
					j++;
				}
				i++;
				j++;
			}
			else{
				exist[s.charAt(i)] = true;
				i++;
			}
		}
		max = Math.max(max, s.length()-j);
		return max;
	}

}
