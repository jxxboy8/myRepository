package Wordladder;

import java.util.HashSet;
import java.util.LinkedList;

public class wordladder {
	public int ladderLength(String start, String end, HashSet<String> dict){
		HashSet<String> set = new HashSet<String>();
		LinkedList<String> queue = new LinkedList<String>();
		queue.add(start);
		set.add(start);
		int distance = 1;
		int count = 1;
		
		while(!queue.isEmpty()){
			while(count > 0){
				char[] charlist = queue.removeFirst().toCharArray();
				for(int i=0; i < charlist.length; i++){
					char temp = charlist[i];
					for(char c='a'; c<='z'; c++){
						if(c == charlist[i])
							continue;
						charlist[i] = c;
						String str = new String(charlist);
						if(str.equals(end))
							return distance+1;
						if(dict.contains(str) && !set.contains(str)){
							queue.add(str);
							set.add(str);
						}
					}
					charlist[i] = temp;
				}
				count--;
			}
			distance++;
			count = queue.size();
		}
		return 0;
	}

}
