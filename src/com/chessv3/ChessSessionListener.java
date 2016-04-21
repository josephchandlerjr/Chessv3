package com.chessv3;

import javax.servlet.http.*;

public class ChessSessionListener implements HttpSessionListener{
	
	public void sessionCreated(HttpSessionEvent event){
		HttpSession session = event.getSession();
		ChessControllerWeb controller = new ChessControllerWeb();
		controller.newGame();
		session.setAttribute("chess", controller);
		System.out.println("session created");
	}

	public void sessionDestroyed(HttpSessionEvent event){
		HttpSession session = event.getSession();
		ChessControllerWeb controller = (ChessControllerWeb) session.getAttribute("chess");
		controller.exit();
		System.out.println("session destroyed");
	}
}

