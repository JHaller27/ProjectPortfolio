/**
 * @(#)Othello.java
 *
 * Othello Applet application
 *
 * @author
 * @version 1.00 2013/8/12
 */

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Othello extends Applet implements Runnable, MouseListener
{
	private int max;
	private Cell[][] board = new Cell[8][8];
	private String text;
	private boolean turn;	// true=white, false=black
	private Color cTurn;
	private int moves;

	Thread runner;

	public static void main(String[] args)
	{
		// create and set up the applet
		Othello applet = new Othello();
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
		max = 400;
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c] = new Cell();
		turn = true;
		cTurn = Color.WHITE;
		text = (60 - moves) + " turns left";
		addMouseListener(this);

		board[3][3].add(Color.WHITE);
		board[4][4].add(Color.WHITE);
		board[3][4].add(Color.BLACK);
		board[4][3].add(Color.BLACK);

		moves = 0;
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

		g.drawString(text, 170, 435);

		for (int c = 97, n = 25; c < 105; c++, n += 50)
			g.drawString("" + (char)c, 410, n);
		for (int c = 1, n = 25; c < 9; c++, n += 50)
			g.drawString("" + c, n, 415);
	}

	private int countWhite ()
	{
		int i = 0;
		for (Cell[] arr : board)
			for (Cell c : arr)
				if (c.getColor() == Color.WHITE)
					i++;
		return i;
	}
	private int countBlack ()
	{
		int i = 0;
		for (Cell[] arr : board)
			for (Cell c : arr)
				if (c.getColor() == Color.BLACK)
					i++;
		return i;
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

	public void move (int y, int x)
	{
		if (board[x][y].getColor() == null)
		{
			board[x][y].add(cTurn);
			repaint();
			moves++;
			text = (60 - moves) + " turns left";
		}
		else
			return;

			// Flips
		int n;
		boolean loop;
		ArrayList<Cell> arr = new ArrayList<Cell>();

		// North
		n = 1;
		loop = true;
		arr.clear();

		while (y - n >= 0 && loop)
		{
			Cell cell = board[x][y-n];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// NorthEast
		n = 1;
		loop = true;
		arr.clear();

		while (y - n >= 0 && x + n < 8 && loop)
		{
			Cell cell = board[x+n][y-n];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// East
		n = 1;
		loop = true;
		arr.clear();

		while (x + n < 8 && loop)
		{
			Cell cell = board[x+n][y];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// SouthEast
		n = 1;
		loop = true;
		arr.clear();

		while (y + n < 8 && x + n < 8 && loop)
		{
			Cell cell = board[x+n][y+n];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// South
		n = 1;
		loop = true;
		arr.clear();

		while (y + n < 8 && loop)
		{
			Cell cell = board[x][y+n];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// SouthWest
		n = 1;
		loop = true;
		arr.clear();

		while (y + n < 8 && x - n >= 0 && loop)
		{
			Cell cell = board[x-n][y+n];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// West
		n = 1;
		loop = true;
		arr.clear();

		while (x - n >= 0 && loop)
		{
			Cell cell = board[x-n][y];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// NorthWest
		n = 1;
		loop = true;
		arr.clear();

		while (y - n >= 0 && x - n >= 0 && loop)
		{
			Cell cell = board[x-n][y-n];

			if (cell.getColor() == null)
				loop = false;
			else if (cell.getColor() == cTurn)
			{
				for (Cell c : arr)
					c.flip();
				loop = false;
			}
			else if (cell.getColor() != cTurn)
			{
				arr.add(cell);
				n++;
			}
		}

		// Switch turn
		if (turn)
			cTurn = Color.BLACK;
		else
			cTurn = Color.WHITE;

		turn = !turn;

		repaint();
	}

	public void mouseEntered(MouseEvent me) {};
	public void mouseExited(MouseEvent me) {};
	public void mousePressed(MouseEvent me){};
	public void mouseReleased(MouseEvent me){};

	public void mouseClicked(MouseEvent me)
	{
		System.out.println("mouseClicked");

		int y = (int)(me.getY() / 50);
		int x = (int)(me.getX() / 50);

		move(y, x);

		// Win statement
		if (moves == 60)
		{
			if (countWhite() > countBlack())
				JOptionPane.showMessageDialog(null, "WHITE WINS!!!");
			else if (countBlack() > countWhite())
				JOptionPane.showMessageDialog(null, "BLACK WINS!!!");
			else if (countWhite() == countBlack())
				JOptionPane.showMessageDialog(null, "---TIE---");

			init();
			repaint();
		}
	}
}

// a = 97, z = 122