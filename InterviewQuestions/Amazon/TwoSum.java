package Amazon;
import java.util.Hashtable;

public class TwoSum {
	public int[] twoSum(int[] numbers, int target) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>();
        int[] result = new int[2];
        for(int i=0; i<numbers.length; i++){
        	if(table.containsKey(numbers[i])){
        		result[0] = table.get(numbers[i]);
        		result[1] = i+1;
        		break;
        	}
        	else{
        		table.put(target-numbers[i], i+1);
        	}
        }
        
        return result;
    }

}
