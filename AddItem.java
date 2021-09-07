
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddItem {

	public static void addInvItem(Connection con) {
		JTextField dim1Txt = new JTextField(10);
		JLabel dim1Lbl= new JLabel();
		JTextField dim2Txt = new JTextField(10);
		JLabel dim2Lbl= new JLabel();
		JTextField dim3Txt = new JTextField(10);
		JLabel dim3Lbl= new JLabel();
		JTextField dim4Txt = new JTextField(10);
		JLabel dim4Lbl= new JLabel();
		JTextField dim5Txt = new JTextField(10);
		JLabel dim5Lbl= new JLabel();
		JPanel addPanel = new JPanel();
		addPanel.setLayout(new GridLayout(5, 1, 50, 20));
		addPanel.setPreferredSize(new Dimension(500, 250));

		String[] options = {"OK"};
		int n = 0;
		
		String input1;
		String input2;
		String input3;
		int input4;
		int input5;
		
		dim1Lbl = new JLabel("Enter Bread Name: ");
		addPanel.add(dim1Lbl);
		addPanel.add(dim1Txt);
		dim2Lbl = new JLabel("Enter Doughtype: ");
		addPanel.add(dim2Lbl);
		addPanel.add(dim2Txt);
		dim3Lbl = new JLabel("Enter Cuisine: ");
		addPanel.add(dim3Lbl);
		addPanel.add(dim3Txt);
		dim4Lbl = new JLabel("Enter Prooftime: ");
		addPanel.add(dim4Lbl);
		addPanel.add(dim4Txt);
		dim5Lbl = new JLabel("Enter Baketemp: ");
		addPanel.add(dim5Lbl);
		addPanel.add(dim5Txt);
		addPanel.setBorder(BorderFactory.createTitledBorder("Add Inventory:"));

		String name = "Add Item";
		n = enterInput(name, addPanel, options);
		System.out.println(dim1Txt.getText());
		if (n == 0) {
			try {
				input1 = dim1Txt.getText();
				input2 = dim2Txt.getText();
				input3 = dim3Txt.getText();
				input4 = Integer.parseInt(dim4Txt.getText());
				input5 = Integer.parseInt(dim5Txt.getText());
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

	private static void addToDatabase(Connection con, String breadName, String dough, String cuisine, 
			int proof, int temp) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO Breads (Name, Doughtype, Cuisine, Prooftime, Baketemp) "
          		+ "VALUES ('" +breadName + "','" + dough + "','"+ cuisine + "','"+ proof + "','"+ temp +"')";
        System.out.println(sql);
        int rs = stmt.executeUpdate(sql);
        System.out.println(rs);
        if (rs==1) {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Successfully added");
        } else {
        	JOptionPane jf = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "Could not add item");
        }
	}
}
