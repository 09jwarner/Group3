package main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddItem {

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

		String[] options = {"OK"};
		int n = 0;
		
		String input1;
		String input2;
		int input3;
		int input4;
		int input5;  
		
		add1Lbl = new JLabel("Enter Item Name: ");
		addPanel.add(add1Lbl);
		addPanel.add(add1Txt);
		add2Lbl = new JLabel("Enter Date(YYYY-MM-DD): ");
		addPanel.add(add2Lbl);
		addPanel.add(add2Txt);
		add3Lbl = new JLabel("Enter Amount: ");
		addPanel.add(add3Lbl);
		addPanel.add(add3Txt);
		add4Lbl = new JLabel("Enter Min Amount: ");
		addPanel.add(add4Lbl);
		addPanel.add(add4Txt);
		add5Lbl = new JLabel("Enter Max Amount: ");
		addPanel.add(add5Lbl);
		addPanel.add(add5Txt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Add Inventory:"));

		String name = "Add Item";
		n = enterInput(name, addPanel, options);
		if (n == 0) {
			try {
				input1 = add1Txt.getText();
				input2 = add2Txt.getText();
				input3 = Integer.parseInt(add3Txt.getText());
				input4 = Integer.parseInt(add4Txt.getText());
				input5 = Integer.parseInt(add5Txt.getText());
				addToDatabase(con, input1, input2, input3, input4, input5);
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
		n = JOptionPane.showOptionDialog(null, panel, "Add Inventory Item Data: "+name, JOptionPane.NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		return n;
	}

	private static void addToDatabase(Connection con, String userName, String date, int qty, 
			int minQty, int maxQty) throws SQLException {
        Statement stmt = con.createStatement();
        //mcbfood
        Date itemDate=Date.valueOf(date);
        String sql = "INSERT INTO mcbfood (item_name, item_date, amount, min_amount, max_amount) "
         		+ "VALUES ('" +userName + "','" + itemDate + "',"+ qty + ","+ minQty + ","+ maxQty +")";
        int rs = stmt.executeUpdate(sql);
        if (rs==1) {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Successfully added");
        } else {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Could not add item");
        }
	}
}
