package Pow;

public class Pow {
	
    public double pow(double x, int n) {
        if(n == 0)
        	return 1.0;
        double half = pow(x, n/2);
        if(n%2 == 0)
        	return half * half;
        if(n > 0)
        	return half * half * x;
        
        return half * half / x;
    }

}
