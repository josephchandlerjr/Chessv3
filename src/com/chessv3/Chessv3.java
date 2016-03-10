package com.chessv3;

public class Chessv3{
	public static void main(String[] args){
		ChessModel model = new ChessModelImpl();
		ChessController controller = new ChessControllerImpl(null,null,model);
	}
}
