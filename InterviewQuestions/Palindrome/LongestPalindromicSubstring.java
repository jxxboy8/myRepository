package Palindrome;

public class LongestPalindromicSubstring {
	
	public static String findLongestPalin(String s){
		int index = s.length()*2-1;
		String longestPalin = "";
		String temp = "";
		for(int i=0; i<index; i++){
			temp = expand(s, i);
			if(temp.length() > longestPalin.length())
				longestPalin = temp;
		}
		return longestPalin;
	}
	
	public static String expand(String s, int n){
		int left=0;
		int right=0;
		if(n%2 == 0){
			left = n/2;
			right = n/2;
			while((left>=0) && (right < s.length())){
				if(s.charAt(left) == s.charAt(right)){
					left--;
					right++;
				}
				else
					break;
			}
			return s.substring(left+1, right);
		}
		else{
			left = (n-1)/2;
			right = left+1;
			while((left>=0) && (right < s.length())){
				if(s.charAt(left) == s.charAt(right)){
					left--;
					right++;
				}
				else
					break;
			}
			if(left == (right-1))
				return "";
			
			return s.substring(left+1, right);
		}
	}
	
	public static void main(String[] args){
		String s = "adasfasafsadfsfggsdasddsad";
		System.out.println(findLongestPalin(s));
		String s1 = "ad";
		System.out.println(findLongestPalin(s1));
	}

}
