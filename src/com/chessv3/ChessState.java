package com.chessv3;

import java.awt.*;


/**
 * class representing state of a chess game
 * that is passed from model to view
 */
public class ChessState{
	String toMove;
	boolean whiteToMove;
	boolean blackToMove;
	boolean whiteInCheck;
	boolean whiteHasWon;
	boolean blackInCheck;
	boolean blackHasWon;
	boolean promotion;
	String lastPlayerToMove;
	int rowOfPromotion;
	int colOfPromotion;

	public ChessState(){
		toMove = "WHITE";
	}
	
	/**
	 * returns json string
	 */
	public String toString(){
		String result = "{";
		result += "\"toMove\":" + "\"" + toMove + "\"" + ",";
		result += "\"whiteToMove\":" + whiteToMove+ ",";
		result += "\"blackToMove\":" + blackToMove+ ",";
		result += "\"whiteInCheck\":" + whiteInCheck+ ",";
		result += "\"whiteHasWon\":" + whiteHasWon+ ",";
		result += "\"blackInCheck\":" + blackInCheck+ ",";
		result += "\"blackHasWon\":" + blackHasWon+ ",";
		result += "\"promotion\":" + promotion+ ",";
		result += "\"lastPlayerToMove\":" + "\""+lastPlayerToMove+"\"" + ",";
		result += "\"rowOfPromotion\":" + rowOfPromotion+ ",";
		result += "\"colOfPromotion\":" + colOfPromotion;
		return result + "}";
	}
}

