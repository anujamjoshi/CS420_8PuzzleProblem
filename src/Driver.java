import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class Driver {

	/**
	 * @param args
	 */
	static ArrayList<Board> path = new ArrayList<Board>(); 
	static int depth =0 ;
	static HashMap<Integer, Long> averageTimeh1 = new HashMap<Integer,Long>();
	static ArrayList <Long> timeh1 = new ArrayList<Long>(); 
	static HashMap<Integer, Long> averageTimeh2 = new HashMap<Integer,Long>();
	static ArrayList <Long> timeh2 = new ArrayList<Long>(); 
	static int step; 

	public static int[] randomInput(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		Collections.shuffle(list);
		//System.out.println(list);
		int [] a = new int[9];
		for(int i =0 ; i < 9; i++){
			a[i]=list.get(i);
		}
		return a;
	}
	public static int[] userInput(){
		int[] a = new int[9];
		Scanner sc = new Scanner (System.in);
		System.out.println("enter in a specific 8-puzzle configuration entered through the standard input, which contains the configuration for only one puzzle");
		System.out.println("Where 0 represents the empty tile.");
		System.out.println("please enter the top row in the format of:\na b c\t where a b c are the numbers");
		String [] input = sc.nextLine().split(" ");
		while (input.length!=3){
			System.out.println("Incorrect input! please enter the top row in the format of:\na b c\t where a b c are the numbers");
			input = sc.nextLine().split(" ");

		}
		int index =0; 
		while(index <3){
			a[index] = Integer.valueOf(input[index]);
			index ++; 
		}
		System.out.println("please enter the second row in the format of:\nd e f\t where a b c are the numbers");
		input = sc.nextLine().split(" ");
		while (input.length!=3){
			System.out.println("Incorrect input! please enter the second row in the format of:\nd e f\t where a b c are the numbers");
			input = sc.nextLine().split(" ");

		}
		index =0; 
		while(index <3){
			a[index + 3] = Integer.valueOf(input[index]);
			index ++; 
		}
		System.out.println("please enter the third row in the format of:\ng h i\t where a b c are the numbers");
		input = sc.nextLine().split(" ");
		while (input.length!=3){
			System.out.println("Incorrect input! please enter the third row in the format of:\ng h i\t where a b c are the numbers");
			input = sc.nextLine().split(" ");

		}
		index =0; 
		while(index <3){
			a[index + 6] = Integer.valueOf(input[index]);
			index ++; 
		}
		sc.close();
		return a;
	}
	public static void fileInput() throws FileNotFoundException{

		File c = new File("input.txt");
		Scanner scan = new Scanner(c);
		while(scan.hasNext()){
			String[] s = scan.nextLine().split(" ");

			if (s[0].equals("Depth")){
				if (depth!=0){

					Long totalTime =(long) 0; 
					for(long l: timeh1){
						totalTime+=l;
					}

					//System.out.println("Total time " + totalTime );
					averageTimeh1.put(depth, totalTime/timeh1.size());
					totalTime =(long) 0; 
					for(long l: timeh2){
						totalTime+=l;
					}

					//System.out.println("Total time " + totalTime );
					averageTimeh2.put(depth, totalTime/timeh2.size());
				}
				depth = Integer.parseInt(s[1]);


			}
			else {
				long startTime =  System.currentTimeMillis();
				int [] a = new int[9];
				for(int index = 0; index < 9; index++){
					a[index] = Integer.valueOf(s[0].charAt(index)+"");


				}
				runh1(a);
				timeh1.add(System.currentTimeMillis()-startTime);
				startTime =System.currentTimeMillis();
				runh2(a);
				timeh2.add(System.currentTimeMillis()-startTime);

			}
		}
		if (depth!=0){

			Long totalTime =(long) 0; 
			for(long l: timeh1){
				totalTime+=l;
			}
			averageTimeh1.put(depth, totalTime/timeh1.size());
			totalTime =(long) 0; 
			for(long l: timeh2){
				totalTime+=l;
			}
			averageTimeh2.put(depth, totalTime/timeh2.size());
		}
		scan.close();
	}
	public static void runh1(int[] input){
		Board b = new Board(input, true, null, 0);
		run( b);
	}

	public static void runh2(int[] input){
		Board b = new Board(input, false, null, 0);
		run ( b);
	}
	private static void run(Board b) {
		path= new ArrayList <Board>();
		step =0; 
		b.printBoard();
		while(!b.isFinal() && b.isSolveable()){
			//b.printBoard(); 
			b.addChildren(); 
			path.add(b);
			b.setNextState(path);
			
			b = b.getNextState(); 
			step += b.getChildren().size();
		}
	}
	public static void main(String[] args) throws FileNotFoundException {

		Scanner sc = new Scanner(System.in);
		System.out.println("How would you like to set up the puzzle? \nPlease select:\n1 for random Input \n2 to enter your own input \n3 for file input of 100 cases ranging from depth 2 to 20\n4 to exit");
		int input =Integer.parseInt(sc.next());
		if (input ==1){
			int [] values = randomInput();
			runh1(values);
			runh2(values);
		}
		else if (input == 2){
			int [] values = userInput();
			runh1(values);
			runh2(values);

		}
		else if (input == 3){
			fileInput();
		}
		else if (input == 4){
			System.out.println("Thank you for playing ");
		}
		
	}

}
