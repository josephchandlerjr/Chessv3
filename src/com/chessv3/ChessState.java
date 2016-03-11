package com.chessv3;

import java.awt.*;


/**
 * class representing state of a chess game
 * that is passed from model to view
 */
public class ChessState{
	String toMove;
	boolean whiteInCheck;
	boolean blackInCheck;
	boolean whiteHasWon;
	boolean blackHasWon;
	boolean promotion;
	String lastPlayerToMove;
	int rowOfPromotion;
	int colOfPromotion;

	public ChessState(){
		toMove = "WHITE";
	}
	
}

