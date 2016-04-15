package com.chessv3;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Arrays;

public class ChessBoardServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		          throws IOException, ServletException {
			  response.setContentType("text/plain");
			  ChessControllerWeb controller = 
				  (ChessControllerWeb) getServletContext().getAttribute("chess");
			  String[][] board = controller.getBoard();
			  PrintWriter out = response.getWriter(); 
			  String result = "";
			  for (int i=0; i < board.length; i++){
				  for(int j=0; j < board[0].length; j++){
					  String p = board[i][j];
					  result += p + " ";
				  }
			  }
			  out.println(result.substring(0,result.length()-1));
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
	}
}


