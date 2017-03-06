/*
 *Made by James Haller
 *Last modified: 01.06.2013
 **/

import java.util.Scanner;
import java.util.ArrayList;

public class Game
{
	public static void main (String[] args)
	{
			// Variables
		Board[] board = new Board[2];
			board[0] = new Board();
			board[1] = new Board();
		Scanner scan = new Scanner(System.in);
		String strIn;
		boolean AIenabled = false;

			// AI?
		strIn = prompt("AI enabled? (y/[n])");
		if (isYes(strIn))
			AIenabled = true;
		System.out.println();

			//Instructions
		strIn = prompt("Would you like instructions? (y/[n])");

		if (isYes(strIn))
		{
			System.out.println(	"\n\nYou can exit the game at anytime by pressing ctrl + C.\n" +
								"The board ranges from a-j along the x-axis and from 1-10 along the y-axis.\n" + board[0] + "\n" +
								"To enter a coordinate type the letter then the number (eg: a5).\n" +
								"To place a ship type the coordinate then 'h' for horizontal or 'v' for vertical\n\tthe ship will start on the side closest to a1\n" +
								"To attack a coordinate simply type the coordinate you wish to attack.\n" +
								"A yes or no question will require any word starting with a 'y' for \"yes\" \nand will otherwise default to \"no\"\n" +
								"Note: It is recommended that you setup your own board with ships totally seperated and away from edges");
			System.out.println("Press ENTER when you're done.");
			scan.nextLine();
		}
		clear();

			// Game play
		if (AIenabled)
			onePlayer(board);
		else
			twoPlayer(board);

	}

	private static void userSetup (Board board)
	{
		System.out.println(board);

		Scanner scan = new Scanner(System.in);
		System.out.println("Input ship placement in the following format: xy v/h (eg: a10h)...");

		for (int n = 0; n < 5; n++)
		{
			Ship sh = board.getFromFleet(n);

			String in = prompt(sh + ":\t");

			if (isValidInputSetup(in, board))
			{
				Space sp = new Space( Integer.parseInt (in.substring(1, in.length()-1 )) - 1 , (int)(in.charAt(0)) - 97 );

				int dir = 0;
				if (in.charAt(in.length()-1) == 'v')
					dir = Board.VERTICAL;
				if (in.charAt(in.length()-1) == 'h')
					dir = Board.HORIZONTAL;

				if (board.addShip(n, sp, dir))
				{
					System.out.println("\n" + board);
				}
				else
				{
					System.out.println("Invalid\n");
					n--;
				}
			}
			else n--;
		}	// End loop

		board.hideAll();
		prompt("Press ENTER when done...");
		clear();
	}
	private static void randomSetup (Board board)
	{
		for (int n = 0; n < 5; n++)
		{
			Ship sh = board.getFromFleet(n);

			Space sp = new Space( (int)(Math.random() * 10) , (int)(Math.random() * 10));

			int dir = (int)(Math.random() * 2);

			if (!board.addShip(n, sp, dir))
				n--;
		}

		board.hideAll();
	}

	private static boolean isValidInput (String strIn, Board board)
	{
			// Length (2-3)
		if (strIn.length() < 2 || strIn.length() > 3)
		{
			System.out.println("Invalid\n");
			return false;
		}
			// Format
				// char? (a-j)
		else if ( (int)strIn.charAt(0) < 97 || (int)strIn.charAt(0) > 107 )
		{
			System.out.println("Invalid" + ((int)strIn.charAt(0)) + "\n");
			return false;
		}
				// 0 < int < 10?
		else if ( !(strIn.substring(1).equals("1") || strIn.substring(1).equals("2") ||
					strIn.substring(1).equals("3") || strIn.substring(1).equals("4") ||
					strIn.substring(1).equals("5") || strIn.substring(1).equals("6") ||
					strIn.substring(1).equals("7") || strIn.substring(1).equals("8") ||
					strIn.substring(1).equals("9") || strIn.substring(1).equals("10") ) )
		{
			System.out.println("Invalid\n");
			return false;
		}
				// valid Space?
		else
		{
			int yCor = Integer.parseInt( strIn.substring(1) ) -1;
			int xCor = (int)strIn.charAt(0) -97;

			if (!board.isValid(yCor, xCor))
			{
				System.out.println("Invalid\n");
				return false;
			}
		}

			// true
		return true;
	}
	private static boolean isValidInputSetup (String strIn, Board board)
	{
		String strSeg = "";

			// Length (3-4)
		if (strIn.length() < 3 || strIn.length() > 4)
		{
			System.out.println("Invalid\n");
			return false;
		}
			// Format
				// char? (a-z)
		else if ( (int)strIn.charAt(0) < 97 || (int)strIn.charAt(0) > 107 )
		{
			System.out.println("Invalid\n");
			return false;
		}
				//  v or h
		else if (strIn.charAt(strIn.length() - 1) != 'v' && strIn.charAt(strIn.length() - 1) != 'h')
		{
			System.out.println("Invalid\n");
			return false;
		}

		else if ( !(strIn.charAt(strIn.length() - 2) != 'v' && strIn.charAt(strIn.length() - 2) != 'h'))
		{
			System.out.println("Invalid\n");
			return false;
		}
				// 0 < "int" < 10?
		else if ( !(strIn.substring(1,2).equals("1") || strIn.substring(1,2).equals("2") ||
					strIn.substring(1,2).equals("3") || strIn.substring(1,2).equals("4") ||
					strIn.substring(1,2).equals("5") || strIn.substring(1,2).equals("6") ||
					strIn.substring(1,2).equals("7") || strIn.substring(1,2).equals("8") ||
					strIn.substring(1,2).equals("9") || strIn.substring(1,3).equals("10") ) )
		{
			System.out.println("Invalid\n");
			return false;
		}
				// valid Space?
		else
		{

			strSeg = strIn.substring(0, strIn.length() - 1);

			int yCor = Integer.parseInt( strSeg.substring(1) ) - 1;
			int xCor = (int)strIn.charAt(0) - 97;

			if (!board.isValid(yCor, xCor))
			{
				System.out.println("Invalid\n");
				return false;
			}
		}

			// true
		return true;
	}

