package Matrix;

public class SurroundedRegions {
	
	public static void scanEdges(char[][] board){
		for(int i=0; i<board[0].length; i++){
			if(board[0][i] == 'O')
				flipEdge(board, 0, i, 1);
		}
		int lastCol = board[0].length-1;
		int lastRow = board.length - 1;
		for(int i=0; i<board.length; i++){
			if(board[i][lastCol] == 'O')
				flipEdge(board, i, lastCol, 2);
		}
		for(int i=0; i<board[0].length; i++){
			if(board[lastRow][i] == 'O')
				flipEdge(board, lastRow, i, 3);
		}
		for(int i=0; i<board.length; i++){
			if(board[i][0] == 'O')
				flipEdge(board, i, 0, 4);
		}
	}
	
	public static void flipEdge(char[][] board, int row, int col, int flag){
		if((row<board.length) && (row>=0) && (col>=0) && (col < board[0].length)){
			if(board[row][col] == 'O'){
				board[row][col] = '+';
				if(flag == 1)
					flipEdge(board, row+1, col, flag);
				if(flag == 2)
					flipEdge(board, row, col-1, flag);
				if(flag == 3)
					flipEdge(board, row-1, col, flag);
				if(flag == 4)
					flipEdge(board, row, col+1, flag);
			}
		}
	}
	
	public static void solve(char[][] board) {
        // Start typing your Java solution below
        // DO NOT write main() function
        scanEdges(board);
        for(int i=0; i < board.length; i++){
        	for(int j=0; j < board[0].length; j++){
        		if(board[i][j] == '+')
        			board[i][j] = 'O';
        		else if(board[i][j] == 'O')
        			board[i][j] = 'X';
        	}
        }
    }
	
	public static void main(String[] args){
		char[][] board = {{'X', 'X', 'X', 'X'}, {'X', 'O', 'O', 'X'}, 
				{'X', 'X', 'O', 'X'}, {'X', 'O', 'X', 'X'}};
		for(int i=0; i < board.length; i++){
        	for(int j=0; j < board[0].length; j++){
        		System.out.print(board[i][j] + " ");
        	}
        	System.out.println("");
        }
		solve(board);
		for(int i=0; i < board.length; i++){
        	for(int j=0; j < board[0].length; j++){
        		System.out.print(board[i][j] + " ");
        	}
        	System.out.println("");
        }
	}
}
