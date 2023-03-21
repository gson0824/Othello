import java.util.*;


public class Reversi {
	static int [][] board;
	static int row;
	static int col;
	static int turn;
	static int player;
	static int computer;
	
	//main function
	public void play(int num, int type, String side) {
		Scanner scnr = new Scanner(System.in);
		Random rand = new Random();
		board = new int[num][num];
		row = num;
		col = num;
		turn = 0;
		//determine which color the player is
		if (side.equals("x")) {
			player = 2;
			computer = 1;
		}
		else if (side.equals("o")) {
			player = 1;
			computer = 2;
		}
		//create appropriate board
		if (num == 4) {
			create4Board(board);
		}
		else {
			create8Board(board);
		}
		while (!gameOver(board)) {
			int n1 = 0;
			int n2 = 0;
			if (turn%2 == 0) {
				System.out.println("Next to Play: x");
			}
			else if (turn%2 == 1) {
				System.out.println("Next to Play: o");
			}
			System.out.println();
			//player's turn
			if (turn%2 ==  player%2) {
				ArrayList<Node> moves = availablemoves(board);
				if (moves.isEmpty()) {
					System.out.println("No moves available");
					turn++;
					continue;
				}
				else {
					printboard();		
					System.out.println("Your move (Example: 'a1' is top left corner): ");
					String move;
					do {
						move = "";
						move = scnr.nextLine();
						if (move.matches("^[a-h][1-8]$")) {
							String temp = move.substring(0, 1);
							n1 = Integer.parseInt(move.substring(1, 2)) - 1;
							n2 = convert(temp);
						}
						else {
							System.out.println("Invalid input, try again:");
						}
						
					} while (!valid(n1, n2, moves));	
				}
				board[n1][n2] = player;
				flip(turn, n1, n2, board);
				turn++;
			}
			//computer's turn
			else {
				ArrayList<Node> moves = availablemoves(board);
				if (moves.isEmpty()) {
					System.out.println("No moves available");
					turn++;
					continue;
				}
				else {
					printboard();
					System.out.println("I'm picking a move...");
					//computer that plays randomly
					if (type == 1) {
						int r = rand.nextInt(moves.size());
						Node n = moves.get(r);
						n1 = n.X;
						n2 = n.Y;
					}
					//computer that uses MINIMAX
					else if (type == 2) {
						Node n = minimax(board, turn, moves);
						n1 = n.X;
						n2 = n.Y;
					}
					//computer that uses H-MINIMAX
					else if (type == 3) {
						Node n = hminimax(board, turn, moves);
						n1 = n.X;
						n2 = n.Y;
					}
				}
				board[n1][n2] = computer;
				flip(turn, n1, n2, board);
				turn++;
				System.out.println("Where the move was: " + convert(n2) + (n1+1));
			}
			System.out.println();
		}
		//end of game
		printboard();
		if (count(1, board) == count(2, board)) {
			System.out.println("Draw");
		}
		if (count(1, board) > count(2, board)) {
			System.out.println("Winner: White");
		}
		if (count(1, board) < count(2, board)) {
			System.out.println("Winner: Black");
		}
		System.out.println("White: " + count(1, board));
		System.out.println("Black: " + count(2, board));
		scnr.close();
	}

