/*
 * Connect Four Twilio Game
 * by Mitchell Ryzuk and Eric Lai
 * 2/25/2018
 */
package com.Twilio;

public class Game {
	// Data Fields
	
	private final static int WIDTH = 7;
	private final static int HEIGHT = 6;
	private int[][] board = new int[HEIGHT][WIDTH];

// an example tied board.
//	board = new char[][]{{'c','x','c','x','c','x','c'},
//			{'x','c','x','c','x','c','x'},
//			{'c','x','c','x','c','x','c'},
//			{'x','c','x','c','x','c','x'},
//			{'c','x','c','x','c','x','c'},
//			{'x','c','x','c','x','c','x'}};
	
	// Constructor
	
	public Game() {
		
	}
		
	// Methods
	
	/**
	 * Checks if there is a win condition for a piece.
	 * @param piece The piece being checked.
	 * @return 0 if win, 1 if tied, 2 if no win
	 */
	public int winCondition(int piece) {
		boolean tie = true;
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				if (this.winHelper(piece, row, col)) {
					return 0;
				}
				if (board[row][col] == 0) {
					tie = false;
				}
			}
		}
		
		if (tie) {
			return 1;
		} else {
			return 2;
		}
	}
	
			private boolean winHelper(int piece, int row, int col) {
				if (this.winDiagRight(piece, row, col) >= 4 || this.winDiagLeft(piece, row, col) >= 4 ||
						this.winUp(piece, row, col) >= 4 || this.winHor(piece, row, col) >= 4) {
					return true;
				}
				return false;
			}
	
	private int winVert(int piece, int row, int col) {
		return this.winUp(piece, row, col) + this.winDown(piece, row, col) - 1;
	}
	
			private int winUp(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				return 1 + winUp(piece, row-1, col);
			}
			
			private int winDown(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				return 1 + winDown(piece, row+1, col);
			}
			
	private int winHor(int piece, int row, int col) {
		return this.winLeft(piece, row, col) + this.winRight(piece, row, col) - 1;
	}
	
			private int winLeft(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				return 1 + winLeft(piece, row, col-1);
			}
			
			private int winRight(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				return 1 + winRight(piece, row, col+1);
			}
	
	private int winDiagLeft(int piece, int row, int col) {
		return this.winNw(piece, row, col) + this.winSe(piece, row, col) - 1;
	}
	
			private int winNw(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				
				return 1 + winNw(piece, row-1, col-1);
			}
			
			private int winSe(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				
				return 1 + winSe(piece, row+1, col+1);
			}
	
	private int winDiagRight(int piece, int row, int col) {
		return this.winNe(piece, row, col) + this.winSw(piece, row, col) - 1;
	}
	
			private int winNe(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				} 
				
				return 1 + winNe(piece, row-1, col+1);
				
			}
			
			private int winSw(int piece, int row, int col) {
				if (!validSpace(col, row) || board[row][col] != piece) {
					return 0;
				}
				
				return 1 + winSw(piece, row+1, col-1);
			}
	
	public static boolean validSpace(int col, int row) {
		if (col < 0 || col >= WIDTH || row < 0 || row >= HEIGHT) {
			return false;
		}
		return true;
	}
	
	public boolean addElement(int piece, int col) {
		if (col < 1 || col > WIDTH) {
			return false;
		}
		int inCol = col-1;
		for(int row = HEIGHT - 1; row >= 0; row--) {
			
			if (board[row][inCol] == 0) {
				board[row][inCol] = piece;
				return true;
			}
			
		}
		return false;
	}
	
	public StringBuffer toStringB() {
		StringBuffer rep = new StringBuffer();
		
		for(int col = 0; col < WIDTH; col++) {
			rep.append("---");
		}
		rep.append("\n");
		
		for(int col = 0; col < HEIGHT; col++) {
			for(int row = 0; row < WIDTH; row++) {
				if (board[col][row] == 0) {
					rep.append(Character.toChars(11036));
				} else {
					rep.append(Character.toChars(board[col][row]));
				}
			}
			rep.append("\n");
		}
		
		for(int col = 0; col < WIDTH; col++) {
			rep.append("---");
		}
		rep.append("\n");
		
		for(int i = 1; i <= WIDTH; i++) {
			rep.append(Character.toChars(48 + i));
			rep.append(Character.toChars(65039));
			rep.append(Character.toChars(8419));
		}
		rep.append(" ");
		
		
		
		return rep;
	}
	
	public static void main(String[] args) {
		Game h = new Game();	
		System.out.println(h.toStringB());
		System.out.println(validSpace(4,4));
		System.out.println(validSpace(0,8));
		System.out.println(h.board[5][0]);
		System.out.println(h.winCondition('7'));

	}

}
