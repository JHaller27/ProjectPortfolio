import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Labyrinth extends Applet implements MouseListener, Runnable, ActionListener, KeyListener
{
	Space[][] board;	// board[x][y]
	Player[] players;
	Thread runner;
	int mouseX, mouseY;
	// Globals.tokenNum = 1	Token to chase
	// Globals.turn = 0		Whose turn
	// Globals.phase = 0	0 = slide, 1 = move, 2 = token

	public static void main(String[] args)
	{
		// create and set up the applet
		Labyrinth applet = new Labyrinth();
		Dimension d = new Dimension(1000, 1000);
		applet.setMinimumSize(d);
		applet.setPreferredSize(d);
		applet.init();

		// create a frame to host the applet, which is just another type of Swing Component
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// add the applet to the frame and show it
		mainFrame.getContentPane().add(applet);
		mainFrame.pack();
		mainFrame.setVisible(true);

		// start the applet
		applet.start();
	}

	public void init ()
	{
		Globals.init();
		board = new Space[7][7];

			// Instructions
		JOptionPane.showMessageDialog(null, "You've got a magic formula that will make you the Master\n" +
			"Magician. But can you get the ingredients you need? Other wizards\n" +
			"will be racing to get them, too!\n\n" +
			"There are twenty-one magic items in the labyrinth, but they can only\n" +
			"be picked up in numerical order. The wizard who gets to item number\n" +
			"one first may keep it, then all the wizards race for number two.\n\n" +
			"On every turn the labyrinth shifts, opening up some passages and\n" +
			"closing others. Beware -- it's easy to get lost in the twisting\n" +
			"corridors! But if you can get the ingredients you need -- and keep\n" +
			"other wizards from getting theirs -- you can score the most points\n" +
			"and become the Master Magician.");

			// Create players array
				// Players: Get number of players
		Object[] possibleValues = { "Two", "Three", "Four" };
		Object selectedValue = JOptionPane.showInputDialog(null, "How many players?", "Input", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue == possibleValues[0])
			players = new Player[2];
		if (selectedValue == possibleValues[1])
			players = new Player[3];
		if (selectedValue == possibleValues[2])
			players = new Player[4];


				// Players: Get name & color
		Object[] allColorValues = { "Brown", "Blue", "White", "Red" };

				// ArrayList so already choosen colors are not chooseable
		ArrayList<String> availableColors = new ArrayList<String>();
			availableColors.add("Brown");
			availableColors.add("Blue");
			availableColors.add("White");
			availableColors.add("Red");

		for (int i = 0; i < players.length; i++)
		{
				// JOptionPane prompt to input a name
			String na = JOptionPane.showInputDialog("Input your name...");
				// JOptionPane prompt to choose a color
			possibleValues = new Object[availableColors.size()];
			for (int n = 0; n < possibleValues.length; n++)
				possibleValues[n] = availableColors.get(n);
			selectedValue = JOptionPane.showInputDialog(null, na + " the ...", "Input", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);

				// create new player
			Color c = Globals.BLACK;
					// change c to match selection & remove selection from ArrayList availableColors
			if (selectedValue == allColorValues[0])
				c = Globals.BROWN;
			else if (selectedValue == allColorValues[1])
				c = Globals.BLUE;
			else if (selectedValue == allColorValues[2])
				c = Globals.WHITE;
			else if (selectedValue == allColorValues[3])
				c = Globals.RED;

			players[i] = new Player(na, c);
			JOptionPane.showMessageDialog(null, players[i].getName() + ", your secret formula is:\n" + players[i].getFormula());

			for (int n = 0; n < availableColors.size(); n++)
				if (availableColors.get(n) == selectedValue)
					availableColors.remove(n);
		}

			// Begin message
		JOptionPane.showMessageDialog(null, "It is the turn of " + players[Globals.turn].getName() + "\nYou are racing for ingredient number " + Globals.tokenNum);

			// Movable spaces
		for (int x = 0; x < board.length; x++)
			for (int y = 0; y < board[x].length; y++)
			{
				boolean n = false, s = false, e = false, w = false;
				int numWalls = (int)(Math.random() * 3) + 1;
				if (numWalls == 3)
					numWalls = 2;
				while (numWalls > 0)
				{
					switch ((int)(Math.random() * 4))
					{
						case 0: n = true; break;
						case 1: s = true; break;
						case 2: e = true; break;
						case 3: w = true; break;
					}

					numWalls--;
				}
				board[x][y] = new Space(n, s, e, w);
			}
			// Corners
		board[0][0] = new Space(true, false, false, true, Globals.GRAY);
		board[6][0] = new Space(true, false, true, false, Globals.GRAY);
		board[0][6] = new Space(false, true, false, true, Globals.GRAY);
		board[6][6] = new Space(false, true, true, false, Globals.GRAY);
			// Top row
		board[2][0] = new Space(true, false, false, false, Globals.GRAY);
		board[4][0] = new Space(true, false, false, false, Globals.GRAY);
			// Bottom row
		board[2][6] = new Space(false, true, false, false, Globals.GRAY);
		board[4][6] = new Space(false, true, false, false, Globals.GRAY);
			// Left column
		board[0][2] = new Space(false, false, false, true, Globals.GRAY);
		board[0][4] = new Space(false, false, false, true, Globals.GRAY);
			// Right column
		board[6][2] = new Space(false, false, true, false, Globals.GRAY);
		board[6][4] = new Space(false, false, true, false, Globals.GRAY);
			// Start pieces
		board[2][2] = new Space(false, false, false, true, Globals.BROWN);
		board[4][2] = new Space(true, false, false, false, Globals.BLUE);
		board[2][4] = new Space(false, true, false, false, Globals.RED);
		board[4][4] = new Space(false, false, true, false, Globals.WHITE);

		for (Player p : players)
		{
			if (p.getColor() == Globals.BROWN)
				board[2][2].addPlayer(p);
			else if (p.getColor() == Globals.BLUE)
				board[4][2].addPlayer(p);
			else if (p.getColor() == Globals.RED)
				board[2][4].addPlayer(p);
			else if (p.getColor() == Globals.WHITE)
				board[4][4].addPlayer(p);
		}

			// Add tokens
		for (int x = 1; x < board.length-1; x++)
			for (int y = 1; y < board[x].length-1; y++)
			{
				boolean validX = !(x == 2 || x == 4);
				boolean validY = !(y == 2 || y == 4);
				if (validX || validY)
					board[x][y].addToken();
			}

			// MouseListener
		addMouseListener(this);
			// KeyListener
		addKeyListener(this);
		requestFocus();
	}

	public void paint (Graphics g)
	{
		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
				board[row][col].draw(g, row, col);
		Globals.hold.draw(g, 8, 0);
	}

		// Shift methods
	private void shiftUp (int row)
	{
		Space temp;
		Token tempToken;
		ArrayList<Player> tempPlayers = new ArrayList<Player>();

		if (row % 2 != 0)
		{
				// Save temporary duplicates of the last Space and its Token and Player list
			temp = board[row][0];
			tempToken = temp.removeToken();
			tempPlayers = temp.getPlayers();

				// Slide Spaces
			for (int col = 0; col < board[row].length - 1; col++)
				board[row][col] = board[row][col + 1];

				// Place saved Space and its Token and Player list
			board[row][board.length - 1] = Globals.hold;
			board[row][board.length - 1].addToken(tempToken);
			board[row][board.length - 1].addPlayers(tempPlayers);
			Globals.hold = new Space(temp.north(), temp.south(), temp.east(), temp.west());
		}
	}
	private void shiftDown (int row)
	{
		Space temp;
		Token tempToken;
		ArrayList<Player> tempPlayers = new ArrayList<Player>();

		if (row % 2 != 0)
		{
				// Save temporary duplicates of the last Space and its Token and Player list
			temp = board[row][board.length - 1];
			tempToken = board[row][board.length - 1].removeToken();
			tempPlayers = temp.getPlayers();

				// Slide Spaces
			for (int col = board[row].length - 1; col > 0; col--)
				board[row][col] = board[row][col - 1];

				// Place saved Space and its Token and Player list
			board[row][0] = Globals.hold;
			board[row][0].addToken(tempToken);
			board[row][0].addPlayers(tempPlayers);
			Globals.hold = new Space(temp.north(), temp.south(), temp.east(), temp.west());
		}
	}
	private void shiftLeft (int col)
	{
		Space temp;
		Token tempToken;
		ArrayList<Player> tempPlayers = new ArrayList<Player>();

		if (col % 2 != 0)
		{
				// Save temporary duplicates of the last Space and its Token and Player list
			temp = board[0][col];
			tempToken = board[0][col].removeToken();
			tempPlayers = temp.getPlayers();

				// Slide Spaces
			for (int row = 0; row < board[row].length - 1; row++)
				board[row][col] = board[row + 1][col];

				// Place saved Space and its Token and Player list
			board[board.length - 1][col] = Globals.hold;
			board[board.length - 1][col].addToken(tempToken);
			board[board.length - 1][col].addPlayers(tempPlayers);
			Globals.hold = new Space(temp.north(), temp.south(), temp.east(), temp.west());
		}
	}
	private void shiftRight (int col)
	{
		Space temp;
		Token tempToken;
		ArrayList<Player> tempPlayers = new ArrayList<Player>();

		if (col % 2 != 0)
		{
				// Save temporary duplicates of the last Space and its Token and Player list
			temp = board[board.length - 1][col];
			tempToken = board[board.length - 1][col].removeToken();
			tempPlayers = temp.getPlayers();

				// Slide Spaces
			for (int row = board.length - 1; row > 0; row--)
				board[row][col] = board[row - 1][col];

				// Place saved Space and its Token and Player list
			board[0][col] = Globals.hold;
			board[0][col].addToken(tempToken);
			board[0][col].addPlayers(tempPlayers);
			Globals.hold = new Space(temp.north(), temp.south(), temp.east(), temp.west());
		}
	}

		// MouseListener methods
		// Slide board
	public void mouseExited (MouseEvent me){};
	public void mouseEntered (MouseEvent me){};
	public void mouseClicked (MouseEvent me)
	{
		int x = (int)(me.getX() / Globals.SPACESIZE);
		int y = (int)(me.getY() / Globals.SPACESIZE);

		if (x == 8 && y == 0 && Globals.phase == 0)
		{
			Globals.hold.rotate();
			return;
		}
	}
	public void mousePressed (MouseEvent me)
	{
		mouseX = (int)(me.getX() / Globals.SPACESIZE);
		mouseY = (int)(me.getY() / Globals.SPACESIZE);
	}
	public void mouseReleased (MouseEvent me)
	{
		int x = (int)(me.getX() / Globals.SPACESIZE);
		int y = (int)(me.getY() / Globals.SPACESIZE);

		mouseX = x - mouseX;
		mouseY = y - mouseY;

		if (mouseX != 0)
			mouseX /= Math.abs(mouseX);
		if (mouseY != 0)
			mouseY /= Math.abs(mouseY);

		if ( !((mouseX == 0 && mouseY == 0) || (mouseX != 0 && mouseY != 0)) && Globals.phase == 0)
		{
			if (mouseY == -1)		// shiftUp
			{
				shiftUp(x);
			}
			else if (mouseY == 1)	// shiftDown
			{
				shiftDown(x);
			}
			else if (mouseX == -1)	// shiftLeft
			{
				shiftLeft(y);
			}
			else if (mouseX == 1)	// shiftRight
			{
				shiftRight(y);
			}

			Globals.advancePhase(players);
		}

		repaint();
	}

		// KeyListener methods
		// Player movement
	public void keyPressed (KeyEvent ke)
	{
		if (Globals.phase != 1)
			return;

			// Find Player to manipulate and its current position
		Player player = players[Globals.turn];
		int xCor = -1, yCor = -1;

		for (int x = 0; x < board.length; x++)
			for (int y = 0; y < board[x].length; y++)
			{
				for (int i = 0; i < board[x][y].getPlayers().size(); i++)
				{
					if (player == board[x][y].getPlayers().get(i))
					{
						xCor = x;
						yCor = y;
						board[xCor][yCor].removePlayer(player);
					}
				}
			}

			// Use wand
		if (ke.getKeyCode() == 87 && Globals.useWand(players[Globals.turn]))
		{
			JOptionPane.showMessageDialog(null, "You used one of your " + players[Globals.turn].getWands() + " remaining wands");
		}

			// End move phase, check for Token, then end token phase
		if (ke.getKeyCode() == 32)
		{
			Globals.advancePhase(players);

			if (Globals.phase != 2)
				return;

			Token t = board[xCor][yCor].getToken();
			if (t != null && t.getId() == Globals.tokenNum){
				Globals.advanceTokenNum();
				player.addToken(board[xCor][yCor].removeToken());
			}

			Globals.advancePhase(players);
			board[xCor][yCor].addPlayer(player);
			return;
		}

			// Move player
		if (ke.getKeyCode() == 37 && xCor > 0 && !board[xCor - 1][yCor].east() && !board[xCor][yCor].west())	// left
			board[xCor - 1][yCor].addPlayer(player);

		else if (ke.getKeyCode() == 38 && yCor > 0 && !board[xCor][yCor - 1].south() && !board[xCor][yCor].north())	// up
			board[xCor][yCor - 1].addPlayer(player);

		else if (ke.getKeyCode() == 39 & xCor < 6 && !board[xCor + 1][yCor].west() && !board[xCor][yCor].east())	// right
			board[xCor + 1][yCor].addPlayer(player);

		else if (ke.getKeyCode() == 40 && yCor < 6 && !board[xCor][yCor + 1].north() && !board[xCor][yCor].south())	// down
			board[xCor][yCor + 1].addPlayer(player);
		else
		{
			board[xCor][yCor].addPlayer(player);
		}

		repaint();
	}
	public void keyReleased (KeyEvent ke){};
	public void keyTyped (KeyEvent ke){};

		// ActionListener methods
	public void actionPerformed (ActionEvent ae){};

		// Runnable
	public void start ()
	{
		if (runner == null)
		{
			runner = new Thread(this);
			runner.start();
		}
	}
	public void stop ()
	{
		if (runner != null)
		{
			runner.stop();
			runner = null;
		}
	}
	public void run (){};

	public void update (Graphics g){
		paint(g);
	}
}