package main;

/** File: 		PantryGUI.java
 ** Author: 	Group 3 Heather, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class contains the main method and its constructor contains
 **				all the buttons to create the GUI for the user to manage their 
 **				inventory.
 ** Revisions:
 *	1.0		09/21/2021		MC			Started coding based on pseudocode
 *	1.1		09/24/2021 		Heather		Updated connection information and updated table name and column names 	 
 *  1.2		09/25/2021		John		Add password masking
 *  1.3		09/27/2021		MC			Added 5 login attempts, added graceful error handling, updated dimensions
 *										to make gui cleaner looking, changed to preparedstement instead of statement,
 *										added comments and javadocs
 *	1.4		09/29/2021		MC			Changed login to 3x, added text to alert buttons, changed title to Food Truck
 *
 *
 **/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

public class PantryGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int DAYS_TO_ADD_EXPIRED = 5;

	static final int W = 600, H = 500;

	// Create buttons
	private JButton addBtn = new JButton("Add Item");
	private JButton deleteBtn = new JButton("Remove Item");
	private JButton updateBtn = new JButton("Update Item");
	private JButton showBtn = new JButton("Show Current Inventory");

	// Alert Buttons
	private JButton expiredBtn = new JButton("");
	private JTextField expiredAlert = new JTextField(20);
	private JButton lowInvBtn = new JButton("");
	private JTextField lowInvAlert = new JTextField(20);

	// Create exit text buttons
	private JButton exitBtn = new JButton("Exit Program");

	private String sql;
	private String reportName;
	private LocalDate date = LocalDate.now();
	private Date datePlusDays = Date.valueOf(date.plusDays(DAYS_TO_ADD_EXPIRED));

	/**
	 * PantryGUI constructor
	 */
	public PantryGUI() {
		Connection con = getCredentials();

		if (con != null) {
			setTitle("Group 3 Food Truck Inventory");
			setSize(W, H);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new BorderLayout());
			setBackground(Color.lightGray);

			// Main Panel
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			// Add buttons to the buttons panel
			JPanel buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new GridLayout(3, 2, 40, 20));
			buttonsPanel.setPreferredSize(new Dimension(150, 75));
			buttonsPanel.setBorder(BorderFactory.createTitledBorder("Food Truck Actions:"));
			buttonsPanel.add(showBtn);
			buttonsPanel.add(addBtn);
			buttonsPanel.add(deleteBtn);
			buttonsPanel.add(updateBtn);

			// Alert Main Panel
			JPanel alertPanel = new JPanel();
			alertPanel.setLayout(new GridLayout(2, 1, 40, 20));
			alertPanel.setPreferredSize(new Dimension(150, 80));
			alertPanel.setBorder(BorderFactory.createTitledBorder("Alerts:"));

			// Create panel for Low Inventory Alerts
			JPanel lowInvAlertPanel = new JPanel();
			lowInvAlertPanel.setLayout(new GridLayout(1, 2, 40, 20));
			lowInvAlertPanel.setPreferredSize(new Dimension(150, 15));
			lowInvAlertPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
					"Low Quantity Inventory Alerts:"));

			// Low Inventory Check
			lowInvAlert.setEditable(false);
			lowInvBtn.setSize(10, 10);
			checkLowInvAlert(con, lowInvAlertPanel);
			lowInvAlertPanel.add(lowInvAlert);
			lowInvAlertPanel.add(lowInvBtn);

			// Create panel for Expired Alerts
			JPanel expiredAlertPanel = new JPanel();
			expiredAlertPanel.setLayout(new GridLayout(1, 2, 40, 20));
			expiredAlertPanel.setPreferredSize(new Dimension(150, 15));
			expiredAlertPanel.setBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Expired Inventory Alerts:"));

			// Expired Check
			expiredAlert.setEditable(false);
			expiredBtn.setSize(10, 10);
			checkExpired(con, expiredAlertPanel);
			expiredAlertPanel.add(expiredAlert);
			expiredAlertPanel.add(expiredBtn);

			// Create panel for exit button
			JPanel exitPanel = new JPanel();
			exitPanel.setPreferredSize(new Dimension(75, 10));
			exitPanel.setBorder(BorderFactory.createTitledBorder("Exit Program:"));
			exitPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			exitBtn.setPreferredSize(new Dimension(125, 25));
			exitPanel.add(exitBtn);

			// Add Everything to the Main Panel
			alertPanel.add(lowInvAlertPanel);
			alertPanel.add(expiredAlertPanel);
			mainPanel.add(buttonsPanel);
			mainPanel.add(alertPanel);
			mainPanel.add(exitPanel);
			setContentPane(mainPanel);

			addBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					AddItem.addInvItem(con);
					refreshAlerts(con, lowInvAlertPanel, expiredAlertPanel);
				}
			});

			deleteBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					DeleteItem.deleteInvItem(con);
					refreshAlerts(con, lowInvAlertPanel, expiredAlertPanel);
				}
			});

			updateBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					UpdateItem.updateInvItem(con);
					refreshAlerts(con, lowInvAlertPanel, expiredAlertPanel);
				}
			});

			showBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					sql = "SELECT * from Inventory";
					reportName = "Inventory Report";
					InventoryReport.displayInvReport(con, sql, reportName);
				}
			});

			expiredBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					sql = "SELECT * from Inventory WHERE exp_date <= '" + datePlusDays + "'";
					reportName = "Expired Inventory";
					InventoryReport.displayInvReport(con, sql, reportName);
				}
			});

			lowInvBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					sql = "SELECT * from Inventory WHERE qty_in_stock <= min_qty";
					reportName = "Low Inventory";
					InventoryReport.displayInvReport(con, sql, reportName);
				}
			});

			// Listener method for exit button
			exitBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					exitBtnAction(event, con);
				}
			});

		}
	}

	/**
	 * checkExpired: Checks to see if there is inventory that has expired or will
	 * expire in the next 5 days.
	 * @param con:        connection
	 * @param alertPanel: alert main panel
	 */
	private void checkExpired(Connection con, JPanel alertPanel) {
		sql = "SELECT count(*) from Inventory WHERE exp_date <= '" + datePlusDays + "'";
		if (con != null) {
			try {
				if (CheckForAlerts.checkExpiredItems(con, sql)) {
					expiredAlert.setFont(new Font("Dialog", Font.BOLD, 14));
					expiredAlert.setText("Expired Items Alert");
					expiredAlert.setBackground(Color.RED);
					expiredBtn.setText("Click to See Expired Items");
				} else {
					expiredAlert.setBackground(null);
					expiredAlert.setText("No Expired Items!");
					expiredBtn.setText("Click to See Expired Items");
				}
			} catch (SQLException e) {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Cannot open database to get expired inventory.");
			}
		}
	}

	/**
	 * checkLowInvAlert: Checks to see if there is inventory with qty_in_stock that
	 * is less than or equal to min_qty needed to be kept on hand.
	 * @param con:        connection
	 * @param alertPanel: alert main panel
	 */
	private void checkLowInvAlert(Connection con, JPanel alertPanel) {
		sql = "SELECT count(*) from Inventory WHERE qty_in_stock <= min_qty";
		if (con != null) {
			try {
				if (CheckForAlerts.checkLowInv(con, sql)) {
					lowInvAlert.setFont(new Font("Dialog", Font.BOLD, 14));
					lowInvAlert.setText("Low Inventory Alert");
					lowInvAlert.setBackground(Color.RED);
					lowInvBtn.setText("Click to See Low Inventory");
				} else {
					lowInvAlert.setBackground(null);
					lowInvAlert.setText("Inventory all Good!");
					lowInvBtn.setText("Click to See Low Inventory");
				}
			} catch (SQLException e) {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Cannot open database to get low quantity inventory.");
			}
		}
	}

	/**
	 * refreshAlerts: When inventory has been added, deleted or updated it refreshes
	 * the expired or low inventory alert messages.
	 * @param con:               connection
	 * @param lowInvAlertPanel:  low inventory alert panel
	 * @param expiredAlertPanel: expired alert panel
	 */
	private void refreshAlerts(Connection con, JPanel lowInvAlertPanel, JPanel expiredAlertPanel) {
		lowInvAlertPanel.setBorder(BorderFactory.createTitledBorder("Updated Low Inventory Alerts:"));
		expiredAlertPanel.setBorder(BorderFactory.createTitledBorder("Updated Expired Inventory Alerts:"));
		checkLowInvAlert(con, lowInvAlertPanel);
		checkExpired(con, expiredAlertPanel);
	}

	/**
	 * exitBtnAction: Allows the user to have an exit button to quickly exit the
	 * program and give an exit message.
	 * @param con: connection
	 */
	public void exitBtnAction(ActionEvent e, Connection con) {
		JOptionPane jPane = new JOptionPane();
		JOptionPane.showMessageDialog(jPane, "Thank you for using the program!");
		closeDatabase(con);
		System.exit(EXIT_ON_CLOSE);
	}

	/**
	 * getCredentials: It gets the username and password. It masks the password.
	 * @return con: Connection
	 */
	private Connection getCredentials() {
		int count = 0;
		Connection con = null;

		while (count <= 3) {
			JTextField userTxt = new JTextField(20);
			JLabel userLbl = new JLabel();
			// JTextField pswdTxt = new JTextField(20);
			// Changed to JPasswordField to mask password text.
			JPasswordField pswdTxt = new JPasswordField(20);
			JLabel pswdLbl = new JLabel();
			JPanel panel = new JPanel();
			String[] options = { "OK" };
			userLbl = new JLabel("Enter User Name: ");
			panel.add(userLbl);
			panel.add(userTxt);
			userTxt.requestFocusInWindow();
			pswdLbl = new JLabel("Enter Password: ");
			panel.add(pswdLbl);
			panel.add(pswdTxt);
			int n = JOptionPane.showOptionDialog(null, panel, "Enter Credentials", JOptionPane.NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (n == 0) {
				String user = userTxt.getText();
				// getText() has been deprecated, used character array
				// used the variable String password to pass to openDatabase J. Warner
				char[] pswd = pswdTxt.getPassword();
				String password = new String(pswd);
				// con = openDatabase(user, pswd);
				count = count + 1;
				con = openDatabase(user, password, count);
			} else {
				System.exit(EXIT_ON_CLOSE);
			}
			if (con != null) {
				count = 3;
			}/* else {
				count++;
			}*/
		}

		return con;
	}

	/**
	 * openDatabase: Takes the username and password and tries to open the Truck
	 * database. The user is given 3 attempts to input correct username/password
	 * combo before program closes.
	 * @param user: user name
	 * @param pswd: masked password
	 * @return con: connection
	 */
	private Connection openDatabase(String user, String pswd, int count) {
		String host = "jdbc:mysql://foodtruckapp.c0gfjylwc1ro.us-east-2.rds.amazonaws.com:3306/TruckDatabase";
		String uname = user;
		String password = pswd;
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(host, uname, password);
		} catch (Exception e) {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Your user name or password is incorrect. Please try again.");
			if (count == 2) {
				JOptionPane.showMessageDialog(jf,
						"You have had 2 unsuccessful logins. You have one more attempt to login, before the application closes.");
			}
			if (count == 3) {
				System.exit(EXIT_ON_CLOSE);
			}
		}
		return con;
	}

	/**
	 * closeDatabase: Closes the database
	 * @param con: connection
	 */
	private void closeDatabase(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Cannot close database.");
		}
	}

	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		PantryGUI gui = new PantryGUI();
		gui.setVisible(true);
	}
}