package com.chessv3;

import java.io.*;
import java.net.*;

/**
 * represents a chess piece. This is superclass to all specific pieces: Pawn, Bishop, etc
 */
public abstract class ChessPiece{
	private String color; 

	public static final ChessPiece WHITEPAWN = new Pawn("WHITE");
	public static final ChessPiece BLACKPAWN = new Pawn("BLACK");
	public static final ChessPiece WHITEROOK= new Rook("WHITE");
	public static final ChessPiece BLACKROOK= new Rook("BLACK");
	public static final ChessPiece WHITEKNIGHT = new Knight("WHITE");
	public static final ChessPiece BLACKKNIGHT = new Knight("BLACK");
	public static final ChessPiece WHITEBISHOP = new Bishop("WHITE");
	public static final ChessPiece BLACKBISHOP = new Bishop("BLACK");
	public static final ChessPiece WHITEQUEEN = new Queen("WHITE");
	public static final ChessPiece BLACKQUEEN = new Queen("BLACK");
	public static final ChessPiece WHITEKING = new King("WHITE");
	public static final ChessPiece BLACKKING = new King("BLACK");

	
        /** 
	 * constructor
	 * @param color "WHITE" or "BLACK" 
	 */
	public ChessPiece(String color){
		this.color = color;
	}
	public int getDirection(){ 
		return 0; 
	}
	public static ChessPiece pieceFromString(String pieceID){
		String color = "WHITE";
		if(pieceID.substring(0,1).equals("B")){
			color = "BLACK";
		}
		if(pieceID.length() > 1){
			switch(pieceID.substring(1,2)){
				case("R"): return new Rook(color); 
				case("B"): return new Bishop(color); 
				case("N"): return new Knight(color);
				case("Q"): return new Queen(color);
				case("K"): return new King(color);
			}
		}
		else{
			return new Pawn(color);
		}
		assert 1==2 : "never should be here";
		return null;

	}

	/**
	 * gets color of piece
	 * @return the color of piece, "WHITE" or "BLACK"
	 */
	public String getColor(){
		return color;
	}

	public static boolean isPawn(ChessPiece piece){
		return piece instanceof Pawn;
	}
	public static boolean isRook(ChessPiece piece){
		return piece instanceof Rook;
	}
	public static boolean isKnight(ChessPiece piece){
		return piece instanceof Knight;
	}
	public static boolean isBishop(ChessPiece piece){
		return piece instanceof Bishop;
	}
	public static boolean isQueen(ChessPiece piece){
		return piece instanceof Queen;
	}
	public static boolean isKing(ChessPiece piece){
		return piece instanceof King;
	}
}


/** 
 * represents a pawn chess piece
*/
class Pawn extends ChessPiece{
	private int direction;

	/**
	 * constructor
	 * @param color either string "BLACK" or "WHITE"
	 */
	public Pawn(String color){
		super(color);
		if (color.equals("BLACK")){
			direction = 1;
		}
		else{
			direction = -1;
		}
	}
	public String toString(){
		return "";
	}
	/** 
	 * gets direction pawn moves
	 * @return 1 if pawn moves toward higher numbered rows or -1 if moving opposite direction
	 */
	public int getDirection(){
		return direction;
	}
	
}


/** 
 * represents a rook chess piece
 */
class Rook extends ChessPiece{
	/**
	 * constructor
	 * @param color either string "BLACK" or "WHITE"
	 */
	public Rook(String color){
		super(color);
	}
	public String toString(){
		return "R";
	}
}


/**
 * represents the knight chess piece
 */
class Knight extends ChessPiece{
	/**
	 * constructor
	 * @param color either string "BLACK" or "WHITE"
	 */
	public Knight(String color){
		super(color);
	}
	public String toString(){
		return "N";
	}
	
}

/**
 * represents a bishop chess piece
 */
class Bishop extends ChessPiece{
	/**
	 * constructor
	 * @param color either string "BLACK" or "WHITE"
	 */
	public Bishop(String color){
		super(color);
	}
	public String toString(){
		return "B";
	}
}


/**
 * represents a Queen chess piece
 */
class Queen extends ChessPiece{
	/**
	 * constructor
	 * @param color either string "BLACK" or "WHITE"
	 */
	public Queen(String color){
		super(color);
	}
	public String toString(){
		return "Q";
	}
}


/**
 * represents the king chess piece
 */
class King extends ChessPiece{
	/**
	 * constructor
	 * @param color either string "BLACK" or "WHITE"
	 */
	public King(String color){
		super(color);
	}
	public String toString(){
		return "K";
	}
	
}

