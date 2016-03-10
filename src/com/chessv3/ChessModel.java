package com.chessv3;

public interface ChessModel extends Observable{
	public void takeAction(int fromRow,int fromCol,int toRow, int toCol);
	public void promote(int row, int col, String color, String piece);
	public ChessState getState();
	public void newGame();
}
