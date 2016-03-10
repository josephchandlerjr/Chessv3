package com.chessv3;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * represents score sheet
 */
public class Score implements Serializable {
	List<ChessMove> moves; 
	List<String> notations;

	public Score()
	{
		moves = new ArrayList<ChessMove>();
		notations = new ArrayList<String>();
	}

	public void addMove(ChessMove move)
	{
		moves.add(move);
		notations.add(move.getNotation());

	}

	/**
	 * searches score sheet for a given square
	 * @param square the square we are looking for
	 * @return true if square is anywhere in socre sheet else false
	 */
	public boolean contains(Square square)
	{
		for (ChessMove move : moves)
		{
			if (move.getTo() == square || move.getFrom() == square){ return true;}
		}
		return false;
	}
	
	public void addResult(String winner){
		if(winner.equals("WHITE")){
			notations.add("1-0");
		}
		else if(winner.equals("BLACK")){
			notations.add("0-1");
		}
		else if(winner.equals("TIE")){
			notations.add("1/2-1/2");
		}
	}
	/**
	 * gets last move in score sheet
	 * @return last Move instance recorded or null if score sheet is empty
	 */
	public ChessMove lastMove()
	{
		if (moves.size() == 0) { return null;}
		return moves.get(moves.size()-1);
	}
	public String toString()
	{
		String res = "";
		for (String notation : notations)
		{
			res = res + notation + "\n";
		}
		return res;
	}
}

