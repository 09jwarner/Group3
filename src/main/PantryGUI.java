package main;
/** File: 		PantryGUI.java
 ** Author: 	Group 3 Heahter, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class contains the main method and its constructor contains
 **				all the buttons to create the GUI for the user to manage their 
 **				inventory.
 **/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.Border;

public class PantryGUI extends JFrame {
	
	private static final int LOW_INV = 400;

	static final int W = 600, H = 500;
	
	// Create radio buttons
	private JButton addBtn = new JButton("Add Item");
	private JButton deleteBtn = new JButton("Remove Item");
	private JButton updateBtn = new JButton("Update Item");
	private JButton showBtn = new JButton("Show Current Inventory");
	
	//Alert Buttons
	private JButton expireBtn = new JButton("Show Expired Items");
	private JTextField expireAlert = new JTextField(20);
	private JButton lowInvBtn = new JButton("Show Low Inventory");
	private JTextField lowInvAlert = new JTextField(20);

	// Create exit text buttons
	private JButton exitBtn = new JButton("Exit Program");
	private String sql;
	private String reportName;
	
	/**
	 * GUIShape constructor
	 * @throws SQLException 
	 */
	public PantryGUI() throws SQLException {
		Connection con = openDatabase();
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
		alertPanel.setLayout(new GridLayout(3, 2, 40, 20));
		alertPanel.setPreferredSize(new Dimension(150, 75));
		alertPanel.setBorder(BorderFactory.createTitledBorder("Alerts:"));
		expireBtn = new JButton("");
		lowInvAlert.setEditable(false);
		lowInvBtn.setSize(10,10);
		Border border = BorderFactory.createLineBorder(Color.RED);

		sql = "SELECT count(*) from Breads WHERE BakeTemp > " + LOW_INV;
		if (CheckLowInventory.checkInv(con, sql)) {
			lowInvAlert.setFont(new Font("Dialog", Font.BOLD, 16));
			lowInvAlert.setText("Low Inventory");
			lowInvAlert.setBackground(Color.RED);
			lowInvBtn = new JButton("Click to See Low Inventory");
			
			alertPanel.add(lowInvAlert);
			alertPanel.add(lowInvBtn);
		} else {
			border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
			lowInvAlert.setBorder(border);
			lowInvAlert.setText("Inventory all Good!");
			lowInvAlert.setSize(10,15);
			alertPanel.add(lowInvAlert);
		}
		expireAlert.setEditable(false);
		
		expireAlert.setText("No Expired Goods");
		expireAlert.setSize(20, 10);
		expireBtn.setVisible(false);
		alertPanel.add(expireAlert);
		alertPanel.add(expireBtn);
		
		
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
				JOptionPane jPane = new JOptionPane();
				AddItem.addInvItem(con);
				JOptionPane.showMessageDialog(jPane, "Item successfully added!");
			}
		});
		
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Delete an Item!");
			}
		});
		
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Update an Item!");
			}
		});

		showBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Show Total Inventory!");
				sql = "SELECT * from Breads";
				reportName = "Inventory Report";
				InventoryReport.displayInvReport(con, sql, reportName);
				}
		});
		
		expireBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Show Expired Inventory!");
			}
		});
		
		lowInvBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				sql = "SELECT * from Breads WHERE BakeTemp > " + LOW_INV;
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
	
	private Connection openDatabase() {
        String host = "jdbc:mysql://mysqltrial.c0gfjylwc1ro.us-east-2.rds.amazonaws.com:3306/mydb";
        String uname = "admin";
        String password = "pineapple";
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