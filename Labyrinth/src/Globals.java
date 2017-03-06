import java.awt.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Globals
{
	public final static int SPACESIZE = 93;
	public final static int SIZE = SPACESIZE / 3;
		// Colors
	public final static Color BROWN = new Color(151, 84, 23);
	public final static Color BLUE = Color.BLUE;
	public final static Color RED = Color.RED;
	public final static Color WHITE = Color.WHITE;
	public final static Color GRAY = Color.GRAY;
	public final static Color BLACK = Color.BLACK;
	public final static Color DEFAULTCOLOR = new Color(171, 174, 117);
		// Tokens
	public static String[] TOKENS = new String[22];
	public static ArrayList<Token> store = new ArrayList<Token>();
		// Held space
	public static Space hold;
		// Gameplay variables
	public static int tokenNum;
	public static int turn;
	public static int phase;

	public static void init()
	{
			// Make a space on hold to replace later
		boolean north = false, south = false, east = false, west = false;
		for (int numWalls = (int)(Math.random() * 2) + 1; numWalls > 0; numWalls--)
			switch ((int)(Math.random() * 4))
			{
				case 0: north = true; break;
				case 1: south = true; break;
				case 2: east = true; break;
				case 3: west = true; break;
			}
		hold = new Space(north, south, east, west);

			// Token descriptions
		TOKENS[0] = "null-null";
		TOKENS[1] = "CRAB APPLES-Crab apples were used to combat fever.\nTheir juice mixed with brandy was believed to be a love-potion.";
		TOKENS[2] = "PINE CONES-Pine cones were placed under children's pillows to help them sleep.";
		TOKENS[3] = "OAK LEAVES-Oak leaves were put inside shoes to protect the feet on long walks.";
		TOKENS[4] = "BLACK SLUGS-Oil from black slugs was used to heal wounds.";
		TOKENS[5] = "FOUR LEAFED CLOVER-A four-leaf clover brought you good luck, but only if it was found in the dark.\nThe best place to keep it was in a small bag with beeswax.";
		TOKENS[6] = "GARLIC-Eating garlic was thought to prevent injuries.\nString of garlic hung from the ceiling would keep sickness away.";
		TOKENS[7] = "RAVEN-Finding a raven's feather was good luck.";
		TOKENS[8] = "HENBANE-Henbane was believed to give witches the ability to fly and to turn themselves into animals.";
		TOKENS[9] = "SPIDERS-Spiders were poisonous, but it brought good luck if on ran over your hand.\nCertain garden spiders were believed to have the power to turn themselves into gold if they were caught.";
		TOKENS[10] = "SKULL MOSS-Skull moss was moss found growing on human skulls.\nIt was thought to be a treatment for epilepsy.";
		TOKENS[11] = "BLINDWORM-A magic wand could be made from a kind of a snake called a blindworm, if it was killed and dried in a chimney flue.";
		TOKENS[12] = "QUARTZ-Quartz crystal protected one from witches' magic.\nWhen crushed into a powder, it helped prevent nausea.";
		TOKENS[13] = "TOADS-Toads were poisonous unless they were properly perpared.\nA meal of toads was thought to allow you to understand the lanuage of animals.";
		TOKENS[14] = "FIRE SALAMANDERS-Fire salamanders could pput out fires and also protect you against burns.";
		TOKENS[15] = "WEASEL-If a weasel spit at you, you might break in two or go blind, but bones of a white weasel could be used to make yourself invisible.";
		TOKENS[16] = "SILVER THISTLES-A silver thistle found in the night of the summer solstice could make you strong.";
		TOKENS[17] = "SNAKE-If you ate a snake, you could understand the language of the animals.";
		TOKENS[18] = "EMERALD-Emeralds protected against epilepsy, fever, and poison.\nThey also improved your sight and your memory, but evil deeds could make the stone shatter.";
		TOKENS[19] = "MANDRAKE-The root of a mandrake looks like a little person.\nIt brought luck in love and in games.";
		TOKENS[20] = "BLACK ROOSTER-A black rooster could change itself into a mythical creature called a basilisk.\nA basilisk could kill you just by looking at you.";
		TOKENS[21] = "MISTLETOE-The berries of the evergreen mistletoe are poisonous, but they were tought to help the metabolism and protect against hardening of the arteries, when taken in the right amount.\nMistletoe was also believed to protect against lightning, fire, ghosts, and evil magic.";

			// Put each token into an ArrayList to ensure no repeats when placing
		for (int n = 0; n < TOKENS.length; n++)
			store.add(new Token(n));

			// Init tokenNum
		tokenNum = 1;
		turn = 0;
		phase = 0;
	}

	public static void advancePhase(Player[] players)
	{
		phase++;
		if (phase == 3)
		{
			phase = 0;
			advanceTurn(players);
		}
		else if (phase == 4)
		{
			for (Player p : players)
				score (p);
		}

	}
	public static void advanceTurn(Player[] players)
	{
		turn ++;
		if(turn == players.length)
			turn = 0;

		JOptionPane.showMessageDialog(null, "It is now the turn of " + players[turn].getName());
	}
	public static void advanceTokenNum()
	{
		if (tokenNum == 25)
			phase = 3;
		tokenNum++;
		if (tokenNum == 21)
			tokenNum = 25;
	}
	public static boolean useWand(Player player)
	{
		if (!player.useWand())
			return false;

		phase = 0;

		return true;
	}
	private static void score(Player p){
		JOptionPane.showMessageDialog(null, p.getName() + ", your score is: " + p.score());
	}
}