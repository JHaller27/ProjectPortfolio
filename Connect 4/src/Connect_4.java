/**
 * @(#)Connect_4.java
 *
 * Connect_4 Applet application
 *
 * @author James Haller
 * @version 1.00 2013/8/16
 */

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;

public class Connect_4 extends Applet implements Runnable, MouseListener
{
    public static void main(String[] args)
    {
        // create and set up the applet
        Connect_4 applet = new Connect_4();
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
	private Cell[][] board;
	private Color cTurn;
	private boolean done;

	Thread runner;

	public void init ()
	{
		done = false;
		board = new Cell[8][8];
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c] = new Cell();

		cTurn = Color.BLACK;

		addMouseListener(this);
	}
	public void reinit ()
	{
		done = false;
		board = new Cell[8][8];
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c] = new Cell();

		cTurn = Color.BLACK;
	}
	public void paint (Graphics g)
	{
		drawBoard(g);
	}

	private void drawBoard (Graphics g)
	{
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c].drawCell(g, r * 50, c * 50);

		g.setColor(cTurn);
		g.fillRect(10, 425, 100, 20);
		g.setColor(Color.BLACK);
		g.drawRect(10, 425, 100, 20);

		for (int c = 1, n = 25; c < 9; c++, n += 50)
			g.drawString("" + c, n, 415);
	}

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

	public void mouseEntered(MouseEvent me) {};
	public void mouseExited(MouseEvent me) {};
	public void mousePressed(MouseEvent me){};
	public void mouseReleased(MouseEvent me){};

	public void mouseClicked (MouseEvent me)
	{
		if (me.getClickCount() == 1 && !done)
			move((int)(me.getX() / 50));
	}

	private void move (int col)
	{
		int row = 7;
		while (row >= 0 && !board[row][col].add(cTurn))
		{
			row--;
		}

		repaint();

		if (checkNorth(row, col, 3, cTurn) || checkNorthEast(row, col, 3, cTurn) ||
			checkEast(row, col, 3, cTurn) || checkSouthEast(row, col, 3, cTurn) ||
			checkSouth(row, col, 3, cTurn) || checkSouthWest(row, col, 3, cTurn) ||
			checkWest(row, col, 3, cTurn) || checkNorthWest(row, col, 3, cTurn))
			{
				String name = "DEFAULT";
				if (cTurn == Color.BLACK)
					name = "Black";
				else if (cTurn == Color.RED)
					name = "Red";

				JOptionPane.showMessageDialog(null, "Congratulations " + name + " player, you win!");
				done = true;
				reinit();
				repaint();
			}

		else if (cTurn == Color.BLACK)
			cTurn = Color.RED;
		else
			cTurn = Color.BLACK;
	}

	private boolean checkNorth (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (y > 0)
			return checkNorth(x, y-1, counter-1, color);

		return false;
	}
	private boolean checkNorthEast (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (y > 0 && x < 7)
			return checkNorthEast(x+1, y-1, counter-1, color);

		return false;
	}
	private boolean checkEast (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (x < 7)
			return checkEast(x+1, y, counter-1, color);

		return false;
	}
	private boolean checkSouthEast (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (x < 7 && y < 7)
			return checkSouthEast(x+1, y+1, counter-1, color);

		return false;
	}
	private boolean checkSouth (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (y < 7)
			return checkSouth(x, y+1, counter-1, color);

		return false;
	}
	private boolean checkSouthWest (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (x > 0 && y < 7)
			return checkSouthWest(x-1, y+1, counter-1, color);

		return false;
	}
	private boolean checkWest (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (x > 0)
			return checkWest(x-1, y, counter-1, color);

		return false;
	}
	private boolean checkNorthWest (int x, int y, int counter, Color color)
	{
		if (board[x][y].getColor() != color)
			return false;

		if (counter == 0)
			return true;

		if (x > 0 && y > 0)
			return checkNorthWest(x-1, y-1, counter-1, color);

		return false;
	}
}