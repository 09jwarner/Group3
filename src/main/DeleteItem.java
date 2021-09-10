package main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DeleteItem {

	public static void deleteInvItem(Connection con) {
		JTextField nameTxt = new JTextField(20);
		JLabel nameLbl= new JLabel();
		JPanel addPanel = new JPanel();
		addPanel.setPreferredSize(new Dimension(350, 100));

		String[] options = {"OK"};
		int n = 0;
		
		String input1;
		
		nameLbl = new JLabel("Enter Item Name: ");
		addPanel.add(nameLbl);
		addPanel.add(nameTxt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Delete Item:"));
		
		//display item
		/*addPanel.add(dim1Txt);
		dim2Lbl = new JLabel("Retrieve Item Data");
		addPanel.add(dim2Lbl);
		addPanel.add(dim2Txt);*/
		
		//TODO: Should have button to query and get rest of data then button added to say Delete
		//Update should be similar but with only certain fields editable
		
		

		String name = "Delete Item";
		n = enterInput(name, addPanel, options);
		if (n == 0) {
			try {
				input1 = nameTxt.getText();
				deleteFromDatabase(con, input1);
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

	private static void deleteFromDatabase(Connection con, String breadName) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "DELETE from mcbfood WHERE item_name = " + "'" + breadName + "'";
        System.out.println(sql);
        int rs = stmt.executeUpdate(sql);
        System.out.println(rs);
        if (rs==1) {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Successfully deleted");
        } else {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Could not delete item");
        }
	}
}
