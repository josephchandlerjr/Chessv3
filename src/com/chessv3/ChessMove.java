package com.chessv3;

import java.io.*;

/** 
 * represents a move for the score sheet
 * a move is an array of Move objects
 */
public class ChessMove{
	private Square from;
	private Square to;
	private Square enPassantCapture;
	private boolean castle;
	private boolean enPassant;
	private boolean twoRowPawnMove;
	private boolean promotion;
	private String castleSide = "";
	private String color;
	private String notation;

	public ChessMove(){}

	public ChessMove(String color, Square from, Square to){
		this.from = from;
		this.to = to;
		this.color = color;
	}

	public ChessMove(String notation, String color, Square from, Square to){
		this.notation = notation;
		this.from = from;
		this.to = to;
		this.color = color;
		ChessPiece piece = from.getPiece();
		
		if(piece instanceof King){ //if it's a king and move is at least 2 columns
			if(colDifference(from,to) < -1){
				this.castleSide = "KING";
				this.castle = true;
			}
			else if(colDifference(from,to) > 1){
				this.castleSide = "QUEEN";
				this.castle = true;
			}
		}
		else if(piece instanceof Pawn){
			if(absRowDifference(from,to) == 2){
				this.twoRowPawnMove = true;
			}
			else if(isDiagonal(from, to) && to.getPiece() == null){
				this.enPassant = true;
			}
			else if(to.getRow()==7 || to.getRow()==0){
				this.promotion = true;
			}	
		}
	}

	//getters
	public boolean isCastle(){return castle;}
	public boolean isEnPassant(){return enPassant;}
	public boolean isTwoRowPawnMove(){return twoRowPawnMove;}
	public boolean isPromotion(){return promotion;}
	public Square getFrom(){return from;}
	public Square getTo(){return to;}
	public Square getEnPassantCapture(){return enPassantCapture;}
	public String getColor(){return color;}
	public String getCastleSide(){return castleSide;}
	public String getNotation(){return notation;}

	//setters
	
	private int absRowDifference(Square A, Square B){
		int rowA = A.getRow();
		int rowB = B.getRow();
		return Math.abs(rowA - rowB);
	}
	private int absColDifference(Square A, Square B){
		int colA = A.getCol();
		int colB = B.getCol();
		return Math.abs(colA - colB);
	}
	private int colDifference(Square A, Square B){
		int colA = A.getCol();
		int colB = B.getCol();
		return colA - colB;
	}
	private boolean isDiagonal(Square A, Square B){
		int r = absRowDifference(A,B);
		int c = absColDifference(A,B);
		return r != 0 && r==c;
	}
}
