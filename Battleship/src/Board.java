import java.util.ArrayList;
import java.util.Scanner;

// A-J, 1-10
public class Board
{
	public static final int VERTICAL = 0, HORIZONTAL = 1;
	private Space[][] grid;
	private ArrayList<Ship> fleet;

	public Board ()
	{
		fleet = new ArrayList<Ship>();
			fleet.add(Ship.PATROLBOAT);
			fleet.add(Ship.SUBMARINE);
			fleet.add(Ship.DESTROYER);
			fleet.add(Ship.BATTLESHIP);
			fleet.add(Ship.AIRCRAFTCARRIER);

		grid = new Space[10][10];
		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[r].length; c++)
				grid[r][c] = new Space(r, c);
	}

		/* Checks if a Space is valid
		 * @param: yIn, xIn = int 0-9
		 * @return: true = valid, false = invalid*/
	public boolean isValid (int yIn, int xIn)
	{
		if (yIn < grid.length && xIn < grid.length)
			return true;
		return false;
	}

		/* Checks if the Spaces for dist spaces in direction dir
		 * @param: sp = Starting Space
		 *         dir = direction
		 *         dist = distance
		 * @return: true = Spaces open for a Ship*/
	private boolean isEmpty (Space sp, int dir, int dist)
	{
		boolean result = true;

		if (dir == VERTICAL)
			for (int n = 0; n < dist && sp.y + n < 10; n++)
				if (get(sp.y + n, sp.x) != null)
					result = false;

		if (dir == HORIZONTAL)
			for (int n = 0; n < dist && sp.x + n < 10; n++)
				if (get(sp.y, sp.x + n) != null)
					result = false;

		return result;
	}

		/* Adds a ship if target Spaces are open
		 * @param: index = index of Ship from fleet
		 *         sp = Starting Space
		 *         dir = direction
		 * @return: true = ship added, false = ship cannot be added*/
	public boolean addShip (int index, Space sp, int dir)
	{
		if (index > fleet.size()-1)
			return false;
		Ship s = fleet.get(index);
		s.setDir(dir);

		if (isEmpty(sp, dir, s.getSize()))
		{
			for (int n = 0; n < s.getSize(); n++)
			{
				if (s.getDir() == VERTICAL)
				{
					if (!isValid(sp.y+s.getSize()-1, sp.x))
						return false;
					grid[sp.y+n][sp.x].add(s);
					grid[sp.y+n][sp.x].reveal();
				}

				else if (s.getDir() == HORIZONTAL)
				{
					if (!isValid(sp.y, sp.x+s.getSize()-1))
						return false;
					grid[sp.y][sp.x+n].add(s);
					grid[sp.y][sp.x+n].reveal();
				}
			}
			return true;
		}

		return false;
	}

		/*
		 *Hides all spaces in grid
		 */
	public void hideAll ()
	{
		for (Space[] sp : grid)
			for (Space s : sp)
				s.hide();
	}

		/* Gets ship from grid
		 * @param: y,x = int 0-9
		 * @return: Ship at (y,x)
		 *          null = empty*/
	public Ship get (int y, int x)
	{
		return grid[y][x].get();
	}

	public Space getSpace (int y, int x)
	{
		return grid[y][x];
	}

		/* Gets ship from fleet
		 * @param: index = index in fleet
		 * @return: Ship in fleet at (y,x)*/
	public Ship getFromFleet (int index)
	{
		return fleet.get(index);
	}

		/* Reveals Space
		 * @param: y,x = int 0-9
		 * @return: true = hit, false = miss*/
	public boolean attack (int y, int x)
	{
		if (y > 10 || x > 10)
			return false;
		return grid[y][x].reveal() != null;
	}

		/*
		 *@param: String starting with a char
		 *@return: int 0-9
		 */
	public static int toX (String strIn)
	{
		return (int)strIn.charAt(0) -97;
	}
		/*
		 *@param: String ending in a number
		 *@return: int 0-9
		 */
	public static int toY (String strIn)
	{
		return Integer.parseInt( strIn.substring(1) ) -1;
	}

	public String toString ()
	{
		String result =	"  | A B C D E F G H I J\n";
		result += 		"--+---------------------\n";
		for (int row = 0; row < 9; row++)
		{
			result += " " + (row+1) + "|";
			for (int col = 0; col < 10; col++)
				result += grid[row][col].toString();
			result += "\n";
		}
		result += "10|";
		for (int col = 0; col < 10; col++)
			result += grid[9][col].toString();

		return result + "\n";
	}
}