	//make 4x4 board
	public void create4Board(int[][] a) {
		for (int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				a[i][j] = 0;
	        }
		}
		a[1][1] = 1;
		a[1][2] = 2;
		a[2][1] = 2;
		a[2][2] = 1;
	}
	
	//make 8x8 board
	public void create8Board(int[][] a) {
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				a[i][j] = 0;
	        }
		}
		a[3][3] = 1;
		a[3][4] = 2;
		a[4][3] = 2;
		a[4][4] = 1;
	}
	
	//make a copy of current board
	public static int[][] copyboard(int[][]a) {
			int[][] temp = new int[a.length][a.length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[i].length; j++) {
					temp[i][j] = a[i][j];
				}
			}
			return temp;
		}
	
	//print the board
	public void printboard() {
			if (row == 4) {
				System.out.println("  a b c d  ");
				for (int i = 0; i < row; i++) {
					System.out.print(i + 1 + " ");
					for (int j = 0; j < col; j++) {
						if (board[i][j] == 0) {
							System.out.print("  ");
						}
						else if (board[i][j] == 1) {
							System.out.print("O ");
						}
						else {
							System.out.print("X ");
						}
					}
					System.out.println(i + 1);
				}
				System.out.println("  a b c d  ");
			}
			else {
				System.out.println("  a b c d e f g h ");
				for (int i = 0; i < row; i++) {
					System.out.print(i + 1 + " ");
					for (int j = 0; j < col; j++) {
						if (board[i][j] == 0) {
							System.out.print("  ");
						}
						else if (board[i][j] == 1) {
							System.out.print("O ");
						}
						else {
							System.out.print("X ");
						}
					}
					System.out.println(i + 1);
				}
				System.out.println("  a b c d e f g h ");
			}
		}

	//convert letter to number
	public static int convert(String s) {
		String temp = "abcdefgh";
		int ans = 0;
		for (int i = 0; i < temp.length(); i++) {
			if (s.charAt(0) == temp.charAt(i)) {
				ans = i;
			}
		}
		return ans;
	}
	
	//convert number to letter
	public static String convert(int x) {
		char[] temp = "abcdefgh".toCharArray();
		return Character.toString(temp[x]);
	}
	
	//count how many whites(1) and blacks(2)
	public static int count(int a, int[][] board) {
		int ans = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == a) {
					ans++;
				}
			}
		}
		return ans;
	}
	
	//check if game is over
	public static boolean gameOver(int[][] board) {
		boolean over = false;
		if (availablemoves(board).isEmpty()) {
			turn++;
			if (availablemoves(board).isEmpty()) {
				turn++;
				over = true;
			}
			else {
				turn++;
			}
		}
		return over;
	}
	
	//flip
	public static void flip(int turn, int x, int y, int[][] board) {
		flipCheck(turn, x, y, -1, 0, board);
		flipCheck(turn, x, y, -1, 1, board);
		flipCheck(turn, x, y, 0, 1, board);
		flipCheck(turn, x, y, 1, 1, board);
		flipCheck(turn, x, y, 1, 0, board);
		flipCheck(turn, x, y, 1, -1, board);
		flipCheck(turn, x, y, 0, -1, board);
		flipCheck(turn, x, y, -1, -1, board);
	}
	
	//extension of flip
	public static boolean flipCheck(int turn, int x, int y, int dirx, int diry, int[][] board) {
		int p1, p2;
		boolean flip = false;
		
		if (turn%2 ==  player%2) {
			p1 = player;
			p2 = computer;
		}
		else {
			p1 = computer;
			p2 = player;
		}
		
		if ((x + dirx < board.length) && (x + dirx >= 0) && (y + diry < board.length) && (y + diry >= 0)
				&& (board[x + dirx][y + diry] == p2)) {
			flip = flipCheck(turn, x + dirx, y + diry, dirx, diry, board);
		}
		else if ((x + dirx < board.length) && (x + dirx >= 0) && (y + diry < board.length) && (y + diry >= 0)
				&& (board[x + dirx][y + diry] == p1)) {
			return true;
		}
		
		if (flip) {
			board[x + dirx][y + diry] = p1;
			return true;
		}
		return false;
	}
	
	//check if a move is valid
	public static boolean valid(int x, int y, ArrayList<Node> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).X == x && array.get(i).Y == y)
				return true;
		}
		return false;
	}
	
	//check if move is legal
	public static boolean legal(int x, int y) {
		if (board[x][y] != 0) {
			return false;
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((i != 0 || j != 0) && flippable(x, y, i, j)) {
					return true;
				}
			}
		}
		return false;
	}
	
	//extension of legal()
	public static boolean flippable(int x, int y, int a, int b) {
		int p1, p2;
		boolean temp = false;
		
		if (turn%2 ==  player%2) {
			p1 = player;
			p2 = computer;
		}
		else {
			p1 = computer;
			p2 = player;
		}
		
		while ((x + a < board.length) && (x + a >= 0) && (y + b < board.length) &&
				(y + b >= 0) && board[x + a][y + b] == p2) {
			x += a;
			y += b;
			temp = true;
		}
		if (temp == false) {
			return false;
		}
		if ((x + a < board.length) && (x + a >= 0) && (y + b < board.length) &&
				(y + b >= 0) && board[x + a][y + b] == p1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//get all available moves as a Node and store them into a arraylist
	public static ArrayList<Node> availablemoves(int[][] board){
		ArrayList<Node> array = new ArrayList<Node>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (legal(i, j)) {
					Node n = new Node(i, j);
					array.add(n);
				}
			}
		}
		return array;
	}

	//utility for minimax
	public static int utility(int[][] board) {
		if (computer == 2) {
			return count(2, board) - count(1, board);
		}
		else {
			return count(1, board) - count(2, board);
		}
	}
	
	//minimax
	public static Node minimax(int[][] board, int turn, ArrayList<Node> moves) {
		int n1 = -1;
		int n2 = -1;
		int best = Integer.MIN_VALUE;
		Node n = new Node(n1, n2);
		for (int i = 0; i < moves.size(); i++) {
			int[][] temp = copyboard(board);
			temp[moves.get(i).X][moves.get(i).Y] = player;
			flip(turn, moves.get(i).X, moves.get(i).Y, temp);
			
			int x = minimaxvalue(temp, turn, 1);
			if (x > best) {
				best = x;
				n1 = moves.get(i).X;
				n2 = moves.get(i).Y;
				n.X = n1;
				n.Y = n2;
			}
		}
		return n;
	}
	
	//tree search for minimax
	public static int minimaxvalue(int[][] board, int turn, int depth) {
		//if is-terminal -> utility
		ArrayList<Node> moves = availablemoves(board);
		int best = Integer.MIN_VALUE;
		if (turn%2 == player%2) {
			best = Integer.MAX_VALUE;
		}
		if (depth == 5 || gameOver(board)) {
			return utility(board);
		}
		if (moves == null) {
			return minimaxvalue(board, turn, depth+1);
		}
		else {
			for (int i = 0; i < moves.size(); i++) {
				int[][] temp = copyboard(board);
				flip(turn, moves.get(i).X, moves.get(i).Y, temp);
				int val = minimaxvalue(temp, turn, depth+1);
				if (turn%2 == computer%2) {
					if (val > best) {
						best = val;
					}
				}
				else {
					if (val < best) {
						best = val;
					}
				}
			}
			return best;
		}
	}

	//heuristic for h-minimax
	public static int heuristic(int[][]board, int turn) {
			int h[][] = { {100, -20, 20, 20, 20, 20, -20, 100},
					{-20, -50, 10, 5, 5, 10, -50, -20},
					{20, 5, 10, 10, 10, 10, 5, 20},
					{20, 5, 10, 1, 1, 10, 5, 20},
					{20, 5, 10, 1, 1, 10, 5, 20},
					{20, 5, 10, 10, 10, 10, 5, 20},
					{-20, -50, 10, 5, 5, 10, -50, -20},
					{100, -20, 20, 20, 20, 20, -20, 100},};
			int x = 0;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if (board[i][j] == computer) {
						x += h[i][j];
					}
					else {
						x -= h[i][j];
					}
				}
			}
			return x;
		}
	
	//heuristic minimax with alpha-beta pruning and depth limit of 5
	//depth limit can be bigger but becomes slower
	public static Node hminimax(int[][] board, int turn, ArrayList<Node> moves) {
		int n1 = -1;
		int n2 = -1;
		int best = Integer.MIN_VALUE;
		Node n = new Node(n1, n2);
		
		for (int i = 0; i < moves.size(); i++) {
			int[][] temp = copyboard(board);
			temp[moves.get(i).X][moves.get(i).Y] = player;
			flip(turn, moves.get(i).X, moves.get(i).Y, temp);
			
			int x = hminimaxvalue(temp, turn, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
			if (x > best) {
				best = x;
				n1 = moves.get(i).X;
				n2 = moves.get(i).Y;
				n.X = n1;
				n.Y = n2;
			}
		}
		return n;
	}
	
	//tree search for heuristic minimax
	public static int hminimaxvalue(int[][] board, int turn, int depth, int alpha, int beta, boolean minmax) {
		//if is-cutoff -> heuristic
		ArrayList<Node> moves = availablemoves(board);
		if (depth == 5 || gameOver(board)) {
			return heuristic(board, turn);
		}
		else if (minmax) {
			alpha = Integer.MIN_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				if (alpha >= beta || moves.isEmpty()) {
					break;
				}
				int[][] temp = copyboard(board);
				flip(turn, moves.get(i).X, moves.get(i).Y, temp);
				int min = hminimaxvalue(temp, turn, depth+1, alpha, beta, false);
				if (alpha < min) {
					alpha = min;
				}
			}
			return alpha;	
		}
		else {
			beta = Integer.MAX_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				if (alpha >= beta || moves.isEmpty()) {
					break;
				}
				int[][] temp = copyboard(board);
				flip(turn, moves.get(i).X, moves.get(i).Y, temp);
				int max = hminimaxvalue(temp, turn, depth+1, alpha, beta, true);
				if (beta > max) {
					beta = max;
				}
			}
			return beta;
		}
	}
	
	
}