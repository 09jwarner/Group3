package main;
/** File: 		PantryGUI.java
 ** Author: 	Group 3 Heather, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class contains the main method and its constructor contains
 **				all the buttons to create the GUI for the user to manage their 
 **				inventory.
 ** Revisions:
 *	1.1		09/24/2021 		Heather		Updated connection information and updated table name and column names 	 
 *
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
import javax.swing.border.Border;

public class PantryGUI extends JFrame {
	
	private static final int DAYS_TO_ADD_EXPIRED = 5;

	static final int W = 600, H = 500;
	
	// Create radio buttons
	private JButton addBtn = new JButton("Add Item");
	private JButton deleteBtn = new JButton("Remove Item");
	private JButton updateBtn = new JButton("Update Item");
	private JButton showBtn = new JButton("Show Current Inventory");
	
	//Alert Buttons
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
	 * GUIShape constructor
	 * @throws SQLException 
	 */
	public PantryGUI() throws SQLException {
		Connection con = getCredentials();

		setTitle("Group 3 Pantry");
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
		buttonsPanel.setBorder(BorderFactory.createTitledBorder("Pantry Actions:"));
		buttonsPanel.add(showBtn);
		buttonsPanel.add(addBtn);
		buttonsPanel.add(deleteBtn);
		buttonsPanel.add(updateBtn);

		// Create panel for alert
		JPanel alertPanel = new JPanel();
		alertPanel.setLayout(new GridLayout(2, 2, 40, 20));
		alertPanel.setPreferredSize(new Dimension(150, 75));
		alertPanel.setBorder(BorderFactory.createTitledBorder("Alerts:"));
		
		//Low Inventory Check
		lowInvAlert.setEditable(false);
		lowInvBtn.setSize(10,20);		
		checkLowInvAlert(con, alertPanel);
		alertPanel.add(lowInvAlert);
		alertPanel.add(lowInvBtn);
		
		//Expired Check
		checkExpired(con, alertPanel);		
		alertPanel.add(expiredAlert);
		alertPanel.add(expiredBtn);

		// Create panel for exit button
		JPanel exitPanel = new JPanel();
		exitPanel.setPreferredSize(new Dimension(75, 10));
		exitPanel.setBorder(BorderFactory.createTitledBorder("Exit Program:"));
		exitPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		exitBtn.setPreferredSize(new Dimension(125, 25));
		exitPanel.add(exitBtn);

		// Add Everything to the Main Panel
		mainPanel.add(buttonsPanel);
		mainPanel.add(alertPanel);
		mainPanel.add(exitPanel);
		setContentPane(mainPanel);

		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				AddItem.addInvItem(con);
				try {
					refreshAlerts(con, alertPanel, mainPanel);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				DeleteItem.deleteInvItem(con);
				try {
					refreshAlerts(con, alertPanel, mainPanel);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				UpdateItem.updateInvItem(con);
				try {
					refreshAlerts(con, alertPanel, mainPanel);
				} catch (SQLException e) {
					e.printStackTrace();
				}
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


	private void checkExpired(Connection con, JPanel alertPanel) throws SQLException {
		sql = "SELECT count(*) from Inventory WHERE exp_date <= '" + datePlusDays + "'";
		if (CheckForAlerts.checkExpiredItems(con, sql)) {
			expiredAlert.setFont(new Font("Dialog", Font.BOLD, 16));
			expiredAlert.setText("Expired Items Alert");
			expiredAlert.setBackground(Color.RED);
			expiredBtn.setText("Click to See soon to be Expired and Expired Items");			
		} else {
			expiredAlert.setBackground(null);
			expiredAlert.setText("No Expired Items!");
			expiredAlert.setSize(10,15);
			expiredBtn.setText("");
		}
	}


	private void checkLowInvAlert(Connection con, JPanel alertPanel) throws SQLException {
		sql = "SELECT count(*) from Inventory WHERE qty_in_stock <= min_qty";
		if (CheckForAlerts.checkLowInv(con, sql)) {
			lowInvAlert.setFont(new Font("Dialog", Font.BOLD, 16));
			lowInvAlert.setText("Low Inventory Alert");
			lowInvAlert.setBackground(Color.RED);
			lowInvBtn.setText("Click to See Low Inventory");
		} else {
			lowInvAlert.setBackground(null);
			lowInvAlert.setText("Inventory all Good!");
			lowInvBtn.setText("");
		}
	}

	private void refreshAlerts(Connection con, JPanel alertPanel, JPanel mainPanel) throws SQLException {
		alertPanel.setBorder(BorderFactory.createTitledBorder("Updated Alerts:"));
		checkLowInvAlert(con, alertPanel);
		checkExpired(con, alertPanel);
	}
	/**
	 * exitBtnAction: 	Allows the user to have an exit button to 
	 * 					quickly exit the program and give an exit message.
	 * @param e
	 */
	public void exitBtnAction(ActionEvent e, Connection con) {
		JOptionPane jPane = new JOptionPane();
		JOptionPane.showMessageDialog(jPane, "Thank you for using the program!");
		closeDatabase(con);
		System.exit(EXIT_ON_CLOSE);
	}
	
	private Connection getCredentials() {
		JTextField userTxt = new JTextField(20);
		JLabel userLbl= new JLabel();
		JTextField pswdTxt = new JTextField(20);
		JLabel pswdLbl= new JLabel();
		JPanel panel = new JPanel();
		String[] options = {"OK"};
		Connection con = null;
		userLbl = new JLabel("Enter User Name: ");
		panel.add(userLbl);
		panel.add(userTxt);
		pswdLbl = new JLabel("Enter Password: ");
		panel.add(pswdLbl);
		panel.add(pswdTxt);
		int n = JOptionPane.showOptionDialog(null, panel, "Enter Credentials", JOptionPane.NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		if (n == 0) {
			String user = userTxt.getText();
			String pswd = pswdTxt.getText();
			con = openDatabase(user, pswd); 
		}
		return con;
	}
	
	private Connection openDatabase(String user, String pswd) {
        String host = "jdbc:mysql://foodtruckapp.c0gfjylwc1ro.us-east-2.rds.amazonaws.com:3306/TruckDatabase";
        String uname = user;
        String password = pswd;
        Connection con = null;
        try {
         	Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection(host, uname, password);
            
        } catch (Exception e) {
        	System.out.println(e);
        }
        return con;
	}
	
	private void closeDatabase(Connection con) {
		try {
	        con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * main method
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		PantryGUI gui = new PantryGUI();
		gui.setVisible(true);
		
	}
}