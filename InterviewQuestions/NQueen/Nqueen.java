package NQueen;

public class Nqueen {

	private int number = 0;
	
	public int totalNQueens(int n) {
        // Start typing your Java solution below
        // DO NOT write main() function
		number = 0;
		int[] list = new int[n];
		totalNumber(0, list);
        return number;
    }
	
	public void totalNumber(int row, int[] list){
		if(row == list.length){
			number++;
			return;
		}
		
		for(int i=0; i<list.length; i++){
			if(valid(row, i, list)){
				list[row] = i;
				totalNumber(row+1, list);
			}
		}
	}
	
	public boolean valid(int row, int col, int[] list){
		for(int i=0; i<row; i++){
			if((col == list[i]) || (Math.abs(row-i) == Math.abs(col-list[i])))
				return false;
		}
		
		return true;
	}
}
