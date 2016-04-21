package com.chessv3;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ChessBoardServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		          throws IOException, ServletException {
			  response.setContentType("application/json");
			  ChessControllerWeb controller = 
				  (ChessControllerWeb) request.getSession().getAttribute("chess");
			  String[][] board = controller.getBoard();
			  String status = controller.getStatus();
			  PrintWriter out = response.getWriter(); 
			  String boardRepr = "";
			  for (int i=0; i < board.length; i++){
				  for(int j=0; j < board[0].length; j++){
					  String p = board[i][j];
					  boardRepr += p + " ";
				  }
			  }
			  boardRepr = boardRepr.substring(0,boardRepr.length()-1);
			  out.println(status.substring(0,status.length()-1) +
					     ", \"boardRepr\":" +"\""+ boardRepr +"\"" +"}");
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		          throws IOException, ServletException {
			  response.setContentType("text/plain");
			  ChessController controller = 
				  (ChessController) request.getSession().getAttribute("chess");
			  int fromRow = Integer.parseInt(request.getParameter("fromRow"));
			  int fromCol = Integer.parseInt(request.getParameter("fromCol"));
			  int toRow = Integer.parseInt(request.getParameter("toRow"));
			  int toCol = Integer.parseInt(request.getParameter("toCol"));
			  controller.takeAction(fromRow,fromCol,toRow,toCol);
			  PrintWriter out = response.getWriter(); 
			  out.println(" ");
	}
}


