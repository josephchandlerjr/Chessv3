package com.chessv3;

import javax.servlet.*;

public class ChessSessionListener implements ServletContextListener{
	
	public void contextInitialized(ServletContextEvent event){
		ServletContext sc = event.getServletContext();
		ChessControllerWeb controller = new ChessControllerWeb();
		sc.setAttribute("chess", controller);
		System.out.println("online motherfucker");
	}

	public void contextDestroyed(ServletContextEvent event){
		//not necessary
	}
}

