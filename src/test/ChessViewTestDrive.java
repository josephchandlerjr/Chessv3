

public class ChessViewTestDrive{
	public static void main(String[] args){
		ChessModel model = new ChessModelImpl();
		ChessController controller = new ChessControllerImpl(null,null,model);
		new ChessView(controller,model);
	}
}
