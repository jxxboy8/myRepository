package ListAndArray;

public class Search2DMatrix {
	
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix[0].length;
        int n = matrix.length;
        int i = 0;
        int j = m-1;
        while((j >= 0) && (i < n)){
        	while((i < n) && (matrix[i][j] < target))
        		i++;
        	if(i == n)
        		return false;
            if(matrix[i][j] == target)
        		return true;
            while((j>=0) && (matrix[i][j] > target))
            	j--;
            if(j < 0)
            	return false;
            if(matrix[i][j] == target)
        		return true;
        }
        return false;
    }

}
