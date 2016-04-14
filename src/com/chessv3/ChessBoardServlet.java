package com.chessv3;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ChessBoardServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		          throws IOException, ServletException {
			  response.setContentType("text/html");
			  ChessController controller = 
				  (ChessController) getServletContext().getAttribute("chess");
			  PrintWriter out = response.getWriter(); 
			  out.println("I see you want an update");
			  System.out.println("want update");
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		          throws IOException, ServletException {
			  response.setContentType("text/html");
			  ChessController controller = 
				  (ChessController) getServletContext().getAttribute("chess");
			  int fromRow = Integer.parseInt(request.getParameter("fromRow"));
			  int fromCol = Integer.parseInt(request.getParameter("fromCol"));
			  int toRow = Integer.parseInt(request.getParameter("toRow"));
			  int toCol = Integer.parseInt(request.getParameter("toCol"));
			  PrintWriter out = response.getWriter(); 
			  //out.println("I see you want to make a move");
			  System.out.println("making move");
	}
}


