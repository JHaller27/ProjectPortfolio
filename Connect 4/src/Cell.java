import java.awt.*;

public class Cell
{
	private Color color;

	public Cell ()
	{
		color = Color.WHITE;
	}

	public boolean add (Color c)
	{
		if (color != Color.WHITE)
			return false;

		color = c;
		return true;
	}

	public Color getColor ()
	{
		return color;
	}

	public void drawCell (Graphics g, int y, int x)		// Draws the square and piece (if !null)
	{
		g.setColor(color);
		g.fillArc(x + 1, y + 1, 49, 49, 0, 360);
		g.setColor(Color.BLACK);
		g.drawArc(x, y, 50, 50, 0, 360);
		g.drawRect(x, y, 50, 50);
	}
}