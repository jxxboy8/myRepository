package Amazon;

public class FirstChar {
	
	public static boolean isSubstring(String s1, String s2){
		for(int i=0; i<s1.length(); i++){
			if(s1.charAt(i) == s2.charAt(0)){
				int j = 0;
				for(j=0; j<s2.length(); j++){
					if(s2.charAt(j) != s1.charAt(i+j))
						break;
				}
				if(j == s2.length())
					return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args){
		String s = "The bucket is full of water";
		String s1 = "bucket";
		String s2 = "off";
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i < s.length(); i++){
			if(s.charAt(i) != ' '){
				if(((i>0) && (s.charAt(i-1) == ' ')) || (i == 0))
					buffer.append(s.charAt(i));				
			}
		}
		System.out.println(buffer.toString());
		System.out.println(isSubstring(s, s1));
		System.out.println(isSubstring(s, s2));
	}

}
