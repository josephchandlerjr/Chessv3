package com.chessv3;

import java.util.*;

/**
 * Model in our MVC
 * validates and performs moves on the board
 * holds a Score object, a scoresheet of moves
 */
public class ChessModelImpl2 implements ChessModel{
	private List<Observer> observers = new ArrayList<Observer>();
	private ChessState state;
	private int[][] board;

	int NONE = -1;

	int BLACK = 0;
	int WHITE = 1; // same as piece % 2

	int PLAYER; 
	int OPPONENT;

	int WHITEPAWN = 1;
	int WHITEROOK = 3;
	int WHITEBISHOP = 5;
	int WHITEKNIGHT = 7;
	int WHITEQUEEN = 9;
	int WHITEKING = 11;

	int BLACKPAWN = 2;
	int BLACKROOK = 4;
	int BLACKBISHOP = 6;
	int BLACKKNIGHT = 8;
	int BLACKQUEEN = 10;
	int BLACKKING = 12;

	int NORMAL = 22;
	int ENPASSANT = 33; 
	int TWOROWPAWN = 44;
	int CASTLEQUEEN = 55;
	int CASTLEKING = 66;

	boolean WhiteHasCastled;
	boolean blackHasCastled;
	boolean whiteKingHasMoved;
	boolean blackKingHasMoved;

	int[] lastMove = {-1,-1,-1,-1}; // so as not to be mistaken for an actual previous move

	/**
	 * API used it initialize a new game
	 */
	public void newGame(){
		initializeBoard();
		state = new ChessState();
		PLAYER = WHITE;
		OPPONENT= BLACK;
		updateStateObject(new ChessMove());
		notifyObservers();
	}
	private void initializeBoard(){
		board = {{ 4, 8, 6,10,12, 6, 8, 4},   
			 { 2, 2, 2, 2, 2, 2, 2, 2},
			 {-1,-1,-1,-1,-1,-1,-1,-1},
			 {-1,-1,-1,-1,-1,-1,-1,-1},
			 {-1,-1,-1,-1,-1,-1,-1,-1},
			 {-1,-1,-1,-1,-1,-1,-1,-1},
			 { 1, 1, 1, 1, 1, 1, 1, 1},
			 { 3, 7, 5, 9,11, 5, 7, 3}   
			};
	}

	/**
	 * API used to register observers
	 */
	public void registerObserver(Observer obs){
		observers.add(obs);
	}
	public void notifyObservers(){
		Iterator<Observer> iterator = observers.iterator();
		while(iterator.hasNext()){
			iterator.next().update();
		}
	}


	/**
	 * API
	 * @return a state object
	 */
	public ChessState getState(){
		return state;
	}

