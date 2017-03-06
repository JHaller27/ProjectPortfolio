import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Game2048 extends Applet implements KeyListener {

	final int XMAX = 400;
	final int YMAX = 400;
	final int XSIZE = XMAX / 4;
	final int YSIZE = YMAX / 4;
	int[][] grid;
	boolean moving;

	public static void main(String[] args)
	{
		// create and set up the applet
		Game2048 applet = new Game2048();
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

	public void init() {
		JOptionPane.showMessageDialog(null,
			"2048 Instructions:\n\n" +
			"- Use the arrow keys to move the numbers\n" +
			"- Press 'R' to reset the board\n" +
			"- Combine equal numbers to double them\n" +
			"- Every time you move, a new number will randomly appear\n" +
			"- GOAL: Create the number 2048 (and beyond)");
			// create a 4x4 grid filled with int values of 0
		grid = new int[4][4];
		for (int x = 0; x < grid.length; x++)
			for (int y = 0; y < grid[x].length; y++)
				grid[x][y] = 0;

			// set 2 blocks to 2 (w/ a 20% chance of a 4)
		for (int i = 0; i < 2; i++)
			addRandomValue();

			// normal inits
		requestFocus();
		addKeyListener(this);
		moving = false;
	}
	public void reset () {
			// create a 4x4 grid filled with int values of 0
		grid = new int[4][4];
		for (int x = 0; x < grid.length; x++)
			for (int y = 0; y < grid[x].length; y++)
				grid[x][y] = 0;

			// set 2 blocks to 2 (w/ a 20% chance of a 4)
		for (int i = 0; i < 2; i++)
			addRandomValue();

			// normal inits
		requestFocus();
		addKeyListener(this);
		moving = false;

	}
	public void paint(Graphics g) {
			// set font
		g.setFont(new Font("TimesRoman", Font.PLAIN, 48));

			// draw grid boxes
		for (int x = 0; x < XMAX; x = x + XSIZE)
			for (int y = 0; y < YMAX; y = y + YSIZE) {
				g.drawRect(x, y, XSIZE, YSIZE);
			}

				// draw grid values
		for (int x = 0; x < grid.length; x++)
			for (int y = 0; y < grid[x].length; y++) {
				if (grid[x][y] != 0)
					g.drawString("" + grid[x][y], x*XSIZE + (XSIZE/2) - 24, y*YSIZE + (YSIZE/2) + 12);
			}

	}

	public void shiftUp () {
		boolean moved = false;
			// Loop through all blocks except top row
		for (int x = 0; x < grid.length; x++)
			for (int y = 1; y < grid[x].length; y++) {
					// If a block is not empty...
				if (grid[x][y] != 0) {
						// ...move it up until it reaches y=0 or a block it cannot merge with
					for (int newY = y; newY > 0; newY--) {
						if (grid[x][newY-1] == 0) {
							grid[x][newY-1] += grid[x][newY];
							grid[x][newY] = 0;
						}
							// If it merged then check next block
						else if (grid[x][newY-1] == grid[x][newY]) {
							grid[x][newY-1] += grid[x][newY];
							grid[x][newY] = 0;
							newY = 0;
							y++;
						}
					}
					moved = true;
				}
			}
		if (moved)
			addRandomValue();
		repaint();
	}
	public void shiftDown() {
		boolean moved = false;
			// Loop through all blocks except bottom row
		for (int x = 0; x < grid.length; x++)
			for (int y = grid[x].length-1; y >= 0; y--) {
				// If a block is not empty...
			if (grid[x][y] != 0) {
					// ...move it down until it reaches y=grid.length-1 or a block it cannot merge with
				for (int newY = y; newY < grid.length-1; newY++) {
					if (grid[x][newY+1] == 0) {
							grid[x][newY+1] += grid[x][newY];
							grid[x][newY] = 0;
					}
						// If it merged then check next block
					else if (grid[x][newY+1] == grid[x][newY]){
						grid[x][newY+1] += grid[x][newY];
						grid[x][newY] = 0;
						newY = grid.length-1;
						y--;
					}
				}
				moved = true;
			}
		}
		if (moved)
			addRandomValue();
		repaint();
	}
	public void shiftLeft() {
		boolean moved = false;
			// Loop through all blocks except left column
		for (int y = 0; y < grid.length; y++)
			for (int x = 1; x < grid[y].length; x++) {
					// If a block is not empty...
				if (grid[x][y] != 0) {
						// ...move it up until it reaches y=0 or a block it cannot merge with
					for (int newX = x; newX > 0; newX--) {
						if (grid[newX-1][y] == 0) {
							grid[newX-1][y] += grid[newX][y];
							grid[newX][y] = 0;
						}
							// If it merged then check next block
						else if (grid[newX-1][y] == grid[newX][y]) {
							grid[newX-1][y] += grid[newX][y];
							grid[newX][y] = 0;
							newX = 0;
							x++;
						}
					}
					moved = true;
				}
			}
		if (moved)
			addRandomValue();
		repaint();
	}
	public void shiftRight() {
		boolean moved = false;
			// Loop through all blocks except bottom row
		for (int y = 0; y < grid.length; y++)
			for (int x = grid[y].length-1; x >= 0; x--) {
				// If a block is not empty...
			if (grid[x][y] != 0) {
					// ...move it down until it reaches y=grid.length-1 or a block it cannot merge with
				for (int newX = x; newX < grid.length-1; newX++) {
					if (grid[newX+1][y] == 0) {
							grid[newX+1][y] += grid[newX][y];
							grid[newX][y] = 0;
					}
						// If it merged then check next block
					else if (grid[newX+1][y] == grid[x][y]){
						grid[newX+1][y] += grid[newX][y];
						grid[newX][y] = 0;
						newX = grid.length-1;
						x--;
					}
				}
				moved = true;
			}
		}
		if (moved)
			addRandomValue();
		repaint();
	}

	private void addRandomValue() {
		ArrayList<Integer> xCors = new ArrayList<Integer>();
		ArrayList<Integer> yCors = new ArrayList<Integer>();
		int arraySize = 0;

		for (int x = 0; x < grid.length; x++)
			for (int y = 0; y < grid[x].length; y++) {
				if (grid[x][y] == 0) {
					xCors.add(new Integer(x));
					yCors.add(new Integer(y));
					arraySize++;
				}
			}

		int i = (int)(Math.random() * arraySize);
		if ((int)(Math.random()*5) == 0)
			grid[xCors.remove(i)][yCors.remove(i)] = 4;
		else
			grid[xCors.remove(i)][yCors.remove(i)] = 2;

	}

	public void keyReleased (KeyEvent ke) {
			// if key is released then allow the board to move
		moving = false;
	}
	public void keyPressed (KeyEvent ke) {
			// if key is already being moved (i.e. key is held) then don't move
		if (moving)
			return;

			// arx keys shift board
		if (ke.getKeyCode() == ke.VK_UP) {
			shiftUp();
			moving = true;
			return;
		}
		if (ke.getKeyCode() == ke.VK_DOWN){
			shiftDown();
			moving = true;
			return;
		}
		if (ke.getKeyCode() == ke.VK_LEFT){
			shiftLeft();
			moving = true;
			return;
		}
		if (ke.getKeyCode() == ke.VK_RIGHT){
			shiftRight();
			moving = true;
			return;
		}
		if (ke.getKeyCode() == ke.VK_R){
			reset();
			repaint();
		}
	}

	public void keyTyped (KeyEvent ke) {}
}

// TBD = shiftUp(), shiftDown(), shiftLeft(), shiftRight()