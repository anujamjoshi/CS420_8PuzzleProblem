import java.util.ArrayList;

/**
 * @class Board.java 
 * This class is the class that contains the state of the current state as an object Board. 
 * @author Anuja Joshi
 *
 */

public class Board {
	private int [][] board = new int[3][3];
	private int heuristic; 
	private boolean h1; 
	private Board parent; 
	private ArrayList<Board> children = new ArrayList<Board>(); 
	private Board nextState; 
	private int zeroRow; 
	private int zeroCol;
	private int gValue; 
	

	/**
	 * @return the board
	 */
	public int[][] getBoard() {
		return board;
	}
	/**
	 * @return zeroRow
	 */
	public int getZeroRow() {
		return zeroRow;
	}
	/**
	 * @return zeroCol
	 */
	public int getZeroCol() {
		return zeroCol;
	}

	/**
	 * @return the heuristic
	 */
	public int getHeuristic() {
		return heuristic;
	}
	/**
	 * @return the parent
	 */
	public Board getParent() {
		return parent;
	}
	/**
	 * @return the children
	 */
	public ArrayList<Board> getChildren() {
		return children;
	}
	/**
	 * @return the nextState that is supposed to be expanded 
	 */
	public Board getNextState() {
		return nextState;
	}
	/**
	 * @param path the nextState to set
	 */
	public void setNextState(ArrayList<Board> path) {
		Board next = getLowestCost(); 
		while (path.contains(next)){
			children.remove(next);
			next = getLowestCost(); 
		}
		this.nextState= next; 
	}
	/**
	 * takes in @param values as a one dimensional array 
	 * and populates the current Board instance with the values passed in
	 * also takes in a @param h1 as a boolean value to indicate if h1 or h2 is being used for the heruistic 
	 */
	public Board(int[]values, boolean h, Board p, int g){
		int i =0; 
		for(int row=0; row <3; row++){
			for (int col=0; col<3; col++){
				board[row][col]= values[i];
				if (values[i]==0){
					zeroRow=row; 
					zeroCol=(col); 
				}
				i++;
			}
		}
		if (h ==true){
			heuristic = this.h1();

		}
		else {
			heuristic = this.h2();
		}
		parent = p; 
		nextState=null; 
		gValue=g; 
		h1 = h;
	}
	public Board(int[][]values, boolean h, Board p, int g){

		for(int row=0; row <3; row++){
			for (int col=0; col<3; col++){
				board[row][col]= values[row][col];
				if (values[row][col]==0){
					zeroRow=row; 
					zeroCol=(col); 
				}
			}
		}
		if (h ==true){
			heuristic = this.h1();
		}
		else {
			heuristic = this.h2();
		}
		parent = p; 
		nextState=null; 
		gValue=g; 
		h1 = h;
	}
	/**
	 * @return boolean value that determines if the board is complete
	 */
	public boolean isFinal(){

		for(int row=0; row <3; row++){
			for (int col=0; col<3; col++){

				if (row*3 +col != board[row][col]){
					return false; 
				}
			}

		}
		return true;
	}
	/**
	 * @returns boolean if the board is solvable 
	 */
	public boolean isSolveable(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 3; i++) {
			// tiny change 1: proper dimensions
			for (int j = 0; j < 3; j++) { 
				// tiny change 2: actually store the values
				if (board[i][j]>0){
					list.add(board[i][j]); 
				}
			}
		}
		int numInversion = inversion(list);
		//System.out.println(numInversion);
		return (numInversion%2) == 0; 
	}
	private int inversion(ArrayList<Integer> list) {
		int count =0; 
		for (int i = 0; i < 7; i++){
			for (int j = i+1; j < 8; j++){
				if (list.get(i)<list.get(j)){
					count++;
				}
			}
		}
		return count;
	}
	/**
	 * @returns string value that is the string version of the current board
	 */
	public void printBoard(){
		String output = "";
		for(int row=0; row <3; row++){
			for (int col=0; col<3; col++){
				output+=board[row][col] +"\t";
			}
			output+="\n";
		}
		System.out.println( output);
	}



	/**
	 * @returns the number of misplaced tiles, the heuristic h1 
	 * Disregards the 0 value as it is the representation for the empty tile
	 */
	private int h1(){

		int count =0; 
		for(int row=0; row <3; row++){
			for (int col=0; col<3; col++){

				if (row*3 +col != board[row][col] && board[row][col]!=0){
					count++; 
				}
			}

		}
		return count;

	}
	/**
	 * @returns the sum of the distances of the tiles from their goal positions, the heuristic h2
	 * uses the formula of row*3 + column = value that's supposed to be there. 
	 * Disregards the 0 value as it is the representation for the empty tile
	 */
	private int h2(){
		int count =0; 
		for (int row = 0; row <3; row++){
			for (int col =0; col <3; col++){
				if (row*3 +col != board[row][col] && board[row][col]!=0){
					count+= calculateMoves(row, col,board[row][col] );
				}
			}
		}
		return count;
	}
	/**
	 * @param row
	 * @param col
	 * @param value is the value that we are trying to calculate the distance for 
	 * @returns an integer value that is the number of moves that are necessary for the move 
	 * used in {@code #h2()}
	 */
	private int calculateMoves(int row, int col, int value) {
		int colFinal = value %3; 
		int rowFinal = (value-colFinal)/3;
		return  Math.abs(colFinal-col+rowFinal-row);
	}
	/**
	 * uses the zeroRow and zeroCol to get the potential boards that can be expanded 
	 * after which uses the lowestCost method to find the next step
	 */
	public void addChildren(){
		//get the left child board 
		if (zeroRow -1>=0){
			//int[][]values, boolean h1, Board p, int g
			children.add(new Board(swap(zeroRow,zeroCol,zeroRow-1,zeroCol), this.h1, this, gValue+1));
		}
		if (zeroRow +1<=2){
			//int[][]values, boolean h1, Board p, int g
			children.add(new Board(swap(zeroRow,zeroCol,zeroRow+1,zeroCol), this.h1, this, gValue+1));
		}
		if (zeroCol -1>=0){
			//int[][]values, boolean h1, Board p, int g
			children.add(new Board(swap(zeroRow,zeroCol,zeroRow,zeroCol-1), this.h1, this, gValue+1));
		}
		if (zeroCol +1<=2){
			//int[][]values, boolean h1, Board p, int g
			children.add(new Board(swap(zeroRow,zeroCol,zeroRow,zeroCol+1), this.h1, this, gValue+1));
		}
		//this.nextState=this.getLowestCost();
	}
	private int[][] swap (int r1, int c1, int r2, int c2){
		int [][]tempBoard = new int[3][3];
		for(int i =0; i<3; i++){
			for (int j = 0; j <3; j++){
				tempBoard[i][j] = board[i][j];
			}
		}
		int temp = board[r1][c1];
		tempBoard[r1][c1] = board[r2][c2];
		tempBoard[r2][c2] = temp;
		//		String output = "";
		//		for(int row=0; row <3; row++){
		//			for (int col=0; col<3; col++){
		//				output+=tempBoard[row][col] +"\t";
		//			}
		//			output+="\n";
		//		}
		//	System.out.println("HI\n" + output);
		return tempBoard; 

	}
	private int calculateCost(){
		return gValue+this.getHeuristic(); 
	}
	private Board getLowestCost(){
		if (children.size()==0){
			return null; 
		}
		int minCost = children.get(0).calculateCost(); 
		Board min = children.get(0);
		for(Board b:children){
			if (b.calculateCost()<minCost){
				minCost = b.calculateCost(); 
				min=b;
			}
		}
		return min;
	}
	


}
