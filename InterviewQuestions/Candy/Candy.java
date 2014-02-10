package Candy;

public class Candy {
	public static int candy(int[] ratings) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        int i,k,len = ratings.length,sum=0;
   int[] res = new int[len];

   for(k=1,i=0;i<len;i++){
       if( (i-1)>=0 && ratings[i]>ratings[i-1] ){
          k++;
          res[i]=k;
       }
       else{                                       
          k=1;
          res[i] = 1;
       }
   }

   for(k=1,i=len-1;i>=0;i--){
       if((i+1)<len && ratings[i]>ratings[i+1] ){    
    	   k++;
    	   if(k > res[i])
    		   res[i]=k;
       }
       else                                         
    	   k=1;
   }

   for(i=0;i<len;i++){
	   System.out.print(res[i]);
       sum+=res[i];
   }

   return sum;
    }
	
	public static void main(String[] args){
		int[] ratings = {2, 1};
		System.out.print(candy(ratings));
	}
}
