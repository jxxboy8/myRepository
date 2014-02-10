package Matrix;

import java.util.ArrayList;

public class SpiralMatrix {
	
	public static ArrayList<Integer> spiralOrder(int[][] matrix) {
        // Start typing your Java solution below
        // DO NOT write main() function
		ArrayList<Integer> result = new ArrayList<Integer>();
        if(matrix.length == 0)
            return result;
        int top = 0;
        int right = matrix[0].length-1;
        int bottom = matrix.length-1;
        int left = 0;
        while(true){
        	for(int j=left; j<=right; j++){
        		result.add(matrix[top][j]);
        		System.out.println(matrix[top][j]);
        	}
        	if((top++) > bottom)
        		break;
        	for(int i=top; i<= bottom; i++)
        		result.add(matrix[i][right]);
        	if((right--) < left)
        		break;
        	for(int j=right; j>=left; j--)
        		result.add(matrix[bottom][j]);
        	if((bottom--) < top)
        		break;
        	for(int i=bottom; i>=top; i--)
        		result.add(matrix[i][left]);
        	if((left++) > right)
        		break;
        }
        return result;
    }
	
	public static void main(String[] args){
		int[][] matrix = {{2,3}};
		ArrayList<Integer> result = spiralOrder(matrix);
	}

}
