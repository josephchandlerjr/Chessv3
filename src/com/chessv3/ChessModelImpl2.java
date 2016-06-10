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
	
	//flags
	
	boolean blackCanCastleKingSide = true;
	boolean blackCanCastleQueenSide = true;
	boolean whiteCanCastleKingSide = true;
	boolean whiteCanCastleQueenSide = true;

	boolean WhiteQueensRookHasMoved;
	boolean WhiteKingsRookHasMoved;
	boolean blackQueensRookHasMoved;
	boolean blackKingsRookHasMoved;

	int[]  lastMove = {-1,-1,-1,-1}; // so as not to be mistaken for an actual previous move
	int lastMovetype = NONE;

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
	public void updateStateObject(ChessMove move){}//need to implement
	public void promote(int row, int col, String color, String piece){}//need to implement
	public String[][] getBoard(){return null;}//need to implement

	private void initializeBoard(){
		board = new int[][]{{ 4, 8, 6,10,12, 6, 8, 4},   
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

	public int getDirection(int color){
		if(color == WHITE)
			return -1;
		return 1;
	}

	/**
	 * determines if a pawn move is valid, not used for en passant or two-row moves
	 * assumes has been already determined that to square is either vacant or occupied by opponent
	 */
	 public boolean isValidPawnMove(int color, int[] from, int[] to){ 
		 int fromRow = getRow(from);
		 int fromCol = getCol(from);
		 int toRow = getRow(to);
		 int toCol = getCol(to);

		 int direction = getDirection(color);

		 if (fromCol == toCol         &&
	         fromRow + direction == toRow &&
		getPiece(to) == NONE){// advance one row
			 return true;
		}
		else if(fromCol-1 == toCol           &&
		        fromRow + direction == toRow &&
			getPiece(to) != NONE){ // capture diagnonal
			return true;
		}
		else if(fromCol+1 == toCol           &&
			fromRow + direction == toRow &&
			getPiece(to) != NONE){ // capture diagnonal 
				return true;
			}
		return false;
	 }

	/**
	 * determines if a rook move is a valid move
	 */
	 private boolean isValidRookMove(int[] from, int[] to){       
		if (!sameColumn(from,to) && !sameRow(from,to))
			return false;
		  
		if (piecesBetween(from, to))
			return false;
		return true;
	 }

        /**
	 * determines if a Knight is a valid move
	 */
         private boolean isValidKnightMove(int[] from, int[] to){ 
		int fromRow = getRow(from);
		int fromCol = getCol(from);
		int toRow = getRow(to);
		int toCol = getCol(to);

		int rowDiff = Math.abs(toRow - fromRow);
		int colDiff = Math.abs(toCol - fromCol);

		if (rowDiff == 2 && colDiff == 1) { return true;}
		if (rowDiff == 1 && colDiff == 2) { return true;}
		 
		return false;
		 
	 }
	/**
	 * determines if a Bishop is a valid move
	 */
	 private boolean isValidBishopMove(int[] from, int[] to){ 
		if (!sameDiagonal(from,to))
			return false;
		if (piecesBetween(from, to))
			return false;
		return true;
	 }
	/**
	 * determines if a Queen is a valid move
	 */
	 private boolean isValidQueenMove(int[] from, int[] to){
		 return isValidRookMove(from,to) || isValidBishopMove(from,to);
	 }
	/**
	 * determines if a King is a valid move
	 */
	 private boolean isValidKingMove(int[] from, int[] to){    
		int fromRow = getRow(from);
		int fromCol = getCol(from);
		int toRow = getRow(to);
		int toCol = getCol(to);

		int colDiff = toCol - fromCol;
		int rowDiff = toRow - fromRow;

		return Math.abs(colDiff) <= 1 && Math.abs(rowDiff) <= 1; 
	 }
	/**
	 * API exposed to other objects used to make a chess move
	 */
	public void takeAction(int fromRow, int fromCol, int toRow, int toCol){
		int[] fromSquare = {fromRow, fromCol};
		int[] toSquare = {toRow, toCol};
		int attacker = getPiece(fromRow,fromCol);
		int attacked = getPiece(toRow,toCol);
		int attackerColor = getPieceColor(attacker);
		int moveType = getMoveType(attacker, attacked, fromRow, fromCol, toRow, toCol);

		boolean moveLeavesColumn = !sameColumn(fromCol,toCol); 
		boolean moveLeavesRow =    !sameRow(fromRow,toRow);
		boolean moveLeavesBackDiagonal = !sameBackDiagonal(fromRow, fromCol, toRow, toCol);
		boolean moveLeavesForwardDiagonal = !sameForwardDiagonal(fromRow, fromCol, toRow, toCol);
		boolean attackerIsKing = isKing(attacker);

		// check initial conditions
		//
		// is there a piece to move
		// are to and from the same
		if(fromRow == toRow && fromCol == toCol)
			return;
		if (attacker == NONE)
			return;
		// is attacker of correct color
		if ( attackerColor != PLAYER) 
			return;
		// is attacked either NONE or opponent
		if (attacked != NONE && areSameColor(attacker, attacked))
			return;	
		// is pin preventing move                           
		if (!attackerIsKing &&
		   (moveLeavesColumn && isPinnedToColumn(attackerColor, fromRow, fromCol)) ||
		   (moveLeavesRow && isPinnedToRow(attackerColor, fromRow, fromCol)) ||
		   (moveLeavesBackDiagonal && isPinnedToBackDiagonal(attackerColor, fromRow, fromCol)) ||
		   (moveLeavesForwardDiagonal && isPinnedToForwardDiagonal(attackerColor, fromRow, fromCol)) ){
			return;
		} 
		//is king being in check going to be an issue
		if (attackerIsKing){
			if (isAttacked(toRow,toCol, otherColor(attackerColor))){
				return;              // castling, fromsquare and intermediary will be checked later
			}	
		}else{
			int[] kingLocation = findKing(attackerColor);
			if (isAttacked(kingLocation, otherColor(attackerColor))){
				int[][] threats = attackersOfLocation(kingLocation, otherColor(attackerColor));
				for (int[] threateningSquare : threats){  // for each threat to king
					int row = threateningSquare[0];
					int col = threateningSquare[1];
					int piece = getPiece(threateningSquare);
					if(sameRow(toRow,row) && sameColumn(toCol,col)){//capturing threat
						break;
					}
					if(isKnight(piece)){ // if knight is attacking king, it cannot be blocked
						return;
					}
					if( !isBetween(toSquare, kingLocation, threateningSquare) ){
						return;
					}
				}
			}
		}
		//now check all nuances specific to that move type and board layout
	
		int[][] actions = defineMove(moveType, fromSquare, toSquare, attacker, attacked, attackerColor);
		if (actions != null){
			setFlags(moveType, fromSquare, toSquare, attackerColor, attacker);

			// go through each action and make it so
			//updateStateObject(// interface for updateStateObject must change, it was move);
			//notifyObservers();
			// make appropriate updates like changing player to move and other stuff
			// mark king has moved, king has castled , king in check, notate as last move etc
		}
	}
	//to be used to initialize
	public void setFlags(){
		blackCanCastleKingSide = true;
		blackCanCastleQueenSide = true;
		whiteCanCastleKingSide = true;
		whiteCanCastleQueenSide = true;
		WhiteKingsRookHasMoved = false;
		blackQueensRookHasMoved = false;
		blackKingsRookHasMoved = false;
		lastMove = new int[]{-1,-1,-1,-1}; // so as not to be mistaken for an actual previous move
		lastMovetype = NONE;
	}
	//used aftera move is completed
	public void setFlags(int moveType, int[] fromSquare, int[] toSquare, int color, int piece){
		if(color == WHITE){
			if(isKing(piece)){
				whiteCanCastleKingSide = false;
				whiteCanCastleQueenSide = false;
			}else if (isRook(piece)){
					if(getRow(fromSquare) == 0 && 
					getCol(fromSquare) == 0 && 
					!WhiteQueensRookHasMoved){
						whiteQueensRookHasMoved = true;
						whiteCanCastleQueenSide = false;
					}
					else if(getRow(fromSquare) == 0 && 
					getCol(fromSquare) == 7 && 
					!WhiteKingsRookHasMoved){
						whiteKingssRookHasMoved = true;
						whiteCanCastleKingSide = false;
					}

			}
		}else if(color == BLACK){
			if(isKing(piece)){
				blackCanCastleKingSide = false;
				blackCanCastleQueenSide = false;
			}else if (isRook(piece)){
					if(getRow(fromSquare) == 0 && 
					getCol(fromSquare) == 0 && 
					!blackQueensRookHasMoved){
						blackQueensRookHasMoved = true;
						blackCanCastleQueenSide = false;
					}
					else if(getRow(fromSquare) == 0 && 
					getCol(fromSquare) == 7 && 
					!blackKingsRookHasMoved){
						blackKingssRookHasMoved = true;
						blackCanCastleKingSide = false;
					}

			}
		}
		latMoveType = moveType;
		lastMove = new int[]{fromSquare[0].fromSquare[1],toSquare[0],toSquare[1]}; 
	}

	public int getMoveType(int attacker, int attacked, int fromRow, int fromCol, int toRow, int toCol){
		if(isPawn(attacker)){
			if( Math.abs(fromRow - toRow) == 2 ){
				return TWOROWPAWN;
			}
			if(fromCol != toCol && attacker == NONE){
				return ENPASSANT;
			}

		}else if (isKing(attacker)){
			int colDiff = Math.abs(fromCol - toCol);
			if(colDiff == 2){
				return CASTLEKING;
			}
			if(colDiff == 3){
				return CASTLEQUEEN;
			}
		}
		return NORMAL;
	}

	public int getRow(int[] location){
		return location[0];
	}
	public int getCol(int[] location){
		return location[1];
	}

	/*
	* returns null if false or 2d array ->[row, col, piece], essentially saying set all these squares as such
	*/
	public int[][] defineMove(int moveType, int[] fromSquare, int[]toSquare, 
			          int attacker, int attacked, int attackerColor){ 
		int[][] result;
		switch (moveType) {
		case NORMAL: result = defineNormalMove(fromSquare, toSquare, attacker, attacked, attackerColor);
			      break;
		case ENPASSANT: result = defineEnPassant(fromSquare, toSquare, attacker, attacked, attackerColor);
			         break;
	 	case TWOROWPAWN: result = defineTwoRowPawn(fromSquare, toSquare, attacker, attacked, attackerColor);
			      break;
	        case CASTLEQUEEN: result = defineCastleQueen(fromSquare, toSquare, attacker, attacked, attackerColor);
			      break;
	        case CASTLEKING: result = defineCastleKing(fromSquare, toSquare, attacker, attacked, attackerColor);
			      break;
		}
		return result;
	}
	public int[][] defineNormalMove(int[] fromSquare, int[] toSquare, 
			                int attacker, int attacked, int attackerColor){
		int fromRow = getRow(fromSquare);
	        int fromCol = getCol(fromSquare);
		int toRow = getRow(toSquare);
		int toCol = getCol(toSquare);
		int color = getColor(attacker);
		boolean isValid;
		if (isPawn(attacker)){ isValid = isValidPawnMove(color, fromSquare, toSquare);}
		if (isRook(attacker)){ isvalid = isValidRookMove(fromSquare, toSquare);}
		if (isKnight(attacker)){ isvalid = isValidKnightMove(fromSquare, toSquare);}
		if (isBishop(attacker)){ isvalid = isValidBishopMove(fromSquare, toSquare);}
		if (isQueen(attacker)){ isvalid = isValidQueenMove(fromSquare, toSquare);}
		if (isKing(attacker)){ isvalid = isValidKingMove(fromSquare, toSquare);}		
		int[][] result ={
				{ fromRow, fromCol, -1},
				{ toRow, toCol, attacker },
				};
		if(isValid)
			return result;
		return null;
	}

	public int[][] defineEnPassant(int[] fromSquare, int[] toSquare, int attacker, 
			               int attacked, int attackerColor){
		int fromRow = getRow(fromSquare);
	        int fromCol = getCol(fromSquare);
		int toRow = getRow(toSquare);
		int toCol = getCol(toSquare);		
		int[][] result ={
				{ fromRow, fromCol, -1},
				{ toRow, toCol, attacker },
				{ fromRow, toCol, -1 },      
				};
		int direction = getDirection(attackerColor);
		int[] lastMoveTo = {lastMove[2],lastMove[3]};
		int lastMoveToRow = getRow(lastMoveTo);
		int lastMoveToCol = getCol(lastMoveTo);


		if (lastMoveType == TWOROWPAWN     &&
	            (fromRow + direction) == toRow &&
	            Math.abs(toCol - fromCol) == 1 &&  // diagonal is row+direction and col+-1  
		    attacked == NONE              &&  // not true diagonal capture bc no piece there
		    sameSquare(lastMoveTo,new int[]{fromRow, toCol}) ){ //'behind' last move
			return result;
		}
		return null;
	}
        public int[][] defineTwoRowPawn(int[] fromSquare, int[] toSquare, 
			                int attacker, int attacked, int attackerColor){
		int fromRow = getRow(fromSquare);
	        int fromCol = getCol(fromSquare);
		int toRow = getRow(toSquare);
		int toCol = getCol(toSquare);
		int direction = piece.getDirection();
		int[][] result ={
				{ fromRow, fromCol, -1},
				{ toRow, toCol, attacker },
				};
		
		if(direction == 1 && fromRow != 1)
			return null;
		if(direction == -1 && fromRow != 6)
			return null;

		if(fromCol == toCol                 &&      // advance two rows, first move only
		   fromRow + 2 * direction == toRow &&
		   attacked == NONE                 &&
		   !piecesBetween(from,to)    &&
		   !scoreSheet.contains(from)){
			 return result;
		 }
		 return null;
	}
	public int otherColor(int color){
		if(color == WHITE)
			return BLACK;
		return WHITE;
	}
	public int[] west(int[] location){
		location[1] -= 1;
                return location;
	}
	public int[] east(int[] location){
		location[1] += 1;
                return location;
	}
	public boolean canCastleQeenSide(int color){
		if(color == WHITE)
			return whiteCanCastleQueenSide;
		return blackCanCastleQueenSide;
	}
	public boolean canCastleKingSide(int color){
		if(color == WHITE)
			return whiteCanCastleKingSide;
		return blackCanCastleKingSide;
	}
	public int[][] defineCastleQueen(int[] fromSquare, int[] toSquare, 
			                 int attacker, int attacked, int attackerColor){
		int fromRow = getRow(fromSquare);
	        int fromCol = getCol(fromSquare);
		int toRow = getRow(toSquare);
		int toCol = getCol(toSquare);
		// are either the from square or middle square attacked, to square checked before we get to this 
		if(isAttacked(fromSquare, otherColor(attackerColor)) ||
		   isAttacked(west(fromSquare), otherColor(attackerColor))){
			return null;		
		}
		// make sure pieces between rook and king
		if(piecesBetween(fromSquare, new int[]{fromRow,0}))
			return null;
		if(!canCastleQueenSide(attackerColor))
			return null;
		int[][] result ={
				{ fromRow, fromCol, -1},
				{ fromRow, fromCol-1, getPiece(fromRow,0)},
				{ toRow, toCol, attacker},
				{ fromRow, 0, -1}
				};
		return result;
	}
	public int[][] defineCastleKing(int[] fromSquare, int[] toSquare, 
			                int attacker, int attacked, int attackerColor){
		int fromRow = getRow(fromSquare);
	        int fromCol = getCol(fromSquare);
		int toRow = getRow(toSquare);
		int toCol = getCol(toSquare);	
                 // are either the from square or middle square attacked, to square checked before we get to this 
		if(isAttacked(fromSquare, otherColor(attackerColor)) ||
		   isAttacked(east(fromSquare), otherColor(attackerColor))){
			return null;		
		}
		// make sure pieces between rook and king
		if(piecesBetween(fromSquare, new int[]{fromRow,7}))
			return null;
		if(!canCastleKingSide(attackerColor))
			return null;	
		int[][] result ={
				{ fromRow, fromCol, -1},
				{ fromRow, fromCol+1, getPiece(fromRow,7)},
				{ toRow, toCol, attacker},
				{ fromRow, 7, -1}
				};
	}

	//returns true if all three pieces are together, on same column, row or diagonal and middle in middle 
	public boolean isBetween(int[] middle, int[] end1, int[] end2) {
		int end1Row = getRow(end1);
		int end1Col = getCol(end1);
		int end2Row = getRow(end2);
		int end2Col = getCol(end2);
		int middleRow = getRow(middle);
		int middleCol = getCol(middle);
		// if same column check row, if same row or diag check col, else false
		if (sameColumn(end1,middle,end2)){
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

	public boolean isAttacked(int[] location, int attackersColor){
		return attackersOfLocation(location,attackersColor).length > 0;
	}
	public boolean isAttacked(int row, int col,int attackersColor){
		int[] location = {row, col};
		return isAttacked(location,attackersColor);
	}

	public int[][] attackersOfLocation(int[] toLocation, int attackersColor){
		List<Integer> resultAsList = new ArrayList<Integer>();
		int[] result; 
		for (int r=0; r < 8; r++){
			for(int c=0; c<8; c++){
				int[] fromLocation = {r,c};
				int piece = getPiece(fromLocation);
				if (piece != NONE && getPieceColor(piece) == attackersColor){
					if (defineNormalMove(fromLocation, toSquare, attacker, 
		                            attacked, attackerColor) != null){
						resultAsList.add(r);
						resultAsList.add(c);
					}
				}
			}
		}
		int[][] result = new int[resultAsList.size()/2][2]; 
		int index = 0;
		for(int i=0; i < resultAsList.size(); i+=2){
			result[index] = new int[]{resultAsList.get(i), resultAsList.get(i+1)};
			index++;
		}
		return result;
	}


	public int[] findKing(int color){
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


	public boolean isKing(int piece){
		return piece == 11 || piece == 12;
	}
	public boolean isQueen(int piece){
		return piece == 9 || piece == 10;
	}
	public boolean isKnight(int piece){
		return piece == 7 || piece == 8;
	}
	public boolean isBishop(int piece){
		return piece == 5 || piece == 6;
	}
	public boolean isRook(int piece){
		return piece == 3 || piece == 4;
	}
	public boolean isPawn(int piece){
		return piece == 1 || piece == 2;
	}

	public boolean sameColumn(int[] sqr1, int[] sqr2){
		int fromCol = sqr1[1];
		int toCol = sqr2[1];
		return sameColumn(fromCol, toCol);
	}
	public boolean sameColumn(int[] sq1, int[] sqr2, int[] sqr3){
		return sameColumn(sqr1,sqr2) && 
		       sameColumn(sqr1,sqr3) && 
		       sameColumn(sqr2,sqr3);
	}
	public boolean sameRow(int[] sq1, int[] sqr2, int[] sqr3){
		return sameRow(sqr1,sqr2) && 
		       sameRow(sqr1,sqr3) && 
		       sameRow(sqr2,sqr3);
	}
	public boolean sameColumn(int fromCol, int toCol){
		return fromCol == toCol;
	}

	public boolean sameRow(int fromRow, int toRow){
		return fromRow == toRow;
	}

	public boolean sameRow(int[] sqr1, int[] sqr2){
		int fromRow = sqr1[0];
		int toRow = sqr2[0];
		return sameRow(fromRow, toRow);
	}
	
	public boolean sameSquare(int[] sq1, int[] sq2){
		return sameRow(sq1,sq2) && sameColumn(sq1,sq2);
	}
	
	public boolean sameDiagonal(int[] sq1, int[] sqr2){
		return sameForwardDiagonal(sq1,sq2) || sameBackDiagonal(sq1,sq2);
	}

	public boolean sameBackDiagonal(int[] sqr1, int[] sqr2){
		int fromRow = sqr1[0];
		int fromCol = sqr1[1];
		int toRow = sqr2[0];
		int toCol = sqr2[1];
		return sameBackDiagonal(fromRow, fromCol, toRow, toCol);
	}
	public boolean sameBackDiagonal(int[] sq1, int[] sqr2, int[] sqr3){
		return sameBackDiagonal(sqr1,sqr2) && 
		       sameBackDiagonal(sqr1,sqr3) && 
		       sameBackDiagonal(sqr2,sqr3);
	}
	public boolean sameForwardDiagonal(int[] sqr1, int[] sqr2){
		int fromRow = sqr1[0];
		int fromCol = sqr1[1];
		int toRow = sqr2[0];
		int toCol = sqr2[1];
		return sameForwardDiagonal(fromRow, fromCol, toRow, toCol);
	}
	public boolean sameForwardDiagonal(int[] sq1, int[] sqr2, int[] sqr3){
		return sameForwardDiagonal(sqr1,sqr2) && 
		       sameForwardDiagonal(sqr1,sqr3) && 
		       sameForwardDiagonal(sqr2,sqr3);
	}

	public boolean sameBackDiagonal(int fromRow, int fromCol, int toRow, int toCol){ // goes like this \
		int rowDiff = fromRow - toRow;
		int colDiff = fromCol - toCol;
		int absRowDiff = Math.abs(rowDiff);
		int absColDiff = Math.abs(colDiff);
		return rowDiff == colDiff;
		
	}
	public boolean sameForwardDiagonal(int fromRow, int fromCol, int toRow, int toCol){ // goes like this /
		int rowDiff = fromRow - toRow;
		int colDiff = fromCol - toCol;
		int absRowDiff = Math.abs(rowDiff);
		int absColDiff = Math.abs(colDiff);
		return (absRowDiff == absColDiff) && (rowDiff != colDiff); 
		 
	}

	public boolean isPinnedToColumn(int kingColor, int row, int col){
		int king = findKing(kingColor);
		int location = {row, col};
                if (!samColumn(king, location))
			return false;
		for(int attackerRow=0; attackerRow< 8; attackerRow++){
			int attacker = getPiece(attackerRow,col);
			if(attacker == NONE)
				continue;
			int attackerColor = getPieceColor(attacker);
      			if(attackerColor == kingColor)
				continue;
			if(isRook(attacker) || isQueen(attacker) && isBetween(location, king, attacker))
				return true;
      		}
		return false;
	}
	public boolean isPinnedToRow(int kingColor, int row, int col){
		int king = findKing(kingColor);
		int location = {row, col};
                if (!sameRow(king,location))
			return false;
		
		for(int attackerCol=0; attackerCol< 8; attackerCol++){
			int attacker = getPiece(row,attackerCol);
			if(attacker == NONE)
				continue;
			int attackerColor = getPieceColor(attacker);
      			if(attackerColor == kingColor)
				continue;
			if(isRook(attacker) || isQueen(attacker) && isBetween(location, king, attacker))
				return true;
      		}
		return false;
	}
	public boolean isPinnedToBackDiagonal(int kingColor, int row, int col){
		int king = findKing(kingColor);
		int location = {row, col};
		if(!sameBackDiagonal(king,location))
			return false;
		for(int r=0;r<8;r++){
			for(int c=0; c<8; c++){
				int[] attackerLocation = {r,c};
				int attacker = getPiece(attackerLocation);
				int attackerColor = getPieceColor(attacker);
				if(attacker == NONE)
					continue;
				if(attackerColor == kingColor)
					continue;
				if(!sameBackDiagonal(location,attackerLocation))
					continue;
				if(isBetween(location, king, attackerLocation))
					return true;
			}
		}
		return false;
	}
	public boolean isPinnedToForwardDiagonal(int kingColor, int row, int col){
		int king = findKing(kingColor);
		int[] location = new int[]{row, col};
		if(!sameForwardDiagonal(king,location))
			return false;
		for(int r=0;r<8;r++){
			for(int c=0; c<8; c++){
				int[] attackerLocation = {r,c};
				int attacker = getPiece(attackerLocation);
				int attackerColor = getPieceColor(attacker);
				if(attacker == NONE)
					continue;
				if(attackerColor == kingColor)
					continue;
				if(!sameForwardDiagonal(location,attackerLocation))
					continue;
				if(isBetween(location, king, attackerLocation))
					return true;
			}
		}
		return false;
	}

	public int getPieceColor(int piece){
		return piece % 2;
	}
	public boolean areSameColor(int piece1, int piece2){
		return getPieceColor(piece1) == getPieceColor(piece2);
	}

	public boolean isUnderAttack(int row, int col){}

	public boolean piecesBetween(int[] from, int[] to) {
			List<int[]> squares = squaresBetween(from, to);
			for (in[] square : squares) {
				if (getPiece(square) != NONE) {
					return true;
				}
			}
			return false;
		}
	public int getPiece(int r, int c){
		int[] loc = {r,c};
		return board[row][col];	
	}

	public int getPiece(int[] location){
		int row = location[0];
		int col = location[1];
		return getPiece(row,col);
	}

	public List<int[]> squaresBetween(int[] from, int[] to){
			int fromRow = from[0];
			int fromCol = from[1];
			int toRow = to[0];
			int toCol = to[1];
			return squaresBetween(fromRow,fromCol,toRow,toCol);	
	}

	public List<int[]> squaresBetween(int fromRow, int fromCol, int toRow, int toCol) {       
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

}
