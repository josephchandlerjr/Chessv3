package com.chessv3;

import java.io.*;


public class ChessControllerImpl implements ChessController{
	ObjectInputStream whitePlayer;
	ObjectInputStream blackPlayer;
	ChessModel model;
	ChessView view;

	public ChessControllerImpl(ObjectInputStream whitePlayer,
			       ObjectInputStream blackPlayer,
			       ChessModel model){
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.model = model;
		model.registerObserver(this);
		this.view = new ChessView(this, model);
	}

        public void takeAction(int fromRow, int fromCol, int toRow, int toCol){
		model.takeAction(fromRow, fromCol, toRow, toCol);
	}
	public void promote(int row, int col, String color, String piece){
		if(piece != null){
			model.promote(row, col, color, piece);
		}
	}
	public void update(){
		ChessState state = model.getState();
		if(state.promotion){
			int row = state.rowOfPromotion;
			int col = state.colOfPromotion;
			String color = state.lastPlayerToMove;
			String piece = view.getPromotion();
			promote(row,col,color,piece);
		}
		//ask view to set status of things
	}
	public void newGame(){
		model.newGame();
	}
	public void exit(){
		System.exit(0);
	}
}
