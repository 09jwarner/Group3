package main;

/** File: 		AddItem.java
 ** Author: 	Group 3 Heather, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class contains methods to add items to the inventory table
 **				
 **				
 ** Revisions:
 *	1.0		09/21/2021		Heather		Started code based on pseudocode
 *	1.1		09/25/2021 		Heather		Converted Statement to PreparedStatement, added cancel button, error handling and hiding of exceptions in user popups/user friendly messages
 *	1.2		09/26/2021		John		Added input validation for all user fields
 *	1.3		09/28/2021		Heather		Added Javadocs, comments
 *	1.4		09/29/2021		John		Added check for expiration date older than today's date			
 *
 **/


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddItem {
	
	/**
	 * addInvItem: Constructs the Add GUI, validates user input
	 * @param con: JDBC connection
	 */
	public static void addInvItem(Connection con) {
		JTextField add1Txt = new JTextField(10);
		JLabel add1Lbl= new JLabel();
		JTextField add2Txt = new JTextField(10);
		JLabel add2Lbl= new JLabel();
		JTextField add3Txt = new JTextField(10);
		JLabel add3Lbl= new JLabel();
		JTextField add4Txt = new JTextField(10);
		JLabel add4Lbl= new JLabel();
		JTextField add5Txt = new JTextField(10);
		JLabel add5Lbl= new JLabel();
		JPanel addPanel = new JPanel();
		addPanel.setLayout(new GridLayout(5, 1, 50, 20));
		addPanel.setPreferredSize(new Dimension(500, 250));

		String[] options = {"OK","Cancel"};
		int n = 0;
		
		String inputItem;
		String inputDate;
		Date itemDate;
		int inputQty;
		int inputMin;
		int inputMax;
		
		add1Lbl = new JLabel("Enter Item Name: ");
		addPanel.add(add1Lbl);
		addPanel.add(add1Txt);
		add2Lbl = new JLabel("Enter Date(YYYY-MM-DD): ");
		addPanel.add(add2Lbl);
		addPanel.add(add2Txt);
		add3Lbl = new JLabel("Enter Quantity: ");
		addPanel.add(add3Lbl);
		addPanel.add(add3Txt);
		add4Lbl = new JLabel("Enter Min Quantity: ");
		addPanel.add(add4Lbl);
		addPanel.add(add4Txt);
		add5Lbl = new JLabel("Enter Max Quantity: ");
		addPanel.add(add5Lbl);
		addPanel.add(add5Txt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Add Inventory:"));

		String name = "Add Item";
		// Prompt user until they get input correct
		while (true) {
			n = enterInput(name, addPanel, options);

			if (n == 0) {
				try {
					
					inputItem = add1Txt.getText();
					if (inputItem.length() > 30) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "The item name cannot be greater than 30 characters.");
						break;
					}
					inputDate = add2Txt.getText();
					itemDate = Date.valueOf(inputDate);
					inputQty = Integer.parseInt(add3Txt.getText());
					inputMin = Integer.parseInt(add4Txt.getText());
					inputMax = Integer.parseInt(add5Txt.getText());
					/*
					 * Test inputQty is less than 30 items, greater than minimum quantity 
					 * and less than maximum quantity
					 */
					if (inputQty < inputMin) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "The Quantity cannot be less than the Minimum Quantity.");
						break;
					}
					else if (inputQty > inputMax) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "The Quantity cannot be greater than the Maximum Quantity.");
						break;
					}
					else if (inputQty > 30) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "The Quantity cannot be greater than 30 items.");
						break;
					}
					/*
					 * Test inputMin less than inputMax
					 */
					if (inputMin >= inputMax) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "The Minimum Quantity cannot be equal to or greater than Maximum Quantity.");
						break;
					}
					if (inputMax > 30) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "The Maximum Quantity cannot be greater than 30 items.");
						break;
					}
					/*
					 *  Test for Expiration Date is later than today's date.
					 *  Convert inputDate String to Date type
					 *  Get today's date
					 *  
					 */
					Date updateDate = Date.valueOf(inputDate);
					LocalDate currDate = LocalDate.now();
					LocalDate updateExpDate = updateDate.toLocalDate();
					if (updateExpDate.compareTo(currDate) < 0) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "Expiration Date must be today's date or later.");
						break;
					}
					
					addToDatabase(con, inputItem, itemDate, inputQty, inputMin, inputMax);
					break;
				// Catch for date and number format errors
				} catch (IllegalArgumentException e) {
					JOptionPane jf = new JOptionPane();
					JOptionPane.showMessageDialog(jf, "You have entered an invalid value for one of the fields!\n Quantities must be whole numbers and date must be in YYYY-MM-DD format!");
					continue;
				} catch (SQLException e) {
					JOptionPane jf = new JOptionPane();
					JOptionPane.showMessageDialog(jf, "A connection error has occured. Try again or contact support.");
				}
			// Cancel button
			} else {
				// Get the current window and close it
				Window activeWindow = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
				activeWindow.dispose();
				break;
			}
		}

	}

	/**
	 * enterInput: The JoptionPane that is displayed for user to enter input
	 * @param name: The name of the window
	 * @param panel: Panel storing text fields and labels
	 * @param options: The OK button on bottom of panel
	 * @return integer
	 */
	private static int enterInput(String name, JPanel panel, String[] options) {
		int n;
		n = JOptionPane.showOptionDialog(null, panel, "Add Inventory Item Data: "+name, JOptionPane.NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		return n;
	}

	/**
	 * addToDatabase: Constructs an injection safe SQL statement to insert a new row into the database
	 * @param con
	 * @param itemName: New unique item name
	 * @param itemDate: Expiration Date
	 * @param qty: Quantity on hand
	 * @param minQty: Minimum quantity to keep in stock
	 * @param maxQty: Maximum quantity to keep in stock
	 * @throws SQLException 
	 */	
	private static void addToDatabase(Connection con, String itemName, Date itemDate, int qty, int minQty, int maxQty)
			throws SQLException {
		try {

			String sql = "INSERT INTO Inventory " + "VALUES (?,?,?,?,?)";
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setString(1, itemName);
			pStmt.setDate(2, itemDate);
			pStmt.setInt(3, qty);
			pStmt.setInt(4, minQty);
			pStmt.setInt(5, maxQty);

			int result = pStmt.executeUpdate();

			// Use execute return value to determine if item was added
			if (result == 1) {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Successfully added");
			// Will catch if connection issues
			} else {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Could not add item");
			}
		// Exception if item exists already	
		} catch (SQLIntegrityConstraintViolationException ce) {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf,
					"Item already exists! \nPlease enter a unique item or update the item if it already exists.");
		}
	}
}