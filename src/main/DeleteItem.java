package main;

/** File: 		DeleteItem.java
 ** Author: 	Group 3 Heather, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class allows the user to enter an item for deletion from the table.
 *				Once the item name is entered it retrieves item data to give user one last look before
 *				deleting it.
 *
 ** Revisions:
 *  1.0		09/21/2021		MC			Coded based on pseudocode 
 *	1.1		09/27/2021		MC			Added graceful error handling, added cancel buttons to pop ups and messages
 *										to handle them, Added comments and javadocs
 *
 *
 **/

import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DeleteItem {

	/**
	 * deleteInvItem:	Allows user to enter an item name to delete item data.
	 * @param con:		connection
	 */
	public static void deleteInvItem(Connection con) {
		JTextField nameTxt = new JTextField(20);
		JLabel nameLbl= new JLabel();
		JPanel addPanel = new JPanel();
		addPanel.setPreferredSize(new Dimension(350, 100));

		String[] options = {"Retrieve Item Data to Delete","Cancel"};
		int n = 0;
		
		String itemName;
		
		nameLbl = new JLabel("Enter Item Name: ");
		addPanel.add(nameLbl);
		addPanel.add(nameTxt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Delete Item:"));
		
		String name = "Delete Item";
		n = enterInput(name, addPanel, options);
		if (n == 0) {
			try {
				//error check test field
				itemName = nameTxt.getText();
				if(itemName.length() > 30) {
						JOptionPane jf = new JOptionPane();
						JOptionPane.showMessageDialog(jf, "Item Name's length is grearter than the allowed character lenght.");
				}
				else
					retrieveItemData(con, itemName);
			} catch (Exception e) {
				System.out.println(e);
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Error processing request. Please try again.");
			}
		}
	}

	/**
	 * enterInput: The JoptionPane that is displayed for user to enter input
	 * @param name: The name of the item to delete
	 * @param panel: Panel storing text fields and labels
	 * @param options: The retrieve/cancel buttons on bottom of panel
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
	 * 						min quantity and max quantity.
	 * @param con:			connection
	 * @param name:			item name
	 */
	public static void retrieveItemData(Connection con, String name) {
		JTextField del1Txt = new JTextField(10);
		del1Txt.setEditable(false);
		JTextField del2Txt = new JTextField(10);
		del2Txt.setEditable(false);
		JTextField del3Txt = new JTextField(10);
		del3Txt.setEditable(false);
		JTextField del4Txt = new JTextField(10);
		del4Txt.setEditable(false);
		JTextField del5Txt = new JTextField(10);
		del5Txt.setEditable(false);
		
		String[] options = {"Delete","Cancel"};
		
		JLabel del1Lbl, del2Lbl, del3Lbl, del4Lbl, del5Lbl; 
		JPanel delPanel = new JPanel();
		delPanel.setLayout(new GridLayout(5, 1, 50, 20));
		delPanel.setPreferredSize(new Dimension(500, 250));

		del1Lbl = new JLabel("Item Name: ");
		delPanel.add(del1Lbl);
		delPanel.add(del1Txt);
		del2Lbl = new JLabel("Expiration Date(YYYY-MM-DD): ");
		delPanel.add(del2Lbl);
		delPanel.add(del2Txt);
		del3Lbl = new JLabel("Quantity: ");
		delPanel.add(del3Lbl);
		delPanel.add(del3Txt);
		del4Lbl = new JLabel("Min Qty: ");
		delPanel.add(del4Lbl);
		delPanel.add(del4Txt);
		del5Lbl = new JLabel("Max Qty: ");
		delPanel.add(del5Lbl);
		delPanel.add(del5Txt);
		
		String sql = "SELECT * from Inventory WHERE item_name = " + "'" + name + "';";
		Statement stmt;
		String itemName = null;
		try {
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				itemName = rs.getString("item_name");
				del1Txt.setText(rs.getString("item_name"));
				del2Txt.setText(rs.getDate("exp_date").toString());
				del3Txt.setText(Integer.toString(rs.getInt("qty_in_stock")));
				del4Txt.setText(Integer.toString(rs.getInt("min_qty")));
				del5Txt.setText(Integer.toString(rs.getInt("max_qty")));
			}
			if ((itemName != null) && (!itemName.isEmpty())) {
				String rptName = "Item Data";
				int n = JOptionPane.showOptionDialog(null, delPanel, rptName, JOptionPane.NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (n == 0) {
					deleteFromDatabase(con, name);
				}
			} else {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Item is not in table.");
			}

		} catch (SQLException e) {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Error processing your request. Please try again.");
		}

	}

	/**
	 * deleteFromDatabase:	Deletes the item data based on what was entered in texts fields and calls the database
	 * 						with an update sql call to delete the row.
	 * @param con:			connection
	 * @param itemName:		item name
	 */
	private static void deleteFromDatabase(Connection con, String itemName) {
		String sql = "DELETE from Inventory WHERE item_name = " + "'" + itemName + "'";
		PreparedStatement stmt;

		try {
			stmt = con.prepareStatement(sql);
			int rs = stmt.executeUpdate();
			if (rs == 1) {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Successfully deleted");
			} else {
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Could not delete item");
			}
		} catch (SQLException e) {
			JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Error processing your request. Please try again.");
		}
	}
	
}
