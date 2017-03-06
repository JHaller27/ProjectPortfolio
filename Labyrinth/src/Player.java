import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.*;

public class Player
{
	ArrayList<Token> tokens;	// Scored by id
	int wands;	// Scored 3pts ea
	int[] formula;	// Scored 20 pts ea matching token (replace score by id)
	Color color;
	String name;

	public Player (String str, Color c)
	{
		tokens = new ArrayList<Token>();
		wands = 3;
		color = c;
		name = str;

			/* generate formula from:
					1-10-13		2-8-17		3-18-1
					4-13-20		5-25-18		6-14-8
					7-6-25		8-19-5		9-20-11
					10-12-16	11-3-14		12-1-9
					13-15-12	14-4-10		15-2-4
					16-9-7		17-5-6		18-11-19
					19-7-15		20-17-3		25-16-2
			*/
		formula = new int[3];

		formula[0] = (int)(Math.random() * 21) + 1;
		switch (formula[0])
		{
			case 1: formula[1] = 10; formula[2] = 13; break;
			case 2: formula[1] = 8; formula[2] = 17; break;
			case 3: formula[1] = 18; formula[2] = 1; break;
			case 4: formula[1] = 13; formula[2] = 20; break;
			case 5: formula[1] = 25; formula[2] = 18; break;
			case 6: formula[1] = 14; formula[2] = 8; break;
			case 7: formula[1] = 6; formula[2] = 25; break;
			case 8: formula[1] = 19; formula[2] = 5; break;
			case 9: formula[1] = 20; formula[2] = 11; break;
			case 10: formula[1] = 12; formula[2] = 16; break;
			case 11: formula[1] = 3; formula[2] = 14; break;
			case 12: formula[1] = 1; formula[2] = 9; break;
			case 13: formula[1] = 15; formula[2] = 12; break;
			case 14: formula[1] = 4; formula[2] = 10; break;
			case 15: formula[1] = 2; formula[2] = 4; break;
			case 16: formula[1] = 9; formula[2] = 7; break;
			case 17: formula[1] = 5; formula[2] = 6; break;
			case 18: formula[1] = 11; formula[2] = 19; break;
			case 19: formula[1] = 7; formula[2] = 15; break;
			case 20: formula[1] = 17; formula[2] = 3; break;
			case 21: formula[0] = 25; formula[1] = 16; formula[2] = 2; break;
		}
	}

	public Color getColor(){
		return color;
	}

	public String getName()
	{
		String c = "error";

		if (color == Globals.RED)
			c = "Red";
		else if (color == Globals.BLUE)
			c = "Blue";
		else if (color == Globals.WHITE)
			c = "White";
		else if (color == Globals.BROWN)
			c = "Brown";

		return name + " the " + c;
	}

	public void addToken(Token t)
	{
		tokens.add(t);
		if (Globals.tokenNum != 26)
			JOptionPane.showMessageDialog(null, getName() + " found " + t.getName() + "!\n\n" + t.getDesc() + "\nThe next ingredient is number " + Globals.tokenNum);
	}

	public String getFormula()
	{
		for (int i = 0; i <= 2; i++)
			if (formula[i] == 25)
				formula[i] = 21;

		String str =	formula[0] + " -- " + Globals.TOKENS[formula[0]].substring(0, Globals.TOKENS[formula[0]].indexOf("-")) + "\n" +
						formula[1] + " -- " + Globals.TOKENS[formula[1]].substring(0, Globals.TOKENS[formula[1]].indexOf("-")) + "\n" +
						formula[2] + " -- " + Globals.TOKENS[formula[2]].substring(0, Globals.TOKENS[formula[2]].indexOf("-"));

		for (int i = 0; i <= 2; i++)
			if (formula[i] == 21)
				formula[i] = 25;

		return str;
	}

	public boolean useWand()
	{
		if (wands > 0)
		{
			wands--;
			return true;
		}

		return false;
	}

	public int getWands(){
		return wands;
	}

	public int score()
	{
		int score = 0;

			// score wands
		score += wands * 3;

			// score ingredients that are part of the formula
		for (int formulaId : formula)
		{
			for (int tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++)
			{
				if (tokens.get(tokenIndex).getId() == formulaId)
				{
					tokens.remove(tokenIndex);
					score += 20;
					tokenIndex--;
				}
			}
		}

			// score other ingredients
		for (Token ingredient : tokens)
			score += ingredient.getId();

		return score;
	}
}