import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;

public class Stock_Game extends Applet implements ActionListener {
		// Misc. variables
	Stock[] market;
	int weekNum;
	int turn;
	int bias; // -1 = bear, 0 = random, 1 = bull
	Font font;
		// advanceButton
	Button advanceButton = new Button("ADVANCE");
		// All buy/sell stock buttons
	Button[] incButtons = new Button[20];
	Button[] decButtons = new Button[20];
	Button[] decMaxButtons = new Button[20];
		// Quantity button
	int quantity = 1;
	Button quantityButton = new Button("QUANTITY");
		// Investors
	Investor[] investors;

	public static void main(String[] args)
	{
		// create and set up the applet
		Stock_Game applet = new Stock_Game();
		Dimension d = new Dimension(1000, 1000);
		applet.setMinimumSize(d);
		applet.setPreferredSize(d);
		applet.init();

		// create a frame to host the applet, which is just another type of Swing Component
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// add the applet to the frame and show it
		mainFrame.getContentPane().add(applet);
		mainFrame.pack();
		mainFrame.setVisible(true);

		// start the applet
		applet.start();
	}

	public void init () {
			// Font
		font = new Font("Courier New", Font.BOLD, 18);
		this.setFont(font);

			// Fill market
		market = new Stock[20];
		for (int i = 0; i < market.length; i++) {
			market[i]=new Stock();
		}

			// Name/create Investors
			// Type '+' before number for continuous bull market OR '-' bear market OR '' for random
		String input = JOptionPane.showInputDialog("How many players?");
		switch (input.charAt(0)) {
			case '+': bias = 1; input = input.substring(1); break;
			case '-': bias = -1; input = input.substring(1); break;
			default: bias = 0; break;
		}
		investors = new Investor[Integer.parseInt(input)];
		for (int i = 0; i < investors.length; i++) {
			investors[i] = new Investor(JOptionPane.showInputDialog("Input your name"), market);
		}
		turn = 0;

			// Add advanceButton
		advanceButton.addActionListener(this);
		advanceButton.setBackground(Color.BLUE);
		add(advanceButton);

			// Add quantityButton
		quantityButton.addActionListener(this);
		quantityButton.setBackground(Color.LIGHT_GRAY);
		add(quantityButton);

			// Fill incButtons/decButtons/decMaxButtons arrays
		for (int i = 0; i < 20; i++) {
			incButtons[i] = new Button("BUY");
			decButtons[i] = new Button("SELL");
			decMaxButtons[i] = new Button("SELL ALL");
		}

		for (int i = 0; i < 20; i++) {
				// Add BUY Buttons
			incButtons[i].addActionListener(this);
			incButtons[i].setBackground(Color.GRAY);
			add(incButtons[i]);

				// Add SELL Buttons
			decButtons[i].addActionListener(this);
			decButtons[i].setBackground(Color.RED);
			add(decButtons[i]);

				// Add SELL ALL Buttons
			decMaxButtons[i].addActionListener(this);
			decMaxButtons[i].setBackground(Color.RED);
			add(decMaxButtons[i]);
		}

		weekNum = 1;
	}

	public void paint(Graphics g) {
			// Position quantityButton / text
		quantityButton.setLocation(0, 0);
		g.drawString("" + quantity, 115, 20);

			// Position advanceButton
		advanceButton.setLocation(150, 0);

			// Display the week, player's name, and player's current balance/net worth
		g.drawString("Week " + weekNum + " Turn: " + investors[turn].getName() + " :: " + investors[turn].getBalanceString() + " (Net: " + investors[turn].getNetWorthString() + ")", 270, 20);

		for (int i = 0; i < 20; i++) {
				// Display the stock market
			g.drawString("" + market[i].getName(), 270, i*30 + 50);
				// If costBasis > 0 then display it w/ getPriceString()
			g.drawString("........" + market[i].getPriceString(), 315, i*30 + 50);
			if (investors[turn].getCostBasis(i) > 0.0)
				g.drawString(" (Cost Basis: " + investors[turn].getCostBasisString(i) + ")", 490, i*30 + 50);

				// Display Investor's number of shares
			for (int n = 0; n < market.length; n++) {
				g.drawString("" + investors[turn].getShares(n), 170, n*30 + 53);
			}

				// Set Color of BUY Buttons
			if (investors[turn].getShares(i) == 0 || investors[turn].getBought(i))
				incButtons[i].setBackground(Color.GREEN);
			else
				incButtons[i].setBackground(Color.GRAY);

				// Position BUY/SELL/SELL ALL Buttons
			incButtons[i].setLocation(205, i*30 + 35); // BUY Buttons
			decButtons[i].setLocation(100, i*30 + 35); // SELL Buttons
			decMaxButtons[i].setLocation(0, i*30 + 35); // SELL ALL Buttons
		}
	}

	private void changeWeek() {

		int chance = 50 + bias*25;
		if ((int)(Math.random()*100) >= chance)
			for (Stock s : market)
					s.dec();
			else
				for (Stock s : market)
					s.inc();

		weekNum++;
		turn = 0;
			// reset bought[] to all false for all Investors and pay interest if in debt
		for (Investor i : investors) {
			i.update();
		}
		repaint();
	}

	public void actionPerformed(ActionEvent ae)
	{
			// advanceButton
		if (ae.getSource() == advanceButton) {
			turn++;
			if (turn == investors.length) {
				changeWeek();
			}
			repaint();
			return;
		}
			// quantityButton
		if (ae.getSource() == quantityButton) {
			switch (quantity) {
				case 1: quantity = 2; break;
				case 2: quantity = 5; break;
				case 5: quantity = 10; break;
				case 10: quantity = 100; break;
				default: quantity = 1; break;
			}
		}

			// BUY/SELL/SELL ALL Button
		for (int i = 0; i < 20; i++) {
			if (ae.getSource() == incButtons[i] && (investors[turn].getShares(i) == 0 || investors[turn].getBought(i))) {
				investors[turn].buy(i, quantity);
			}
			if (ae.getSource() == decButtons[i])
				investors[turn].sell(i, quantity);
			if (ae.getSource() == decMaxButtons[i])
				investors[turn].sell(i, investors[turn].getShares(i));
		}

		repaint();
	}
}