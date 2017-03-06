public class Computer
{
	Board board;
	int lastX;
	int lastY;
	int lastHitX;
	int lastHitY;
	int firstHitX;
	int firstHitY;
	int[][] pastHits; // -1 = never hit, 0 = miss, 1 = hit
	boolean foundShip;
	boolean foundDir;
	int dir;
	int size;

	public Computer (Board bIn)
	{
		foundShip = false;
		foundDir = false;
		dir = 0;
		lastX = -1;
		lastY = -1;
		lastHitX = -1;
		lastHitY = -1;
		firstHitX = -1;
		firstHitY = -1;
		size = 0;
		board = bIn;

		pastHits = new int[10][10];
		for (int r = 0; r < 10; r++)
		for (int c = 0; c < 10; c++)
			pastHits[r][c] = -1;
	}

	/*
	 *@return: int xCor + " " + int yCor
	 *         int = 1-10
	 */
	public String getInput ()
	{
		String str = "";
		if (foundShip)
			str = intellInput();
		else
			str = randomInput();

		int index = str.indexOf(" ");
		lastX = Integer.parseInt(str.substring(0,index));
		lastY = Integer.parseInt(str.substring(index+1));

		switch (pastHits[lastX - 1][lastY - 1]) // If already fired at lastX,lastY fire again
		{
			case 0: str = getInput(); break;
			case 1: str = getInput(); break;
		}

		return str;
	}

	/*
	 * Gets random coordinates to fire at
	 * @return: String (int + int)
	 *          int = 1-10
	 */
	public String randomInput ()
	{
		int x = (int) (Math.random() * 10 + 1);
		int y = (int) (Math.random() * 10 + 1);
		return x + " " + y;
	}

	/*
	 * Makes an intelligent decision based on:
	 *  - Last attack
	 *  - Last hit
	 *  - First hit of current ship
	 *  - Known direction
	 *  - Size of ship
	 *
	 * @return: int xCor + " " + int yCor
	 */

	/*
	 *East = 0
	 *North = 1
	 *West = 2
	 *South = 3
	 */
	public String intellInput ()
	{
		int x = lastHitX;
		int y = lastHitY;

		switch (dir)
		{
			case 0: x++; break;
			case 1: y--; break;
			case 2: x--; break;
			case 3: y++; break;
		}

		return x + " " + y;
	} // End intellInput()

	public void resultOfAttack (boolean b)
	{
		if (b) // If hit
		{
			pastHits[lastX - 1][lastY - 1] = 1;
			lastHitX = lastX;
			lastHitY = lastY;
			if (foundShip)
			{
				if (!foundDir)
					foundDir = true;
					size--;
			}
			else
			{
				foundShip = true;
				firstHitX = lastX;
				firstHitY = lastY;
				size = board.get(lastY - 1, lastX - 1).getSize() - 1;
			}
		}
		else // If miss
		{
			pastHits[lastX - 1][lastY - 1] = 0;
			if (foundShip)
			{
				if (!foundDir)
				{
					boolean loop;
					do
					{
						loop = false;
						if (firstHitX == 1 && dir == 2)
						{
							dir ++;
							loop = true;
						}
						if (firstHitX == 10 && dir == 0)
						{
							dir ++;
							loop = true;
						}
						if (firstHitY == 1 && dir == 1)
						{
							dir ++;
							loop = true;
						}
						if (firstHitY == 10 && dir == 3)
						{
							dir ++;
							loop = true;
						}
					}
					while (loop);
					dir ++;
				}
				else
				{
					switch (dir)
					{
						case 0: dir = 2; break;
						case 1: dir = 3; break;
						case 2: dir = 0; break;
						case 3: dir = 1; break;
					}
					lastHitX = firstHitX;
					lastHitY = firstHitY;
				}
			}
		}

		if (size == 0)
		{
			foundShip = false;
			foundDir = false;
			firstHitX = -1;
			firstHitY = -1;
			lastHitX = -1;
			lastHitY = -1;
			dir = 0;
		}

			System.out.println("Computer.RoA() last attack: " + lastX + "," + lastY);
			System.out.println("Computer.RoA() last hit: " + lastHitX + "," + lastHitY);
			System.out.println("Computer.RoA() first hit: " + firstHitX + "," + firstHitY);
			System.out.println("Computer.RoA() foundShip?: " + foundShip);
			System.out.println("Computer.RoA() dir: " + dir);
			System.out.println("Computer.RoA() size: " + size);
	} // End resultOfAttack()
}
/*
 * To fix:
 *  - fire at same spot more than once
 */