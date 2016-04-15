package com.chessv3;


/**
 * the C in MVC; strategy of the view
 * takes user input from a ChessView object and interprets it for the model
 * is an observer of the model in order to update view appropriately
 */
public class ChessControllerWeb implements ChessController{
	ChessModel model;

	public ChessControllerWeb(){
		this.model = new ChessModelImpl();
		model.registerObserver(this);
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
		if(piece != null)
			model.promote(row, col, color, piece);
	}
	public void update(){};
	/**
	 * called by Observerables when change made
	 */
	public String[][] getBoard(){
		ChessState state = model.getState();
		if(state.promotion){
			int row = state.rowOfPromotion;
			int col = state.colOfPromotion;
			String color = state.lastPlayerToMove;
			//do call promote in javascript some how
			//and call promote
		}
		//set labels somehow 
		return model.getBoard();
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

