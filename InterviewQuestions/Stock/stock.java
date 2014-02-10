package Stock;
import java.util.ArrayList;

public class stock {
	public int maxProfit(int[] prices){
		if(prices.length == 0)
			return 0;
		int maxProfit = 0;
		int min = prices[0];
		for(int i=0; i<prices.length; i++){
			if(prices[i] < min)
				min = prices[i];
			else{
				int temp = prices[i] - min;
				if(temp > maxProfit)
					maxProfit = temp;
			}
		}
		return maxProfit;
	}
	
	public int maxProfit2(int[] prices) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		if(prices.length == 0)
			return 0;
		int maxProfit = 0;
        int buy = 0;
        int sell = 0;
        for(int i=0; i<prices.length; i++){
        	if(prices[i] <= prices[sell]){
        		if(buy != sell)
        			maxProfit = maxProfit + prices[sell] - prices[buy];
        		buy = i;
        		sell = i;
        	}
        	else{
        		sell = i;
        	}
        }
        maxProfit = maxProfit + prices[sell] - prices[buy];
        return maxProfit;
    }
	
	public int maxProfit3(int[] prices) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		if(prices.length < 2)
			return 0;
		int[] before = new int[prices.length];
		int[] after = new int[prices.length];
		int min = prices[0];
		before[0] = 0;
		for(int i=1; i<prices.length; i++){
			if(prices[i] < min){
				min = prices[i];
				before[i] = before[i-1];
			}
			else{
				int tmp = prices[i] - min;
				if(tmp > before[i-1])
					before[i] = tmp;
				else
					before[i] = before[i-1];
			}
		}
		after[prices.length-1] = 0;
		int max = prices[prices.length-1];
		int maxProfit=before[prices.length-1];
		for(int i=prices.length-2; i>0; i--){
			if(prices[i] > max){
				max=prices[i];
				after[i] = after[i+1];
			}
			else{
				int tmp = max-prices[i];
				if(tmp > after[i+1])
					after[i] = tmp;
				else
					after[i] = after[i+1];
			}
			int sum = after[i] + before[i-1];
			if(sum > maxProfit)
				maxProfit = sum;
		}
		return maxProfit;
    }

}
