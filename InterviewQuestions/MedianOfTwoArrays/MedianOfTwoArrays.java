package MedianOfTwoArrays;

public class MedianOfTwoArrays {
	
	public static double findMedian(int[] a, int start_a, int end_a,
			int[] b, int start_b, int end_b){
		System.out.println(start_a + " " + end_a + " " + start_b + " " +end_b);
		
		if(start_a > end_a)
			return findMedianSingleArray(b, start_b, end_b);
		else if(start_b > end_b)
			return findMedianSingleArray(a, start_a, end_a);
		else if(start_a == end_a)
			return findMedianBaseCase1(a, start_a, b, start_b, end_b);
		else if(start_b == end_b)
			return findMedianBaseCase1(b, start_b, a, start_a, end_a);
		else if((start_a+1) == end_a)
			return findMedianBaseCase2(a, start_a, end_a, b, start_b, end_b);
		else if((start_b+1) == end_b)
			return findMedianBaseCase2(b, start_b, end_b, a, start_a, end_a);
		
		int ma = (start_a + end_a)/2;
		int mb = (start_b + end_b)/2;
		if(a[ma] > b[mb]){
			int k = min(end_a-ma, mb-start_b);
			return findMedian(a, start_a, end_a-k, b, start_b+k, end_b);
		}
		else{
			int k = min(ma-start_a, end_b-mb);
			return findMedian(a, start_a+k, end_a, b, start_b, end_b-k);
		}
	}
	
	public static int min(int a, int b){
		if(a>b)
			return b;
		return a;
	}
	
	public static double findMedianBaseCase2(int[] n, int start_n, int end_n,
			int[] m, int start_m, int end_m){
		if((start_m+1) == end_m){
			if(n[0] > m[0]){
				if(n[1] < m[1])
					return (n[0] + n[1]) / 2.0;
				else
					return (n[0] + m[1]) / 2.0;
			}
			else{
				if(n[1] > m[1])
					return (m[0] + m[1]) / 2.0;
				else
					return (m[0] + n[1]) / 2.0;
			}
		}
		
		int median = (start_m + end_m)/2;
		if((end_m-start_m+1)%2 == 0){
			if(n[start_n] > m[median]){
				if(n[start_n] < m[median+1]){
					if(n[start_n+1] < m[median+1])
						return (n[start_n] + n[start_n+1])/2.0;
					else
						return (n[start_n] + m[median+1])/2.0;
				}
				else{
					return (n[start_n] + m[median+1])/2.0;
				}
			}
			else{
				if(n[start_n+1] < m[median])
					return (n[start_n+1] + m[median]) /2.0;
				else{
					if(n[start_n+1] < m[median+1])
						return (n[start_n+1] + m[median]) /2.0;
					else
						return (m[median+1] + m[median]) /2.0;
				}
			}
		}
		else{
			if(n[start_n] > m[median]){
				if(n[start_n] < m[median+1])
					return n[start_n];
				else
					return m[median+1];
			}
			else{
				if(n[start_n + 1] > m[median])
					return m[median];
				else
					return n[start_n + 1];
			}
		}
			
	}
	
	public static double findMedianBaseCase1(int[] a, int start_a, int[] b,
			int start_b, int end_b){
		if(start_b == end_b)
			return (a[start_a] + b[start_b])/2.0;
		
		if((end_b-start_b+1)%2 == 0){
			if(a[start_a] < b[(end_b+start_b)/2])
				return b[(end_b+start_b)/2];
			else{
				if(a[start_a] < b[(end_b+start_b)/2+1])
					return a[start_a];
				else
					return b[(end_b+start_b)/2+1];
			}
		}
		else{
			if(a[start_a] < b[(end_b+start_b)/2]){
				if(a[start_a] < b[(end_b+start_b)/2-1])
					return ((b[(end_b+start_b)/2-1] + b[(end_b+start_b)/2])/2.0);
				else
					return ((a[start_a] + b[(end_b+start_b)/2])/2.0);
			}
			else{
				if(a[start_a] < b[(end_b+start_b)/2+1])
					return ((a[start_a] + b[(end_b+start_b)/2])/2.0);
				else
					return ((b[(end_b+start_b)/2+1] + b[(end_b+start_b)/2])/2.0);
			}
		}
			
	}
	
	public static double findMedianSingleArray(int[] n, int start, int end){
		if((end-start+1)%2 == 0)
			return (n[(end+start)/2] + n[(end+start)/2+1])/2.0;
		else
			return n[(end+start)/2];
	}
	
	public static void main(String[] args){
		int[] a = {1, 2, 10, 11};
		int[] b = {3, 5};
		System.out.println(findMedian(a, 0, a.length-1, b, 0, b.length-1));
	}

}
