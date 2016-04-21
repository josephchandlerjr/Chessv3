package com.chessv3;

import javax.servlet.http.*;

public class ChessSessionListener implements HttpSessionListener{
	
	public void sessionCreated(HttpSessionEvent event){
		HttpSession session = event.getSession();
		ChessControllerWeb controller = new ChessControllerWeb();
		controller.newGame();
		session.setAttribute("chess", controller);
	}

	public void sessionDestroyed(HttpSessionEvent event){
		HttpSession session = event.getSession();
		ChessControllerWeb controller = new ChessControllerWeb();
		controller.exit();
		//not necessary
	}
}

