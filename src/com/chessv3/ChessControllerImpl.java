package com.chessv3;


/**
 * the C in MVC; strategy of the view
 * takes user input from a ChessView object and interprets it for the model
 * is an observer of the model in order to update view appropriately
 */
public class ChessControllerImpl implements ChessController{
	ChessModel model;
	ChessView view;

	public ChessControllerImpl(ChessModel model){
		this.model = model;
		model.registerObserver(this);
		this.view = new ChessView(this, model);
	}
	/**
	 * public API the view uses to trigger move requests on model
	 * @param fromRow row of piece being moved
	 * @param fromCol column of piece being moved
	 * @param toRow row piece being moved to 
	 * @param toCol column piece being moved to
	 */
        public void takeAction(int fromRow, int fromCol, int toRow, int toCol){
		model.takeAction(fromRow, fromCol, toRow, toCol);
	}
	/**
	 * public API the view uses to promote a pawn
	 * @param row row on which pawn lies
	 * @param col column on which pawn lies
	 * @param color of pawn
	 * @param piece representation of piece pawn promoted to
	 */
	public void promote(int row, int col, String color, String piece){
		if(piece != null){
			model.promote(row, col, color, piece);
		}
	}
	/**
	 * called by Observerables when change made
	 */
	public void update(){
		ChessState state = model.getState();
		if(state.promotion){
			int row = state.rowOfPromotion;
			int col = state.colOfPromotion;
			String color = state.lastPlayerToMove;
			String piece = view.getPromotion();
			promote(row,col,color,piece);
		}
		view.setLabels(state.whiteToMove,
			       state.whiteInCheck,
			       state.whiteHasWon,
			       state.blackToMove,
			       state.blackInCheck,
			       state.blackHasWon);
	}
	/**
	 * public API to create new game
	 */
	public void newGame(){
		model.newGame();
	}
	/**
	 * public API to end program
	 */
	public void exit(){
		System.exit(0);
	}
}
