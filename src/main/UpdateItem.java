package main;

/** File: 		UpdateItem.java
 ** Author: 	Group 3 Heather, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class allows the user to enter an item for updating in the table.
 *				Once the item name is entered it retrieves item data to allow user to edit
 *				expiration date, quantity, min quantity and max quantity.
 *
 ** Revisions:
 *  1.0		09/21/2021		MC			Coded based on pseudocode 
 *	1.1		09/27/2021		MC			Added graceful error handling, added cancel buttons to pop ups and messages
 *										to handle them, Added comments and javadocs
 *	1.2		09/29/2021		MC			Fixed endless loop; added checks for fields entered and name
 *
 *
 **/

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UpdateItem {

	/**
	 * updateInvItem:	Allows user to enter an item name to update item data.
	 * @param con:		connection
	 */
	public static void updateInvItem(Connection con) {
		JTextField nameTxt = new JTextField(20);
		JLabel nameLbl= new JLabel();
		JPanel addPanel = new JPanel();
		addPanel.setPreferredSize(new Dimension(350, 100));

		String[] options = {"Retrieve Item Data","Cancel"};
		int n = 0;
		
		String itemName;
		
		nameLbl = new JLabel("Enter Item Name: ");
		addPanel.add(nameLbl);
		addPanel.add(nameTxt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Update Item:"));
		

		String name = "Update Item";
		boolean isUpdated = false;
		while (!isUpdated) {
			n = enterInput(name, addPanel, options);
			if (n == 0) {
				try {
					itemName = nameTxt.getText();
					if (checkName(itemName)) {
						isUpdated = retrieveItemData(con, itemName);	
					}
				} catch (Exception e) {
					JOptionPane jf = new JOptionPane();
					JOptionPane.showMessageDialog(jf, "Error processing request. Please try again.");
				}
			} else {
				Window activeWindow = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
				activeWindow.dispose();
				break;
			}
		}
	}

	/**
	 * enterInput: 		The JoptionPane that is displayed for user to enter input
	 * @param name: 	The name of the shape
	 * @param panel: 	Panel storing text fields and labels
	 * @param options: 	The OK button on bottom of panel
	 * @return integer
	 */
	private static int enterInput(String name, JPanel panel, String[] options) {
		int n;
		n = JOptionPane.showOptionDialog(null, panel, name, JOptionPane.NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		return n;
	}

	/**
	 * retrieveItemData:	Retrieves item data for item name entered. Expiration date, quantity in stock,
	 * 						min quantity and max quantity can be editted.
	 * @param con:			connection
	 * @param name:			item name
	 */
	public static boolean retrieveItemData(Connection con, String name) {
		JTextField update1Txt = new JTextField(10);
		update1Txt.setEditable(false);
		JTextField update2Txt = new JTextField(10);
		update2Txt.setEditable(true);
		JTextField update3Txt = new JTextField(10);
		update3Txt.setEditable(true);
		JTextField update4Txt = new JTextField(10);
		update4Txt.setEditable(true);
		JTextField update5Txt = new JTextField(10);
		update5Txt.setEditable(true);
		
		String[] options = {"Update","Cancel"};
		
		JLabel update1Lbl, update2Lbl, update3Lbl, update4Lbl, update5Lbl; 
		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new GridLayout(5, 1, 50, 20));
		updatePanel.setPreferredSize(new Dimension(500, 250));

		update1Lbl = new JLabel("Item Name: ");
		updatePanel.add(update1Lbl);
		updatePanel.add(update1Txt);
		update2Lbl = new JLabel("Expiration Date(YYYY-MM-DD): ");
		updatePanel.add(update2Lbl);
		updatePanel.add(update2Txt);
		update3Lbl = new JLabel("Quantity: ");
		updatePanel.add(update3Lbl);
		updatePanel.add(update3Txt);
		update4Lbl = new JLabel("Min Qty: ");
		updatePanel.add(update4Lbl);
		updatePanel.add(update4Txt);
		update5Lbl = new JLabel("Max Qty: ");
		updatePanel.add(update5Lbl);
		updatePanel.add(update5Txt);
		
		String sql = "SELECT * from Inventory WHERE item_name = " + "'" + name + "';";
		String itemName = null;
		Date itemDate = null;
		int qty = 0;
		int minQty = 0;
		int maxQty = 0;
		boolean updated = false;
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				itemName = rs.getString("item_name");
				itemDate = rs.getDate("exp_date");
				qty = rs.getInt("qty_in_stock");
				minQty = rs.getInt("min_qty");
				maxQty = rs.getInt("max_qty");

				update1Txt.setText(itemName);
				update2Txt.setText(itemDate.toString());
				update3Txt.setText(Integer.toString(qty));
				update4Txt.setText(Integer.toString(minQty));
				update5Txt.setText(Integer.toString(maxQty));
			}
			if ((itemName != null) && (!itemName.isEmpty())) {
				while (!updated) {
					String rptName = "Item Data";
					int n = JOptionPane.showOptionDialog(null, updatePanel, rptName, JOptionPane.NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (n == 0) {
						Date updateDate = Date.valueOf(update2Txt.getText());
						int updateQty = Integer.parseInt(update3Txt.getText());
						int updateMin = Integer.parseInt(update4Txt.getText());
						int updateMax = Integer.parseInt(update5Txt.getText());
						//boolean checkFields for quantity <= 30, date, min <= max 
						if (checkFields(updateQty, updateMin, updateMax, updateDate)) {
							updated = updateItemDatabase(con, name, updateDate, updateQty, updateMin, updateMax);
						}
					} else {
						Window activeWindow = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
						activeWindow.dispose();
						break;	
					}
				}

			} else {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Item is not in table.");
			}
		} catch (SQLException e) {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Error processing your request. Please try again.");
		}
		return updated;
	}
	
	/**
	 * updateItemDatabase:	Updates the item data based on what was entered in texts fields and calls the database
	 * 						with an update sql call.
	 * @param con:			Connection
	 * @param itemName:		Item Name
	 * @param itemDate:		Expiration Date
	 * @param qty:			Quantity in Stock
	 * @param minQty:		Minimum Quantity to keep on hand
	 * @param maxQty:		Maximum Quantity allowed to be stored
	 */
	private static boolean updateItemDatabase(Connection con, String itemName, Date itemDate, int qty, int minQty, int maxQty) {
 		
		String sql = "UPDATE Inventory SET exp_date = ?, qty_in_stock = ?, min_qty = ?, max_qty = ? WHERE item_name = ?";
        PreparedStatement pStmt;
        boolean successfulUpdate = false;

        try {
			pStmt = con.prepareStatement(sql);
			pStmt.setDate(1, itemDate);
			pStmt.setInt(2, qty);
			pStmt.setInt(3, minQty);
			pStmt.setInt(4, maxQty);
			pStmt.setString(5, itemName);
			
			int rs = pStmt.executeUpdate();
			
			// Use execute return value to determine if item was added
			if (rs==1) {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Successfully updated");
				successfulUpdate = true;
	        } else {
	        	JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Could not update item");
	        }
		} catch (Exception e){
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Error processing your request. Please try again.");
		}
        
        return successfulUpdate;
	}
	
	/**
	 * checkFields: checks the fields to make sure that meet criteria
	 * @param updateQty: quantity in stock
	 * @param updateMin: min quantity to be kept on hand
	 * @param updateMax: maximum quantity to be kept on hand
	 * @param updateDate: expiration date
	 * @return boolean
	 */
	private static boolean checkFields(int updateQty, int updateMin, int updateMax, Date updateDate) {
		LocalDate currDate = LocalDate.now();
		LocalDate updateExpDate = updateDate.toLocalDate();
		
		boolean maxQtyCheck = false;
		boolean minMaxCheck = false;
		boolean dateCheck = false;
		boolean checkFields = false;
		
		if ((updateQty > 0 && updateQty <= 30) && (updateMin > 0 && updateMin <= 30) && 
				(updateMax > 0 && updateMax <= 30)) {
			maxQtyCheck = true;
		} else {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Quantities must be <= 30. Please Try Again");	
		}
		
		if (updateMin <= updateMax) {
			minMaxCheck = true;
		} else {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Min Quantity must be less than Max Qunatity.");	
		}
		
		if (updateExpDate.compareTo(currDate) >= 0) {
			dateCheck = true;
		} else {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Expiration Date must be >= Today.");	
		}
		
		if ((maxQtyCheck) && (minMaxCheck) && (dateCheck)) {
			checkFields = true;
		}
		
		return checkFields;
	}
	
	/**
	 * checkName:	checks the name length to make sure <= 30 characters
	 * @param updateNm: name entered
	 * @return boolean
	 */
	private static boolean checkName(String updateNm) {
		boolean nameCheck = false;
		
		if (updateNm.length() <= 30) {
			nameCheck = true;
		} else {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Name length must be <= 30.");	
		}
		
		return nameCheck;
	}
}
