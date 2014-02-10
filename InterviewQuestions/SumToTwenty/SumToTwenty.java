package SumToTwenty;

import java.util.ArrayList;

public class SumToTwenty {
	
	public static ArrayList<Integer> SumToTwenty(){
		
		//get all combinations
		ArrayList<ArrayList<Integer>> combs = new ArrayList<ArrayList<Integer>>();
		combs = getCombinations();    
		
		//get all permutations for all combinations
		ArrayList<Integer> allNumbers = new ArrayList<Integer>();
		for(int i=0; i<combs.size(); i++){
			addPerms(allNumbers, combs.get(i), 0, 3);
		}
		return allNumbers;
	}

	public static ArrayList<ArrayList<Integer>> getCombinations(){
		ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
		int[] digits = new int[4];
		addCombs(results, digits, 0);
		return results;
	}
	
	public static void addCombs(ArrayList<ArrayList<Integer>> results, int[] digits, 
			int index){
		// make digits in ascending order, then we won't find same combinations
		int start = 0;
		if(index > 0){
			start = digits[index-1] + 1;
		}
		
		//when reach the last digit
		if(index == 3){
			int last = 20 - digits[0] - digits[1] - digits[2];
			if((last >= start) && (last < 10))
				digits[3] = last;
			else
				return;
			ArrayList<Integer> newComb = new ArrayList<Integer>();
			for(int i=0; i<4; i++)
				newComb.add(digits[i]);
			results.add(newComb);
			return;
		}
			
		for(int i=start; i<9; i++){
			digits[index] = i;
			
			//if we need to change the previous digit
			if(digitSum(digits, index)+i+1 > 20)
				return;
			addCombs(results, digits, index+1);
		}
	}
	
	public static int digitSum(int[] digits, int end){
		int sum = 0;
		for(int i=0; i<=end; i++){
			sum = sum + digits[i];
		}
		return sum;
	}
	
	public static void addPerms(ArrayList<Integer> allNumbers, 
			ArrayList<Integer> comb, int start, int end){
		if(start == end){
			int temp = 0;
			for(int i=0; i<comb.size(); i++){
				temp = temp*10 + comb.get(i);
			}
			allNumbers.add(temp);
		}
		else{
			for(int i=start; i<=end; i++){
				int temp = comb.get(start);
				comb.set(start, comb.get(i));
				comb.set(i, temp);
				addPerms(allNumbers, comb, start+1, end);
				temp = comb.get(start);
				comb.set(start, comb.get(i));
				comb.set(i, temp);
			}
		}
	}
	
	public static void main(String[] args){
		ArrayList<ArrayList<Integer>> combs = new ArrayList<ArrayList<Integer>>();
		combs = getCombinations();
		printCombs(combs);
		System.out.println("");
		
		ArrayList<Integer> allNumbers = new ArrayList<Integer>();
		ArrayList<Integer> comb = new ArrayList<Integer>();
		comb.add(0);
		comb.add(3);
		comb.add(8);
		comb.add(9);
		addPerms(allNumbers, comb, 0, 3);
		printNumbers(allNumbers);
		System.out.println("");
		
		allNumbers = SumToTwenty();
		printNumbers(allNumbers);
	}
	
	public static void printCombs(ArrayList<ArrayList<Integer>> combs){
		for(int i=0; i<combs.size(); i++){
			for(int j=0; j<4; j++){
				System.out.print(combs.get(i).get(j) + " ");
			}
			System.out.println(" ");
		}
	}
	
	public static void printNumbers(ArrayList<Integer> allNums){
		for(int i=0; i<allNums.size(); i++){
			System.out.println(allNums.get(i));
		}
	}
	
}
