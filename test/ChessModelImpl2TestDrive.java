package test;

import java.util.*;
import com.chessv3.ChessModelImpl2;

/**
 * tests of ChessModelImpl2
 */
public class ChessModelImpl2TestDrive{

	public void newGame(){
	}
	public void updateStateObject(){}//need to implement
	public void promote(){}//need to implement
	public void getBoard(){}//need to implement

	private void initializeBoard(){

	}

	public void getDirection(){
	}

	public void isValidPawnMove(){ 

	}

	private void isValidRookMove(){       

	}

	private void isValidKnightMove(){ 

	}

	private void isValidBishopMove(){ 

	}
	private void isValidQueenMove(){
	}
	private void isValidKingMove(){    

	}
	public void takeAction(){
	}
	public void setFlags(){

	}

	public void getMoveType(){

	}

	public void getRow(){
	}
	public void getCol(){
	}

	public void defineMove(int moveType, int[] fromSquare, int[]toSquare, 
	int attacker, int attacked, int attackerColor){ 
	}
	public void defineNormalMove(int[] fromSquare, int[] toSquare, 
	int attacker, int attacked, int attackerColor){
	}

	public void defineEnPassant(int[] fromSquare, int[] toSquare, int attacker, 
	int attacked, int attackerColor){

	}
	public void defineTwoRowPawn(int[] fromSquare, int[] toSquare, 
	int attacker, int attacked, int attackerColor){

	}
	public void otherColor(){
	}
	public void west(){
	}
	public void east(){
	}
	public void canCastleQueenSide(){
	}
	public void canCastleKingSide(){
	}
	public void defineCastleQueen(int[] fromSquare, int[] toSquare, 
	int attacker, int attacked, int attackerColor){

	}
	public void defineCastleKing(int[] fromSquare, int[] toSquare, 
	int attacker, int attacked, int attackerColor){

	}
	public void isBetween() {

	}

	public void isAttacked(){
	}

	public void attackersOfLocation(){

	}
	public void findKing(){

	}
	public void isKing(){
	}
	public void isQueen(){
	}
	public void isKnight(){
	}
	public void isBishop(){
	}
	public void isRook(){
	}
	public void isPawn(){
	}

	public void sameColumn(){
	}
	public void sameRow(){
	}

	public void sameSquare(){
	}

	public void sameDiagonal(){
	}


	public void sameBackDiagonal(){ // goes like this \

	}
	public void sameForwardDiagonal(){ // goes like this /

	}

	public void isPinnedToColumn(){
	}
	public void isPinnedToRow(){
	}
	public void isPinnedToBackDiagonal(){

	}
	public void isPinnedToForwardDiagonal(){

	}

	public void getPieceColor(){
	}
	public void areSameColor(){
	}

	public void piecesBetween() {
	}

	public void getPiece(){
	}

	public void squaresBetween(){
	}


}


