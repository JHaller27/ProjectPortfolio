import java.awt.*;

public class Cell
{
	private Color color;

	public Cell ()
	{
		color = null;
	}

	public boolean add (Color c)
	{
		if (color != null)
			return false;

		color = c;
		return true;
	}

	public Color getColor ()
	{
		return color;
	}

	public boolean flip ()	// Changes the color then returns if not null
	{
		if (color == Color.WHITE)
			color = Color.BLACK;
		else if (color == Color.BLACK)
			color = Color.WHITE;

		return color != null;
	}

	public void drawCell (Graphics g, int y, int x)		// Draws the square and piece (if !null)
	{
		g.setColor(Color.BLACK);
		g.drawRect(y, x, 50, 50);
		if (color != null)
		{
			g.setColor(Color.BLACK);
			g.drawArc(y + 5, x + 5, 40, 40, 0, 360);
			g.setColor(color);
			g.fillArc(y + 6, x + 6, 39, 39, 0, 360);
		}
	}
}