//Battleship(4) - Submarine(3) - AircraftCarrier(5) - Destroyer(3) - PatrolBoat(2)
public class Ship
{
	private int size;
	private int direction;
	private String type;

	public static final Ship PATROLBOAT = new Ship("Patrol Boat", 2);
	public static final Ship SUBMARINE = new Ship("Submarine", 3);
	public static final Ship DESTROYER = new Ship("Destroyer", 3);
	public static final Ship BATTLESHIP = new Ship("Battleship", 4);
	public static final Ship AIRCRAFTCARRIER = new Ship("Aircraft Carrier", 5);

		/* Contructs a new Ship
		 * @param: str = type
		 *         sizeIn = size*/
	public Ship (String str, int sizeIn)
	{
		type = str;
		size = sizeIn;
	}

		/* Sets the direction of this Ship
		 * @param: in = direction (VERTICAL or HORIZONTAL)*/
	public void setDir (int in)
	{
		direction = in;
	}
		/* Gets direction
		 * @return: int direction*/
	public int getDir ()
	{
		return direction;
	}

		/* Gets the size of the Ship
		 * @return: int size*/
	public int getSize ()
	{
		return size;
	}

		/* Gets the letter-symbol of this Ship
		 * @return: first letter of String type*/
	public String getLetter ()
	{
		return type.substring(0,1);
	}

	public String toString ()
	{
		return type + "(" + size + ")";
	}
}