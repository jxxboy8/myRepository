package SurroundedRegions;

public class SurroundedRegions {
	
	public void solve(char[][] board){
		if((board.length < 3) || (board[0].length < 3))
			return;
		for(int col=0; col<board[0].length; col++)
			flip(board, 0, col);
		for(int col=0; col<board[0].length; col++)
			flip(board, board.length-1, col);
		for(int row=1; row<board.length-1; row++)
			flip(board, row, 0);
		for(int row=1; row<board.length-1; row++)
			flip(board, row, board[0].length-1);
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] == 'O')
					board[i][j] = 'X';
				else if(board[i][j] == '+')
					board[i][j] = 'O';
			}
		}
	}
	
	public void flip(char[][] board, int row, int col){
		if((row < 0) || (col < 0) || (col >= board[0].length) || (row >= board.length))
			return;
		if((board[row][col] == '+') || (board[row][col] == 'X'))
			return;
		board[row][col] = '+';
		flip(board, row, col+1);
		flip(board, row, col-1);
		flip(board, row+1, col);
		flip(board, row-1, col);
	}
	
}
