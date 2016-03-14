package com.chessv3;

import java.util.*;

/**
 * Model in our MVC
 * validates and performs moves on the board
 * holds a Score object, a scoresheet of moves
 */
public class ChessModelImpl implements ChessModel{
	private List<Observer> observers = new ArrayList<Observer>();
	private ChessState state;
	private Square[][] board;
	private String WHITE,BLACK;
	private String player,opponent;
	private Square initWK,initWKR,initWQR;
	private Square initBK,initBKR,initBQR;
	private Score scoreSheet;
	private boolean blackCheck,whiteCheck;
	private boolean blackCheckmate,whiteCheckmate; //true if king of said color in checkmate

	public ChessModelImpl(){
		WHITE = "WHITE";
		BLACK = "BLACK";
		board = new Square[8][8];
	}
	/**
	 * API used it initialize a new game
	 */
	public void newGame(){
		scoreSheet = new Score();
		board = new Square[8][8];
		initializeBoard();
		initBQR = board[0][0];
		initBKR = board[0][7];
		initWQR = board[7][0];
		initWKR = board[7][7];
		initBK =  board[0][4];
		initWK =  board[7][4];
		state = new ChessState();
		player = WHITE;
		opponent = BLACK;
		updateStateObject(new ChessMove());
		notifyObservers();
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
	 * @return string array representing board pieces
	 */
	public String[][] getBoard(){
		String[][] result = new String[8][8];
		BoardIterator iter = new BoardIterator(board);
		while(iter.hasNext()){
			Square s = iter.next();
			int r = s.getRow();
			int c = s.getCol();
			String pieceColor = s.getPieceColor();
			if(pieceColor.equals("")){
				result[r][c] = null;
			}
			else{
				result[r][c] = pieceColor.substring(0,1) + 
						    s.getPiece().toString();
			}
		}
		return result;
	}
	/**
	 * API promotes a pawn
	 * @param row row where square of promoted pawn lies
	 * @param col column where square of promoted pawn lies
	 * @param color color of piece to be promoted
	 * @param piece string representation of piece pawn is to be promoted to
	 */ 
	public void promote(int row, int col, String color, String piece){
		ChessPiece promoteTo = ChessPiece.pieceFromString(color.substring(0,1)+piece);
		setPiece(row, col, promoteTo);
		updateStateObject(new ChessMove());
		notifyObservers();
	}
	private void updateStateObject(ChessMove move){
		if(move.isPromotion()){
			state.promotion = true;
			state.rowOfPromotion = move.getTo().getRow();
			state.colOfPromotion = move.getTo().getCol();
		}
		else
			state.promotion = false;
		state.lastPlayerToMove = move.getColor();
		state.blackInCheck = blackCheck; //true if black king in check
		state.whiteInCheck = whiteCheck;
		state.whiteHasWon  = blackCheckmate; //true if black king in checkmate
		state.blackHasWon = whiteCheckmate;
	}

	/**
	 * API
	 * @return a state object
	 */
	public ChessState getState(){
		return state;
	}

	private void addResultToScoreSheet(String winner){
		scoreSheet.addResult(winner);
	}

	private boolean isInCheck(String color){	
		if(color.equals(BLACK)){
			return blackCheck;
		}
		return whiteCheck;
	}
	/**
	 * given a square, returns string representing
	 * piece+file+rank 
	 */
	private String getNotationFromSquare(Square s) {
		String file = s.getFile();
		String rank = s.getRank();
		String pieceID = "";
		ChessPiece piece = s.getPiece();
		if(piece != null){
			pieceID = s.getPiece().toString();
		}
		return pieceID + file + rank;
	}
	/**
	 * API exposed to other objects used to make a chess move
	 */
	public void takeAction(int fromRow, int fromCol, int toRow, int toCol){
		Square fromSquare = getSquare(fromRow,fromCol);
		Square toSquare = getSquare(toRow,toCol);
	        String pieceColor  = fromSquare.getPieceColor();
		String fromNotation = getNotationFromSquare(fromSquare);
		String toNotation = getNotationFromSquare(toSquare);

		if(!fromSquare.isOccupied()) //must be moving a piece
			return;
	        if(!pieceColor.equals(player))  //must be correct color
			return; 
		String notation = fromNotation+toNotation;
		ChessMove move = new ChessMove(notation, pieceColor, fromSquare, toSquare);
 
		if (takeAction(move)){
			updateStateObject(move);
			notifyObservers();
		}
	}
	private boolean takeAction(ChessMove move){
		String color = move.getColor();
		if(move.isCastle()){
			boolean validMove = castle(color, move.getCastleSide());
			if(!validMove)
				return false;
		}
		else if(move.isEnPassant()){
			boolean validMove = isValidEnPassant(move);
                        if(!validMove){return false;}
			moveEnPassant(move);
			boolean invalid = kingInCheck(color);
			if(invalid){
				unMoveEnPassant(move);
				return false;
			};
		}
		else if (move.isTwoRowPawnMove()){
			boolean validMove = isValidTwoRowPawnMove(move);
                        if(!validMove){return false;}
			movePawnTwoRows(move);
			boolean invalid = kingInCheck(color);
			if(invalid){
				unMovePawnTwoRows(move);
				return false;
			};
		}
		else{
			boolean validMove = isValidMove(move);
                        if(!validMove){return false;}
			movePiece(move);
			boolean invalid = kingInCheck(color);
			if(invalid){
				unMovePiece(move);
				return false;
			};
		}

		scoreSheet.addMove(move);	
		updateCheckStatus();

		if(colorInCheckmate(BLACK)){blackCheckmate = true;}
		if(colorInCheckmate(WHITE)){whiteCheckmate = true;}

		//move completed so swap player and opponent colors
		String temp = player;
		player = opponent;
		opponent = temp;

		return true;
	}
	private void updateCheckStatus(){
		if(kingInCheck(BLACK)){blackCheck = true;}
		else                    {blackCheck = false;}
		if(kingInCheck(WHITE)){whiteCheck = true;}
		else                    {whiteCheck = false;}
	}

	/**moves a piece, used for 'normal' moves not en passant, two row pawn moves, or castling
	 * @param move move object
	 */
	private void movePiece(ChessMove move){
		Square from = move.getFrom();
		Square to = move.getTo();

		ChessPiece piece = from.getPiece();
		to.setPiece(piece);
		from.setPiece(null);
	}
	/** 
	 * undos a change made by movePiece method
	 * @param move Move object describing original move
	 */
	private void unMovePiece(ChessMove move){
		Square from = move.getFrom();
		Square to = move.getTo();
		from.setPiece(from.getPreviousPiece());
		to.setPiece(to.getPreviousPiece());

	}

	/**
	 * undos changes made by movePawnTwoRows method
	 * @param move Move object describing original move
	 */ 
	private void unMovePawnTwoRows(ChessMove move){
		unMovePiece(move);
	}

	/** makes two row pawn move
	 * @param move Move object describing move
	 */
	private void movePawnTwoRows(ChessMove move){
		Square from = move.getFrom();
		Square to = move.getTo();

		ChessPiece piece = from.getPiece();
		setPiece(to.getRow(),to.getCol(), piece);
		from.setPiece(null);
	}
	/** 
	 * undos a change made by moveEnPassant method
	 */
	private void unMoveEnPassant(ChessMove move){
		Square from = move.getFrom();
		Square to = move.getTo();

		unMovePiece(move);
		Square captured = getSquare(from.getRow(),to.getCol());
		captured.setPiece(captured.getPreviousPiece());
	}

	/** makes en passant
	 */
	private void moveEnPassant(ChessMove move){
		Square from = move.getFrom();
		Square to = move.getTo();
		
		ChessPiece piece = from.getPiece();
		setPiece(to.getRow(),to.getCol(), piece);
		from.setPiece(null);
		Square squareToCapture = getSquare(from.getRow(),to.getCol());
		squareToCapture.setPiece(null);
	}
	/**
	 * determines if a two row pawn move is a valid move
	 */
	private boolean isValidTwoRowPawnMove(ChessMove move){
		if(!ChessPiece.isPawn(move.getFrom().getPiece()))
			return false;
		Square from = move.getFrom();
		Square to = move.getTo();

		ChessPiece piece = from.getPiece();
		int direction = piece.getDirection();
		int fromRow = from.getRow();
		int fromCol = from.getCol();
		int toRow = to.getRow();
		int toCol = to.getCol();

		if(fromCol == toCol                 &&      // advance two rows, first move only
		   fromRow + 2 * direction == toRow &&
		   !to.isOccupied()                 &&
		   !to.isOccupied()                 &&
		   !piecesBetween(from,to)    &&
		   !scoreSheet.contains(from)){
			 return true;
		 }
		return false;
	}

	/**
	 * determines if en passant is a valid move
	 */
	private boolean isValidEnPassant(ChessMove move){
		// last move must be two square pawn move
		// last move must have ended next to from square
		// to move move be diagonal and 'behind' last move to square

		if(!ChessPiece.isPawn(move.getFrom().getPiece()))
			return false;

		Square from = move.getFrom();
		Square to = move.getTo();

		 int fromRow = from.getRow();
		 int fromCol = from.getCol();
		 int toRow = to.getRow();
		 int toCol = to.getCol();

		 Pawn piece = (Pawn)(from.getPiece());
		 String myColor = piece.getColor();
		 int direction = piece.getDirection();

		 ChessMove lastMove = scoreSheet.lastMove();

		 if (lastMove != null               &&
		     lastMove.isTwoRowPawnMove()    &&  //only after pawn jumps past another pawn
	             fromRow + direction == toRow   &&
	             Math.abs(toCol - fromCol) == 1 &&  // diagonal is row+direction and col+-1  
		     !to.isOccupied()               &&  // not true diagonal capture bc no piece there
		     lastMove.getTo() == getSquare(to.getRow() - direction, to.getCol())){ //'behind' last move
			 return true;
		 }
		 return false;

	}
	/**
	 * determines if a move is a valid move
	 */
	 private boolean isValidMove(ChessMove move){
		 Square from = move.getFrom();
		 Square to = move.getTo();

		 if (from == to) 
			 return false;
		 if (!from.isOccupied()) // can't move a piece that isn't there
			 return false;
		 if (isOccupiedByPieceOfSameColor(to, from.getPiece().getColor()))
			 return false;
		 ChessPiece p = from.getPiece();
		 if (ChessPiece.isPawn(p)){ return isValidPawnMove(from, to);}
		 if (ChessPiece.isRook(p)){ return isValidRookMove(from, to);}
		 if (ChessPiece.isKnight(p)){ return isValidKnightMove(from, to);}
		 if (ChessPiece.isBishop(p)){ return isValidBishopMove(from, to);}
		 if (ChessPiece.isQueen(p)){ return isValidQueenMove(from, to);}
		 if (ChessPiece.isKing(p)){ return isValidKingMove(from, to);}
		 return false;
	 }
	/**
	 * determines if a pawn move is valid (not used for en passant or two-row moves
	 */
	 private boolean isValidPawnMove(Square from, Square to){ 
		 if(!ChessPiece.isPawn(from.getPiece())){return false;}
		 int fromRow = from.getRow();
		 int fromCol = from.getCol();
		 int toRow = to.getRow();
		 int toCol = to.getCol();

		 ChessPiece piece= from.getPiece();
		 String myColor = piece.getColor();
		 int direction = piece.getDirection();

		 if (fromCol == toCol         &&
	         fromRow + direction == toRow &&
		 !to.isOccupied()){// advance one row
			 return true;
		 }
		 else if(fromCol-1 == toCol           &&
		         fromRow + direction == toRow &&
			 isOccupiedByOpponent(to, myColor)){ // capture diagnonal
			 return true;
		 }
		 else if(fromCol+1 == toCol           &&
		         fromRow + direction == toRow &&
			 isOccupiedByOpponent(to, myColor)){ // capture diagnonal 
				 return true;
			 }
		 return false;
	 }
	/**
	 * determines if a rook move is a valid move
	 */
	 private boolean isValidRookMove(Square from, Square to){       

		 ChessPiece piece= from.getPiece();
		 String myColor = piece.getColor();
		 if (!areOnSameColumn(from,to) && !areOnSameRow(from,to)){ return false;}
		  
		 if (isOccupiedByPieceOfSameColor(to, myColor)) { return false;}
		 if (piecesBetween(from, to)){ return false;}
		 return true;

	 }
	/**
	 * determines if a Knight is a valid move
	 */
         private boolean isValidKnightMove(Square from, Square to){ 
		 int fromRow = from.getRow();
		 int fromCol = from.getCol();
		 int toRow = to.getRow();
		 int toCol = to.getCol();

		 int rowDiff = Math.abs(toRow - fromRow);
		 int colDiff = Math.abs(toCol - fromCol);

		 if (rowDiff == 2 && colDiff == 1) { return true;}
		 if (rowDiff == 1 && colDiff == 2) { return true;}
		 
		 return false;
		 
	 }
	/**
	 * determines if a Bishop is a valid move
	 */
	 private boolean isValidBishopMove(Square from, Square to){ 
		 ChessPiece piece= from.getPiece();
		 String myColor = piece.getColor();
		 if (!areOnSameDiagonal(from,to)) { return false;}
		  
		 if (isOccupiedByPieceOfSameColor(to, myColor)) { return false;}
		 if (piecesBetween(from, to)){ return false;}
		 return true;
	 }

	/**
	 * determines if a Queen is a valid move
	 */
	 private boolean isValidQueenMove(Square from, Square to){
		 return isValidRookMove(from,to) || isValidBishopMove(from,to);
	 }

	/**
	 * determines if a King is a valid move
	 */
	 private boolean isValidKingMove(Square from, Square to){    

		 int fromRow = from.getRow();
		 int fromCol = from.getCol();
		 int toRow = to.getRow();
		 int toCol = to.getCol();

		 int colDiff = toCol - fromCol;
		 int rowDiff = toRow - fromRow;

		 return Math.abs(colDiff) <= 1 && Math.abs(rowDiff) <= 1; 
	 }

	/**
	 * returns location of King of a given color
	 */
	 private Square findKing(String color){
		 Square result = null;
		 BoardIterator iter = new BoardIterator(board);
		 while(iter.hasNext()){
			Square sqr = iter.next();
			if (sqr.isOccupied()){
				 ChessPiece p = sqr.getPiece();
				 if(ChessPiece.isKing(p) && p.getColor().equals(color)){
					 result = sqr;
				 }	
			}
		 }
		 return result;
	 }
	/**
	 * determines if a King is in check
	 */
	 private boolean kingInCheck(String color){
		 Square kingLoc = findKing(color);
		 for (Square opponentSquare : getSquaresByPieceColor(otherColor(color))){
			 ChessPiece piece = opponentSquare.getPiece();
			 boolean moveValid =isValidMove(new ChessMove(color,opponentSquare, kingLoc));  
			 if(moveValid)
				 return true;
		 }
		 return false;
	 }

	 /**
	  * finds if given color is in checkmate
	  */
	 private boolean colorInCheckmate(String color){
		 if (!kingInCheck(color))
			 return false;
		 List<Square> squares = getSquaresByPieceColor(color);
		 for (Square from : squares){
			 BoardIterator iter = new BoardIterator(board);
			 while(iter.hasNext()){
				 Square to = iter.next();
				 ChessMove move = new ChessMove(color, from, to);
				 if(isValidMove(move)){
					 movePiece(move);
					 boolean inCheck = kingInCheck(color);
					 unMovePiece(move);
					 if(!inCheck)
						 return false;
				 }
				 else if(isValidEnPassant(move)){
					 moveEnPassant(move);
					 boolean inCheck  = kingInCheck(color);
					 unMoveEnPassant(move);
					 if(!inCheck)
						 return false;
				 }
				 else if(isValidTwoRowPawnMove(move)){
					 movePawnTwoRows(move);
					 boolean inCheck = kingInCheck(color);
					 unMovePawnTwoRows(move);
					 if(!inCheck)
						 return false;
				 }
			 }
		 }
		 return true;
	 }

	 /**
	  * determines if king and rook in original position and no pieces between them
	  */
	 private boolean checkCastlePosition(Square initK, Square initR, String side){
		 int kingShift = 8;
		 int rookShift = 8; 
		 if (side.equals("KING")){
			 kingShift = 2;
			 rookShift = -2;
		 }
		 else if (side.equals("QUEEN")){
			 kingShift = -2;
			 rookShift = 3;
		 }
		 //rook and king in original positions?
		 if (scoreSheet.contains(initR) || scoreSheet.contains(initK))
			 return false;
		 //no pieces between
		 if (piecesBetween(initR,initK)) 
			 return false;
		 return true;
	 } 
	 /**
	  * castles
	  */
	 private boolean castle(String color, String side){
		// to make compiler happy
		Square initK = null;
		Square initR = null;
	        Square newKingSqr = null;
		Square newRookSqr = null;
		Square intermediarySquare = null; //the square king must pass through

		if (color.equals(WHITE)){
			initK = initWK;
	 		if (side.equals("KING")){
				initR = initWKR;
				newKingSqr = initK.east().east();
				newRookSqr = initR.west().west();
				intermediarySquare = initK.east(); 
			}
			else if(side.equals("QUEEN")){
				initR = initWQR;
				newKingSqr = initK.west().west();
				newRookSqr = initR.east().east().east();
				intermediarySquare = initK.west(); 
			}
		}
		else if (color.equals(BLACK)){
			initK = initBK;
			if (side.equals("KING")){
				initR = initBKR;
				newKingSqr = initK.east().east();
				newRookSqr = initR.west().west();
				intermediarySquare = initK.east(); 
			}
			else if(side.equals("QUEEN")){
				initR = initBQR;
				newKingSqr = initK.west().west();
				newRookSqr = initR.east().east().east();
				intermediarySquare = initK.west(); 
			}
		}
		boolean correctAlignment = checkCastlePosition(initK, initR, side);
		if(!correctAlignment)
			return false;
		//before castling let's see if king will pass through check on way to destination	
		ChessMove oneSqr = new ChessMove(color, initK, intermediarySquare);
		movePiece(oneSqr);
		boolean invalid = kingInCheck(color);
		unMovePiece(oneSqr);
		if (invalid)
			return false;
		//now lets do full castle
		ChessPiece king = initK.getPiece();
		ChessPiece rook = initR.getPiece();
		initK.setPiece(null);
		initR.setPiece(null);
		newKingSqr.setPiece(king);
		newRookSqr.setPiece(rook);
		//now before we are done, see if king in check
		invalid = kingInCheck(color);
		if(invalid){
			unCastle(initK,newKingSqr,initR,newRookSqr);
			return false;
	        }
		return true;
	 }
	 /** 
	  * reverses a castle
	  */
	 private void unCastle(Square initK, Square newKingSqr,Square initR,Square newRookSqr){
		 newKingSqr.setPiece(null);
		 newRookSqr.setPiece(null);
		 initK.setPiece(initK.getPreviousPiece());
		 initR.setPiece(initR.getPreviousPiece());
	 }

	 /**
	  * sets instance variables that essentially link adjacent squares for convenience elsewhere
	  */
	 private void setDirectionalVar(){
		BoardIterator iter = new BoardIterator(board);
		while(iter.hasNext()){
			Square sqr = iter.next();
			int row = sqr.getRow();
			int col = sqr.getCol(); 
			if(!isOffBoard(row, col+1)){sqr.setEAST(getSquare(row,col+1));}
			if(!isOffBoard(row, col-1)){sqr.setWEST(getSquare(row,col-1));}
		}
	}	

	/**
	 * creates all ChessPiece objects and inserts them in board array
	 */
	private void initializeBoard(){
		for (int r=0; r < 8; r++){
			for (int c=0; c < 8; c++){
				board[r][c] = new Square(r, c);
			}
		}//end for loop
		//place pawns
		for (int i=0; i < 8; i++){
			board[1][i].setPiece(ChessPiece.BLACKPAWN);
			board[6][i].setPiece(ChessPiece.WHITEPAWN);
		}
		//rooks
		board[0][0].setPiece(ChessPiece.BLACKROOK);
		board[0][7].setPiece(ChessPiece.BLACKROOK);
		board[7][0].setPiece(ChessPiece.WHITEROOK);
		board[7][7].setPiece(ChessPiece.WHITEROOK);
		//knights
		board[0][1].setPiece(ChessPiece.BLACKKNIGHT);
		board[0][6].setPiece(ChessPiece.BLACKKNIGHT);
		board[7][1].setPiece(ChessPiece.WHITEKNIGHT);
		board[7][6].setPiece(ChessPiece.WHITEKNIGHT);
		//Bishops
		board[0][2].setPiece(ChessPiece.BLACKBISHOP);
		board[0][5].setPiece(ChessPiece.BLACKBISHOP);
		board[7][2].setPiece(ChessPiece.WHITEBISHOP);
		board[7][5].setPiece(ChessPiece.WHITEBISHOP);
		//Queens
		board[0][3].setPiece(ChessPiece.BLACKQUEEN);
		board[7][3].setPiece(ChessPiece.WHITEQUEEN);
		//Kings
		board[0][4].setPiece(ChessPiece.BLACKKING);
		board[7][4].setPiece(ChessPiece.WHITEKING);	

		setDirectionalVar();
	}
	
	/**
	 * gets all squares with a piece of given color on it
	 */
	private List<Square> getSquaresByPieceColor(String color) {
		List<Square> result = new ArrayList<Square>();
		BoardIterator iter = new BoardIterator(board);
		while(iter.hasNext()){
			Square sqr = iter.next();
			if(sqr.isOccupied() && sqr.getPieceColor().equals(color)){
				result.add(sqr);
			}
		}
		return result;
	}

	/**
	 * gets a Square from the board
	 */
	private Square getSquare(int row, int col) {
		return board[row][col];
	}
        /**
	 * puts a piece on square
	 */
	private void setPiece(int row, int col, ChessPiece piece) {
		Square square = board[row][col];
		square.setPiece(piece);
	}

	 /**
	  * checks if a row,column combination is off of the 8x8 board
	  */
	 private boolean isOffBoard(int row, int col) {
		 return row > 7 || row < 0 || col > 7 || col < 0;
	 }

	/**
	 * "WHITE" returns "BLACK"
	 * "BLACK" returns "WHITE"
	 */
	private String otherColor(String myColor) {
		if (myColor.equals(WHITE)){ return BLACK;}
		return WHITE;
	} 

	/**
	 * checks if a square is occupied by a piece of the other(color) of given color
	 */
	private boolean isOccupiedByOpponent(Square s, String myColor) {
		String opponentsColor = otherColor(myColor);
		return s.isOccupied() && s.getPiece().getColor().equals(opponentsColor);
	}

	/**
	 * checks if a square is occupied by a piece of the given color
	 */
	private boolean isOccupiedByPieceOfSameColor(Square s, String myColor) {
		boolean result;
		if (!s.isOccupied())  
			return false;
		else 
			result =s.getPiece().getColor().equals(myColor); 
		return result;
	}	

	/**
	 * takes two pieces on same row, column, or diagonal and tells if there are pieces between them
	 */
	private boolean piecesBetween(Square from, Square to) {
		List<Square> squares = squaresBetween(from, to);
		for (Square square : squares) {
			if (square.isOccupied()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns squares that lie between two squares on same row, column or diagonal
	 */
	private List<Square> squaresBetween(Square from, Square to) {       

		List<Square> result = new ArrayList<Square>();
		int fromRow = from.getRow();
		int fromCol = from.getCol();
		int toRow = to.getRow();
		int toCol = to.getCol();
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
			result.add(getSquare(row,col));
			col = col + colStep;
			row = row + rowStep;
		}
		return result;
	}
	private boolean areOnSameColumn(Square A, Square B) {       
		int fromCol = A.getCol();
		int toCol = B.getCol();
		return fromCol == toCol;
	}
	private boolean areOnSameRow(Square A, Square B) {       
		int fromRow = A.getRow();
		int toRow = B.getRow();
		return fromRow == toRow;
	}
	private boolean areOnSameDiagonal(Square A, Square B) {
		int fromCol = A.getCol();
		int toCol = B.getCol();
		int fromRow = A.getRow();
		int toRow = B.getRow();

		int colDiff = toCol - fromCol;
		int rowDiff = toRow - fromRow;

		return Math.abs(colDiff) == Math.abs(rowDiff);
	}
}


