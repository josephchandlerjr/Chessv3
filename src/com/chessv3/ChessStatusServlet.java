package com.chessv3;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ChessStatusServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		          throws IOException, ServletException {
			  response.setContentType("application/json");
			  ChessControllerWeb controller = 
				  (ChessControllerWeb) getServletContext().getAttribute("chess");
			  PrintWriter out = response.getWriter(); 
			  out.println(controller.getStatus());
	}

}