	/**
	 * API exposed to other objects used to make a chess move
	 */
	public void takeAction(int fromRow, int fromCol, int toRow, int toCol){
		int[] fromSquare = {fromRow, fromCol};
		int[] toSquare = {toRow, toCol};
		int attacker = board[fromRow][fromCol];
		int attacked = board[toRow][toCol];
		int attackerColor = getPieceColor(attacker);
		int moveType = getMoveType(attacker, attacked, fromRow, fromCol, toRow, toCol);

		boolean moveLeavesColumn = !sameCol(fromCol,toCol); 
		boolean moveLeavesRow =    !sameRow(fromRow,toRow);
		boolean moveLeavesBackDiagonal = !sameBackDiagonal(fromRow, fromCol, toRow, toCol);
		boolean moveLeavesForwardDiagonal = !sameForwardDiagonal(fromRow, fromCol, toRow, toCol);
		boolean attackerIsKing = isKing(attacker);

		// check initial conditions
		//
		// is there a piece to move
		if (attacker == NONE)
			return null;
		// is attacker of correct color
		if ( attackerColor != PLAYER) 
			return null;
		// is attacked either NONE or opponent
		if (attacked != NONE && areSameColor(attacker, attacked)
			return null;	
		// is pin preventing move                           
		if (!attackerIsKing &&
		   (!moveLeavesColumn && isPinnedToColumn) ||
		   (!moveLeavesRow && isPinnedToRow) ||
		   (!moveLeavesBackDiagonal && isPinnedToBackDiagonal) ||
		   (!moveLeavesForwardDiagonal && isPinnedToForwardDiagonal) ){
			return null;
		} 
		is king being in check going to be an issue
		if (attackerIsKing){
			if (isAttacked(toRow,toCol){
				return null;
			}
		}else{
			int[] kingLocation = findKing(attackerColor);
			if (isAttacked(kingLocation){
				int[][] threats = attackersOfLocation(kingLocation);
				for (int[] threateningSquare : threats){
					int row = threateningSquare[0];
					int col = threateningSquare[1];
					int piece = board[row][col];
					if(toRow != row && toCol != col){ //attacker is capturing piece, all good
						break;
					}
					if(isKnight(piece)){ // if knight is attacking king
						return null;
					}
					if( !isBetween(toSquare, kingLocation, threateningSquare) ){
						return null;
					}
				}
			}
		}
		//now, with move type known, check all nuances specific to that move type and board layout
		if(!isValidMove(fromSquare, toSquare, attacker, attacked, attackerColor){
			return false;
		}
		
		int[][] actions = isValidMove(int fromRow, int fromCol, int toRow, int toCol);
		if (actions != null){
			// go through each action and make it so
			updateStateObject(// interface for updateStateObject must change, it was move);
			notifyObservers();
		}
	}

	private int getMoveType(int attacker, int attacked, int fromRow, int fromCol, int toRow, int toCol){
		if(isPawn(attacker)){
			if( Math.abs(fromRow - toRow) == 2 ){
				return TWOROWPAWN;
			}
			if(fromCol != toCol && attacker == NONE){
				return ENPASSANT;
			}

		}else if (isKing(attacker){
			int colDiff = Math.abs(fromCol - toCol);
			if(colDiff == 2){
				return CASTLEKING;
			}
			if(colDiff == 3){
				return CASTLEQUEEN;
			}
		}
		return NORMAL:
	}

	private int getRow(int[] location){
		return location[0];
	}
	private int getCol(int[] location){
		return location[1];
	}

	/*
	* returns null if false or 2d array ->[row, col, piece], essentially saying set all these squares as such
	*/
	private int[][] isValidMove(int fromRow, int fromCol, int toRow, int toCol){ 
		

	
		

		
		if(moveType == ENPASSANT){
			int[][] result ={
					{ fromRow, fromCol, -1},
					{ toRow, toCol, attacker },
					{ fromRow, toCol, -1 },      
					};
		}else{
			int[][] result ={
					{ fromRow, fromCol, -1},
					{ toRow, toCol, attacker },
					};
		}
		return result;
	}

	//returns true if all three pieces are together, on same column, row or diagonal and middle in middle 
	private boolean isBetween(int[] middle, int[] end1, int[] end2) {
		int end1Row = getRow(end1);
		int end1Col = getCol(end1);
		int end2Row = getRow(end2);
		int end2Col = getCol(end2);
		int middleRow = getRow(middle);
		int middleCol = getCol(middle);
		// if same column check row, if same row or diag check col, else false
		if (sameCol(end1,middle,end2)){
			if (!( (end1Row > middleRow && middleRow > end2Row) ||
			       (end1Row < middleRow && middleRow < end2Row)  ) 
			   ){
				return false;
				}
		}else if( sameRow(end1,middle,end2) ||
			  sameBackDiagonal(end1,middle, end2) ||
			  sameForwardDiagonal(end1,middle, end2)
			){
			if (!( (end1Col > middleCol && middleCol > end2Col) ||
			       (end1Col < middleCol && middleCol < end2Col)  ) 
			   ){
				return false;
				}
		}else{
			return false;
		}
		if( piecesBetween(end1,end2) || piecesBetween(end1,middle) || piecesBetween(end2,middle) ){
			return false;
		}
		return true;
	}

	private boolean isAttacked(int[] location){
		return attackersOfLocation(location).length > 0;
	}
	private boolean isAttacked(int row, int col){
		int[] location = {row, col};
		return isAttacked(location);
	}

	private int[][] attackersOfLocation(int[] location){
		int attackedRow = location[0];
		int attackedCol = location[1];
		for (
		// go through and find them, prob return List instead of int[] indicated in signature above
		// but essential return a int[2][x] array of square locations that are attacking said location
	}

	private int[] findKing(int color){
		for(int row : board){
			for (int col : row){
				int piece = board[row][col];
				if( isKing(piece)  && (piece % 2 == color) ){
					return new int[]{row, col}; // anonymous array
				}
			}
		}
		//will throw compile time exception cause no return statement here, fix later
	}


	private boolean isKing(int piece){
		return piece == 11 || piece == 12;
	}
	private boolean isQueen(int piece){
		return piece == 9 || piece == 10;
	}
	private boolean isKnight(int piece){
		return piece == 7 || piece == 8;
	}
	private boolean isBishop(int piece){
		return piece == 5 || piece == 6;
	}
	private boolean isRook(int piece){
		return piece == 3 || piece == 4;
	}
	private boolean isPawn(int piece){
		return piece == 1 || piece == 2;
	}

	private boolean sameColumn(int[] sqr1, int[] sqr2){
		int fromCol = sqr1[1];
		int toCol = sqr2[1];
		return sameColumn(fromCol, toCol);
	}
	private boolean sameColumn(int[] sq1, int[] sqr2, int[] sqr3){
		return sameColumn(sqr1,sqr2) && 
		       sameColumn(sqr1,sqr3) && 
		       sameColumn(sqr2,sqr3);
	}
	private boolean sameRow(int[] sq1, int[] sqr2, int[] sqr3){
		return sameRow(sqr1,sqr2) && 
		       sameRow(sqr1,sqr3) && 
		       sameRow(sqr2,sqr3);
	}
	private boolean sameColumn(int fromCol, int toCol){
		return fromCol == toCol;
	}

	private boolean sameRow(int fromRow, int toRow){
		return fromRow == toRow;
	}

	private boolean sameRow(int[] sqr1, int[] sqr2){
		int fromRow = sqr1[0];
		int toRow = sqr2[0];
		return sameRow(fromRow, toRow);
	}


	private boolean sameBackDiagonal(int[] sqr1, int[] sqr2){
		int fromRow = sqr1[0];
		int fromCol = sqr1[1];
		int toRow = sqr2[0]
		int toCol = sqr2[1];
		return sameBackDiagonal(fromRow, fromCol, toRow, toCol);
	}
	private boolean sameBackDiagonal(int[] sq1, int[] sqr2, int[] sqr3){
		return sameBackDiagonal(sqr1,sqr2) && 
		       sameBackDiagonal(sqr1,sqr3) && 
		       sameBackDiagonal(sqr2,sqr3);
	}
	private boolean sameForwardDiagonal(int[] sqr1, int[] sqr2){
		int fromRow = sqr1[0];
		int fromCol = sqr1[1];
		int toRow = sqr2[0]
		int toCol = sqr2[1];
		return sameForwardDiagonal(fromRow, fromCol, toRow, toCol);
	}
	private boolean sameForwardDiagonal(int[] sq1, int[] sqr2, int[] sqr3){
		return sameForwardDiagonal(sqr1,sqr2) && 
		       sameForwardDiagonal(sqr1,sqr3) && 
		       sameForwardDiagonal(sqr2,sqr3);
	}

	private boolean sameBackDiagonal(int fromRow, int fromCol, int toRow, int toCol){ // goes like this \
		int rowDiff = fromRow - toRow;
		int colDiff = fromCol - toCol;
		int absRowDiff = Math.abs(rowDiff);
		int absColDiff = Math.abs(colDiff);
		return rowDiff == colDiff;
		
	}
	private boolean sameForwardDiagonal(int fromRow, int fromCol, int toRow, int toCol){ // goes like this /
		int rowDiff = fromRow - toRow;
		int colDiff = fromCol - toCol;
		int absRowDiff = Math.abs(rowDiff);
		int absColDiff = Math.abs(colDiff);
		return (absRowDiff == absColDiff) && (rowDiff != colDiff); 
		 
	}

	private boolean isPinnedToColumn(int kingColor, int row, int col){}
	private boolean isPinnedToRow(int kingColor, int row, int col){}
	private boolean isPinnedToBackDiagonal(int kingColor, int row, int col){}
	private boolean isPinnedToForwardDiagonal(int kingColor, int row, int col){}

	private int getPieceColor(int piece){
		return piece % 2;
	}
	private int areSameColor(int piece1, int piece2){
		return getPieceColor(piece1) == getPieceColor(piece2)
	}

	private boolean isUnderAttack(int row, int col){}

	private boolean piecesBetween(int[] from, int[] to) {
			List<int[]> squares = squaresBetween(from, to);
			for (in[] square : squares) {
				if (getPiece(square) != NONE) {
					return true;
				}
			}
			return false;
		}

	private int getPiece(int[] location){
		int row = location[0];
		int col = location[1];
		return board[row][col];
	}

	private List<int[]> squaresBetween(int[] from, int[] to){
			int fromRow = from[0];
			int fromCol = from[1];
			int toRow = to[0];
			int toCol = to[1];
			return squaresBetween(fromRow,fromCol,toRow,toCol);	
	}

	private List<int[]> squaresBetween(int fromRow, int fromCol, int toRow, int toCol) {       
			List<int[]> result = new ArrayList<int[]>();
			int rowStep;
			int colStep;
			 
			if     (toRow > fromRow){ rowStep = 1;}
			else if(toRow < fromRow){ rowStep = -1;}
			else                    { rowStep = 0;}
			 
			if     (toCol > fromCol){ colStep = 1;}
			else if(toCol < fromCol){ colStep = -1;}
			else                    { colStep = 0;}	 

			int col = fromCol + colStep;
			int row = fromRow + rowStep;

			while (col != toCol || row != toRow){	
				result.add(new int[]{row,col});
				col = col + colStep;
				row = row + rowStep;
			}
			return result;

}


