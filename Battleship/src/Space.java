public class Space
{
	public final int y;
	public final int x;
	private boolean revealed;
	private String symbol;
	private Ship occupant;

		/* Constructs a new Space
		 * @param: yIn,xIn = coordinates of new Space*/
	public Space (int yIn, int xIn)
	{
		y = yIn;
		x = xIn;
		revealed = false;
	}

		/* Reveals Space
		 * @return: Ship occupant or null*/
	public Ship reveal ()
	{
		revealed = true;
		return occupant;
	}
	public void hide ()
	{
		revealed = false;
	}

		/* Puts a Ship into this Space
		 * @param: s = Ship to add*/
	public void add (Ship s)
	{
		if (occupant == null)
		{
			symbol = s.getLetter();
			occupant = s;
		}
	}

		/* Gets the Ship at this Space
		 * @return: Ship occupant or null*/
	public Ship get ()
	{
		return occupant;
	}

		/* Checks is this Space is revealed
		 * @return: true = revealed, false = hidden*/
	public boolean revealed ()
	{
		return revealed;
	}

	public String toString ()
	{
		if (revealed)
			if (occupant == null)
				return " x";
			else return " " + occupant.getLetter();
		else
			return "  ";
	}
}