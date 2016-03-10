

public class ChessModelImplTestDrive{

	public static void main(String[] args){
		ChessModelImpl model = new ChessModelImpl();
		ChessState state = model.getState();
		for(String[] row : state.board){
			for(String s : row){
				System.out.printf("%2s",s);
			}
			System.out.println();
		}
	}
}
