import java.text.*;

public class Investor {
	String name;
	double balance;
	Stock[] market;
	int[] shares;
	double[] costBasis;
	boolean[] bought;

	public Investor (String str, Stock[] mkt) {
		name = str;
		balance = 3000.00;
		market = mkt;
		shares = new int[20];
		costBasis = new double[20];
		bought = new boolean[20];
	}

	public boolean buy (int stockIndex, int num) {
			// cannot buy on margin
		if (balance < 0)
			return false;

		shares[stockIndex] += num; // inc the number of shares

			// dec balance
			// If need a loan
		if (balance - market[stockIndex].getPrice()*num < 0) {
			double loan = (market[stockIndex].getPrice()*num - balance) * 1.05;
			balance = loan * -1;
		}
		else
			balance -= market[stockIndex].getPrice()*num;

		costBasis[stockIndex] = market[stockIndex].getPrice(); // set costBasis

		bought[stockIndex] = true; // set bought to true
		return true;
	}
	public boolean sell (int stockIndex, int num) {
		if (shares[stockIndex] - num < 0) // cannot have less than 0 shares
			return false;
		shares[stockIndex] -= num; // dec the number of stocks
		if (shares[stockIndex] == 0) { // if all stocks are sold then reset costBasis
			costBasis[stockIndex] = 0.0;
		}
		balance += market[stockIndex].getPrice()*num; // MORE MONEY!
		return true;
	}
	public void update () {
			// Reset bought[]
		for (int i = 0; i < 20; i++) {
			bought[i] = false;
		}

			// If in debt, pay interest
		if (balance < 0)
			balance *= 1.05;
	}

	public String getName() {
		return name;
	}

	public double getBalance () {
		return balance;
	}
	public String getBalanceString () {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		double tempBalance = balance;
		String moneyString = "";
		if (tempBalance < 0) {
			tempBalance *= -1;
			moneyString = "-";
		}
		moneyString += formatter.format(tempBalance);

		return moneyString;
	}

	public int getShares (int i) {
		return shares[i];
	}

	public double getCostBasis (int i) {
		return costBasis[i];
	}
	public String getCostBasisString (int i) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		String moneyString = formatter.format(costBasis[i]);

		return moneyString;
	}

	public double getNetWorth () {
		double result = balance;
		for (int i = 0; i < market.length; i++) {
			result += shares[i] * market[i].getPrice();
		}

		return result;
	}
	public String getNetWorthString () {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		double netWorth = getNetWorth();
		String moneyString = "";
		if (netWorth < 0) {
			netWorth *= -1;
			moneyString = "-";
		}
		moneyString += formatter.format(netWorth);

		return moneyString;
	}

	public boolean getBought (int i) /* bought shares on this turn? (reset to false on ADVANCE)*/ {
		return bought[i];
	}
}