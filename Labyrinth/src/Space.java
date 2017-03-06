import java.awt.*;
import java.util.ArrayList;

public class Space
{
	boolean north, south, east, west;	// Walls present?
	int xMod, yMod;
	ArrayList<Player> players;
	Token token;
	Color background = Globals.DEFAULTCOLOR;
	boolean moveable;

	public Space (boolean n, boolean s, boolean e, boolean w)
	{
		north = n;
		south = s;
		east = e;
		west = w;
		players = new ArrayList<Player>();
		token = null;
		moveable = true;
	}
	public Space (boolean n, boolean s, boolean e, boolean w, Color c)
	{
		north = n;
		south = s;
		east = e;
		west = w;
		background = c;
		players = new ArrayList<Player>();
		token = null;
		moveable = false;
	}
	public Space (boolean n, boolean s, boolean e, boolean w, Color c, Player p)
	{
		north = n;
		south = s;
		east = e;
		west = w;
		background = c;
		players = new ArrayList<Player>();
			players.add(p);
		token = null;
		moveable = false;
	}	// Generally useless

		// Token handling
	public void addToken()
	{
		int id = (int)(Math.random() * (Globals.store.size() - 1)) + 1;
		token = Globals.store.get(id);
		Globals.store.remove(id);
	}
	public void addToken(Token t){
		token = t;
	}
	public Token getToken() {
		return token;
	}
	public Token removeToken()
	{
		Token t = token;
		token = null;
		return t;
	}

		// Player handling
	public void addPlayer(Player p){
		players.add(p);
	}
	public void addPlayers(ArrayList<Player> p)
	{
		players = p;
	}
	public ArrayList<Player> getPlayers(){
		return players;
	}
	public Player removePlayer(Player p)
	{
		for (int n = 0; n < players.size(); n++)
			if (players.get(n) == p)
				return players.remove(n);

		return null;
	}
	public ArrayList<Player> removePlayers()
	{
		ArrayList<Player> tempPlayers = players;
		players.clear();
		return tempPlayers;
	}

	public void draw (Graphics g, int xMod, int yMod)
	{
		xMod *= Globals.SPACESIZE;
		yMod *= Globals.SPACESIZE;

			// Background
		g.setColor(background);
		g.fillRect(0 + xMod, 0 + yMod, Globals.SPACESIZE, Globals.SPACESIZE);

		g.setColor(Globals.BLACK);

			// Border
//		g.drawRect(0 + xMod, 0 + yMod, Globals.SPACESIZE, Globals.SPACESIZE);

			// Corners
		for (int x = 0; x <= Globals.SIZE * 2; x += Globals.SIZE * 2)
			for (int y = 0; y <= Globals.SIZE * 2; y += Globals.SIZE * 2)
				g.fillRect(x + xMod, y + yMod, Globals.SIZE, Globals.SIZE);

			// Walls
		if (north)
			g.fillRect(Globals.SIZE + xMod, 0 + yMod, Globals.SIZE, Globals.SIZE);
		if (south)
			g.fillRect(Globals.SIZE + xMod, Globals.SIZE * 2 + yMod, Globals.SIZE, Globals.SIZE);
		if (east)
			g.fillRect(Globals.SIZE * 2 + xMod, Globals.SIZE + yMod, Globals.SIZE, Globals.SIZE);
		if (west)
			g.fillRect(0 + xMod, Globals.SIZE + yMod, Globals.SIZE, Globals.SIZE);

			// Token
		if (token != null)
		{
			g.setColor(Globals.WHITE);
			g.fillArc(Globals.SIZE + xMod, Globals.SIZE + yMod, Globals.SIZE - 1, Globals.SIZE - 1, 0, 358);
			g.setColor(Globals.BLACK);
			g.drawArc(Globals.SIZE + xMod - 1, Globals.SIZE + yMod - 1, Globals.SIZE, Globals.SIZE, 0, 358);
			g.drawString("" + token.getId(), (int)(Globals.SIZE * 1.35 + xMod), (int)(Globals.SIZE * 1.6 + yMod));
		}

			// Player
		for (int n = 0; n < players.size(); n++)
		{
			g.setColor(players.get(n).getColor());
			g.fillArc(Globals.SIZE + xMod, Globals.SIZE + yMod, Globals.SIZE - 1, Globals.SIZE - 1, 0, 358);
			g.setColor(Globals.BLACK);
			g.drawArc(Globals.SIZE + xMod, Globals.SIZE + yMod, Globals.SIZE - 1, Globals.SIZE - 1, 0, 358);
		}
	}

	public boolean north(){
		return north;
	}
	public boolean south(){
		return south;
	}
	public boolean east(){
		return east;
	}
	public boolean west(){
		return west;
	}

		// Rotates walls clockwise
	public void rotate()
	{
		if (moveable)
		{
			boolean temp = north;
			north = west;
			west = south;
			south = east;
			east = temp;
		}
	}

	public void print()	// Debug
	{
		System.out.println("Space(" + north + ", " + south + ", " + east + ", " + west + ")");
	}
}