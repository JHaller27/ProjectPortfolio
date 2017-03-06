public class Token
{
	int id;
	String desc;
	String name;

	public Token(int idin)
	{
		id = idin;
		String str = Globals.TOKENS[id];
		name = str.substring(0, str.indexOf("-"));
		desc = str.substring(str.indexOf("-") + 1);
		if (idin == 21)
			id = 25;
	}

	public int getId(){
		return id;
	}
	public String getDesc(){
		return desc;
	}
	public String getName(){
		return name;
	}
}