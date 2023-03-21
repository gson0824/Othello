import java.util.*;


public class Main {
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		System.out.println("Reversi by GyeongJun Son");
		System.out.println("Choose your game(1 or 2):");
		System.out.println("1. Small 4x4 Reversi");
		System.out.println("2. Standard 8x8 Reversi");
		System.out.print("Your choice? ");
		int num = scnr.nextInt();
		System.out.println("Choose your opponent:");
		System.out.println("1. An agent that plays randomly");
		System.out.println("2. An agent that uses MINIMAX");
		System.out.println("3. An agent that uses H-MINIMAX (Only for 8x8 board)");
		System.out.println("Your choice? ");
		int type = scnr.nextInt();
		System.out.print("Do you want to play DARK (x) or LIGHT (o)? ");
		String side = scnr.next();
		System.out.println();
		Reversi reversi = new Reversi();
		if (num == 1) {
			reversi.play(4, type, side);
		}
		else {
			reversi.play(8, type, side);
		}
		scnr.close();
	}
}
