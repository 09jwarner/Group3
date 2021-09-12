package main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UpdateItem {

	public static void updateInvItem(Connection con) {
		JTextField nameTxt = new JTextField(20);
		JLabel nameLbl= new JLabel();
		JPanel addPanel = new JPanel();
		addPanel.setPreferredSize(new Dimension(350, 100));

		String[] options = {"Retrieve Item Data"};
		int n = 0;
		
		String itemName;
		
		nameLbl = new JLabel("Enter Item Name: ");
		addPanel.add(nameLbl);
		addPanel.add(nameTxt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Update Item:"));
		

		String name = "Update Item";
		n = enterInput(name, addPanel, options);
		if (n == 0) {
			try {
				itemName = nameTxt.getText();
				retrieveItemData(con, itemName);
			} catch (Exception e) {
				System.out.println(e);
				JOptionPane jf = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "Error:" + e + " Please try again.");
			}
		}
	}

	/**
	 * enterInput: The JoptionPane that is displayed for user to enter input
	 * @param name: The name of the shape
	 * @param panel: Panel storing text fields and labels
	 * @param options: The OK button on bottom of panel
	 * @return integer
	 */
	private static int enterInput(String name, JPanel panel, String[] options) {
		int n;
		n = JOptionPane.showOptionDialog(null, panel, name, JOptionPane.NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		return n;
	}

	public static void retrieveItemData(Connection con, String name) throws SQLException {
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
		
		String[] options = {"update","Cancel"};
		
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
		
		String sql = "SELECT * from mcbfood WHERE item_name = " + "'" + name + "';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		String itemName;
    	Date itemDate = null;
    	int qty = 0;
    	int minQty = 0;
    	int maxQty = 0;
        while (rs.next()) {
        	itemName = rs.getString("item_name");
        	itemDate = rs.getDate("item_date");
        	qty = rs.getInt("amount");
        	minQty = rs.getInt("min_amount");
        	maxQty = rs.getInt("max_amount");
        	
        	update1Txt.setText(itemName);
        	update2Txt.setText(itemDate.toString());
        	update3Txt.setText(Integer.toString(qty));
        	update4Txt.setText(Integer.toString(minQty));
        	update5Txt.setText(Integer.toString(maxQty));
        }	
		String rptName = "Item Data";
		int n = JOptionPane.showOptionDialog(null, updatePanel, rptName, JOptionPane.NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		if (n == 0) {
			Date updateDate = Date.valueOf(update2Txt.getText());
			int updateQty = Integer.parseInt(update3Txt.getText());
			int updateMin = Integer.parseInt(update4Txt.getText());
			int updateMax = Integer.parseInt(update5Txt.getText());
			updateItemDatabase(con, name, updateDate, updateQty, updateMin, updateMax);
		}
	}
	
	private static void updateItemDatabase(Connection con, String itemName, Date itemDate, int qty, int minQty, int maxQty)
			throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "UPDATE mcbfood SET item_date = " + "'" + itemDate + "'" 
        		+ ", amount = " + qty  + ", min_amount = " + minQty 
        		+ ", max_amount = " + maxQty + " WHERE item_name = " + "'" + itemName + "'";
        int rs = stmt.executeUpdate(sql);
        if (rs==1) {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Successfully updated");
        } else {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Could not update item");
        }
	}
}
