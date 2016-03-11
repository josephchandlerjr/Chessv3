package com.chessv3;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.*;

/**
 * the View in MVC
 * for user actions or actions which impact the model this
 * class will defer to the ChessControlle object
 */
public class ChessView implements Observer{
	ChessController controller;
	ChessModel model;

	Color blackSquareColor = new Color(102,51,0);
	Color whiteSquareColor = Color.white;

	JFrame frame;
	JPanel mainPanel;
	SquarePanel[][] squares;

	public ChessView(ChessController controller, ChessModel model){
		this.controller = controller;
		this.model = model;
		model.registerObserver(this);
		build();
	}
	public void update(){
		updateBoard(model.getBoard());
	}
	public void updateBoard(String[][] modelBoard){
		for(int r=0; r<8; r++){
			for(int c=0; c<8; c++){
				String pieceID = modelBoard[r][c];
				if(pieceID != null){
					Image image = findResourceByPieceID(pieceID);
					squares[r][c].setImage(image);
				}
				else{
					Image image = null;
					squares[r][c].setImage(image);
				}
			}
		}
	}

	public String getPromotion(){
		Object[] possibleValues = {"Queen","Knight"};
		Object selectedValue = JOptionPane.showInputDialog(null,
				                                   "Choose one",
								   "Input",
								   JOptionPane.INFORMATION_MESSAGE,
								   null,
								   possibleValues,
								   possibleValues[0]);
		String result = (String)(selectedValue);
		if(result != null){
			if(result.equals("Queen"))
				return "Q";
			if(result.equals("Knight"))
				return "N";
		}
		return null;
	}

	public Image findResourceByPieceID(String pieceID){
		String color = "White";
		String piece = "Pawn";
		if(pieceID.substring(0,1).equals("B")){
			color = "Black";
		}
		if(pieceID.length() > 1){
			switch(pieceID.substring(1,2)){
				case("R"): piece = "Rook";
					   break;
				case("B"): piece = "Bishop";
					   break;
				case("N"): piece = "Knight";
					   break;
				case("Q"): piece = "Queen";
					   break;
				case("K"): piece = "King";
					   break;
			}
		}

		String fileName =  ".\\etc\\" + color + piece + ".png";
		Image image = new ImageIcon(fileName).getImage(); 
		return image;
	}

        public void takeAction(int fromRow, int fromCol, int toRow, int toCol){
		controller.takeAction(fromRow, fromCol, toRow, toCol);
	}

	public void build(){
		frame = new JFrame("CHESS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildMenuBar();

		GridLayout grid = new GridLayout(8,8);
		grid.setVgap(1);
		grid.setHgap(1);
		mainPanel = new JPanel(grid);
		mainPanel.setPreferredSize(new Dimension(70*8,70*8));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		initialize();
	
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);

		frame.pack();
		frame.setVisible(true);
	}
	public void buildMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu file = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
					                      Event.CTRL_MASK));
		newGame.addActionListener(new ActionListener(){
					  	public void actionPerformed(ActionEvent e){
							controller.newGame();
						}});
		file.add(newGame);
		menuBar.add(file);

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					                   Event.CTRL_MASK));
		exit.addActionListener(new ActionListener(){
					   	public void actionPerformed(ActionEvent e){
							controller.exit();
						}});
		file.add(exit);
	}
	public Color otherSquareColor(Color c){
		if(c == blackSquareColor){
			return whiteSquareColor;
		}
		return blackSquareColor;
	}
	public void initialize(){
		BoardListener listener = new BoardListener();
		squares = new SquarePanel[8][8];
		Color squareColor = whiteSquareColor;
		for (int row = 0; row < 8; row++){
			squareColor = otherSquareColor(squareColor);
			for (int col = 0; col < 8; col++){
				SquarePanel p = new SquarePanel(row, col);
				p.setColor(squareColor);
				p.addMouseListener(listener);
				mainPanel.add(p);
				squares[row][col] = p;
				squareColor = otherSquareColor(squareColor);
			}
		}
	}

	class BoardListener implements MouseListener {
		MouseEvent lastEntered;
		MouseEvent lastPressed;

		public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e){
			lastEntered = e;
		}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){
			lastPressed = e;
		}
		public void mouseReleased(MouseEvent e){
			SquarePanel from = (SquarePanel)(lastPressed.getSource());
			SquarePanel to = (SquarePanel)(lastEntered.getSource());
			takeAction(from.row,from.col,to.row,to.col);
		}  
	}//end inner class BoardListener

}

class SquarePanel extends JPanel {
	int row;
	int col;
	Color color;
	Image image;

	public SquarePanel(int row, int col){
		this.row = row;
		this.col = col;
	}
	public void setColor(Color color){
		this.color = color;
	}
	public void setImage(Image image){
		this.image = image;
		this.repaint();
	}
	public void paintComponent(Graphics g) {
		g.setColor(color);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		if (image != null) {
			g.drawImage(image,10,10,this);
		}
	}
}



