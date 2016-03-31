package com.chessv3;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** 
 * represents a square on the chess board
 */
public class Square{
	private final int ROW;
	private final int COL;
	private final String rank;
	private final String file;
	private ChessPiece piece;
	private ChessPiece previousPiece;
	private Square EAST;
	private Square WEST;


	/**
	 * create new square
	 * @param row row square resides in
	 * @param col col square resides in 
	 */
	public Square(int row, int col){
		this.ROW = row;
		this.COL = col;
		this.rank = rowToRank(row);
		this.file = columnToFile(col);
		this.piece = null;
	}
	/**
	 * static method that converts column number to file notation
	 * @param column column number between 0-7
	 * @return letter of corresponding file in range a-h
	 */
	public static String columnToFile(int column)
	{
		String file = "abcdefgh".substring(column,column+1);
		return file;
	}
	/**
	 * static method that converts row number to rank notation
	 * @param row row number between 0-7
	 * @return number of corresponding file in range 1-8
	 */
	public static String rowToRank(int row)
	{
		String rank = "87654321".substring(row ,row+1);
		return rank;
	}
	//helper methods when castling
	public void setEAST(Square e){ EAST = e;}
	public void setWEST(Square w){ WEST = w;}
	public Square east(){
	       return EAST;
	}	       
	public Square west(){
		return WEST;
	}
	
	public ChessPiece getPreviousPiece()
	{
		return previousPiece;
	}
	public String getRank(){
		return rank;
	}
	public String getFile(){
		return file;
	}
	/**
	 * places piece on this square
	 * @param piece ChessPiece object to put on this square
	 */
	public void setPiece(ChessPiece piece){
		this.previousPiece = this.piece;
		this.piece = piece;
	}
	/**
	 * gets piece on this square
	 * @return piece on this square or null if there is none
	 */
	public ChessPiece getPiece(){
		return piece;
	}
	/**
	 * gets the string representation of piece on this square
	 * @return string representation of piece
	 */
	public String toString()
	{
		if (piece == null)
		{ return null;}
		return piece.toString();
	}

	/**
	 * gets row
	 * @return the row number of piece
	 */
	public int getRow(){
		return ROW;
	}

	/**
	 * gets column of piece
	 * @return the column number of piece
	 */
	public int getCol(){
		return COL;
	}

	/**
	 * gets color of piece sitting on square
	 * @return color of piece on square
	 */
	public String getPieceColor(){
		if (piece == null){
			return "";
		}
		return piece.getColor();
	}

	/**
	 * checks if given position is occupied by a piece 
	 * @param piece ChessPiece reference at given location on board
	 * @return true if parameter given is not a pointer to null
	 */
	public boolean isOccupied(){
		return piece != null;
	}
}