	private static void clear ()
	{
		for (int s = 0; s < 100; s++)
					System.out.println();
	}

	private static String prompt (String in)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println(in);
		String str = scan.nextLine();
		return str.toLowerCase();
	}

	private static boolean isYes (String strIn)
	{
		char in;
		if (strIn.equals(""))
			in = 'n';
		else
			in = strIn.charAt(0);

		if (in == 'y' || in == 'Y')
			return true;

		return false;
	}

	private static void twoPlayer (Board[] board)
	{
		int n = 0;
		boolean next = true;
		Scanner scan = new Scanner(System.in);
		int[] scores = new int[2];
			scores[0] = 0;
			scores[1] = 0;
		String strIn = "";

			// User-prompt for random or custom setup
		System.out.println(board[0]);
		for (int x = 0; x < 2; x++)
		{
			int i = Math.abs(x - 1);

			strIn = prompt("Player " + (x+1) + " would you like to customize your ship layout?");

			if (isYes(strIn))
				userSetup(board[i]);
			else
				randomSetup(board[i]);
		}// End loop

			// Game play
		while (scores[0] < 17 && scores[1] < 17)
		{
				//Prompt for "ready" and "clears" screen then prints current board
			if (next == true)
			{
				System.out.println("Player " + (n+1) + ", press enter when ready...");
				scan.nextLine();
				System.out.println();

				clear();
			}

			System.out.println(board[n]);

				//User attack input
			strIn = prompt("Player " + (n+1) + ", enter attack coordinates... ");

				//Hit, miss, or invalid
			if (isValidInput(strIn, board[n]))
			{
				int yCor = Board.toY(strIn);
				int xCor = Board.toX(strIn);

				if ( board[n].attack(yCor , xCor) )
				{
					System.out.println("Hit\n");
					scores[n]++;
				}
				else System.out.println("Miss\n");

				System.out.println(board[n]);

				next = true;
				n = Math.abs(n - 1);
			}
			else next = false;

		}//End of loop

		System.out.println("Player " + (Math.abs(n - 1)) + " WON!");
	}

	private static void onePlayer (Board[] board)
	{
		int n = 0;
		boolean next = true;
		Scanner scan = new Scanner(System.in);
		int[] scores = new int[2];
			scores[0] = 0;
			scores[1] = 0;
		String strIn = "";
		boolean player = true;
		Computer computer = new Computer(board[1]);

			// User-prompt for random or custom setup
		System.out.println(board[1]);

		strIn = prompt("Would you like to customize your ship layout?");

				// Setup boards
		if (isYes(strIn))
			userSetup(board[1]);
		else
			randomSetup(board[1]);
		randomSetup(board[0]);

			// Game play loop
		while (scores[0] < 17 && scores[1] < 17)
		{

				//Prompt for "ready" and "clears" screen then prints current board
			if (next == true)
			{
				System.out.println("Press ENTER when ready...");
				scan.nextLine();
				System.out.println();

				clear();
			}

			if (player)
				System.out.println(board[n]);	// Displays current board for player's use

				//Attack input
			if (player)
				strIn = prompt("Enter attack coordinates... ");	// Get player input
			else
			{
				strIn = computer.getInput();	// Get computer input
				int index = strIn.indexOf(" ");

				char chX = (char) (Integer.parseInt(strIn.substring(0, index)) + 96);
				String strY = strIn.substring(index + 1);

				strIn = chX + "" + strY;
			}

				// Check if valid
			if (isValidInput(strIn, board[n]))
			{
				int xCor = 0;
				int yCor = 0;

				xCor = Board.toX(strIn);
				yCor = Board.toY(strIn);
					// Attack :: if hit
				if ( board[n].attack(yCor , xCor) )
				{
					System.out.println("Hit\n");	// Display hit
					scores[n]++;	// Increase score
					if (board[n].getSpace(xCor, yCor).revealed())
						scores[n]--;

					if (!player)	// If computer's turn => tell computer the outcome
						computer.resultOfAttack(true);
				}
					// If miss
				else
				{
					System.out.println("Miss\n");	// Display miss

					if (!player)	// If computer's turn => tell computer the outcome
						computer.resultOfAttack(false);
				}

				System.out.println(board[n]);	// Display (updated) board

					// Advance turn
				next = true;
				n = Math.abs(n - 1);
				player = !player;
			}
				// Invalid
			else next = false;

		}//End of loop

			// Publicize winner
		player = !player;
		n = Math.abs(n - 1);

		if (player)
			System.out.println("YOU WON");
		else
			System.out.println("You Lost");
	}
}

//a=97 - j=107