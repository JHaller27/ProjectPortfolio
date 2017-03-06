import java.text.*;

public class Stock {
	double price; // per share
	double last; // used to track inc. or dec. in toString()
	String name;

	public Stock () {
		price = (Math.random() * 200);

		name = "";
		for (int n = 0; n < (int)(Math.random() * 3) + 2; n++)
			name += (char)((int)(Math.random()*26 + 65));

		last = price;
	}

	public double getPrice () {
		return price;
	}
	public String getPriceString () {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		String moneyString = formatter.format(price);
		String result = "";
		if (price > last)
			result += "+";
		else if (price < last)
			result += "-";
		else if (price == last)
			result += " ";
		result += moneyString;
		return result;
	}
	public String getName () {
		return name;
	}

	public void inc () {
		last = price;
		boolean prob = ((int)(Math.random()*4)) != 0;

		if (prob)
			price += (Math.random() * (price*.5)) + (price*.01);
		else
			price -= (Math.random() * (price*.2)) + (price*.01);
	}
	public void dec () {
		last = price;
		boolean prob = ((int)(Math.random()*4)) != 0;

		if (prob)
			price -= (Math.random() * (price*.5)) + (price*.01);
		else
			price += (Math.random() * (price*.2)) + (price*.01);
	}

	public String toString () {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		String moneyString = formatter.format(price);
		String result = name + "............";
		if (price > last)
			result += "+";
		else if (price < last)
			result += "-";
		else if (price == last)
			result += " ";
		result += moneyString;
		return result;
	}
}