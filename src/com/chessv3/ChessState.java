package com.chessv3;

import java.awt.*;


/**
 * class representing state of a chess game
 * that is passed from model to view
 */
public class ChessState{
	String[][] board;//just color and piece ex: BP or WQ
	String toMove;
	boolean whiteInCheck;
	boolean blackInCheck;
	boolean whiteHasWon;
	boolean blackHasWon;
	boolean promotion;
	String lastPlayerToMove;
	int[] rowColOfPromotion = new int[2];

	public ChessState(){
		this.board = new String[8][8];
		toMove = "WHITE";
	}
	
}

