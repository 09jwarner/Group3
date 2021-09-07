/** File: 		GUIShape.java
 ** Author: 	MC Birrane
 ** Date: 		2/9/2021
 ** Purpose: 	This class contains the main method and its constructor contains
 **				all the radio buttons and buttons to create the GUI for the user to choose
 **				their shape. It also has 2 listener methods to listen to the submit button and the 
 **				exit button when clicked.  The submit button will call either the DrawUserInput or
 **				Display3DImage class.
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

public class PantryGUI extends JFrame {
	static final int W = 450, H = 300;
	
	// Create radio buttons
	private JButton addBtn = new JButton("Add Item");
	private JButton deleteBtn = new JButton("Remove Item");
	private JButton updateBtn = new JButton("Update Item");
	private JButton showBtn = new JButton("Show Current Inventory");
	private JButton expireBtn = new JButton("Show Expired Items");
	private JButton lowInvBtn = new JButton("Show Low Inventory");

	// Create submit and exit text buttons
	private JButton submitBtn = new JButton("Submit");
	private JButton exitBtn = new JButton("Exit");

	/**
	 * GUIShape constructor
	 */
	public PantryGUI() {
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
		buttonsPanel.setLayout(new GridLayout(3, 2, 80, 20));
		buttonsPanel.setPreferredSize(new Dimension(150, 100));
		buttonsPanel.setBorder(BorderFactory.createTitledBorder("Pantry Actions:"));
		buttonsPanel.add(addBtn);
		buttonsPanel.add(deleteBtn);
		buttonsPanel.add(updateBtn);
		buttonsPanel.add(showBtn);
		buttonsPanel.add(expireBtn);
		buttonsPanel.add(lowInvBtn);

		// Create panel for submit and exit button
		JPanel submitPanel = new JPanel();
		submitPanel.setPreferredSize(new Dimension(75, 10));
		submitPanel.setBorder(BorderFactory.createTitledBorder("Exit Program:"));
		submitPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		//submitBtn.setPreferredSize(new Dimension(100, 25));
		exitBtn.setPreferredSize(new Dimension(75, 25));
		//submitPanel.add(submitBtn);
		submitPanel.add(exitBtn);

		// Add Everything to the Main Panel
		mainPanel.add(buttonsPanel);
		mainPanel.add(submitPanel);
		setContentPane(mainPanel);
		
		Connection con = openDatabase();
		
		//addBtn, deleteBtn, updateBtn, showBtn, expireBtn, lowInvBtn

		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//submitBtnAction(event);
				JOptionPane jPane = new JOptionPane();
				//JOptionPane.showMessageDialog(jPane, "Add an Item!");
				AddItem.addInvItem(con);
			}
		});
		
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//submitBtnAction(event);
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Delete an Item!");
			}
		});
		
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//submitBtnAction(event);
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Update an Item!");
			}
		});

		showBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//submitBtnAction(event);
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Show Total Inventory!");
			}
		});
		
		expireBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//submitBtnAction(event);
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Show Expired Inventory!");
			}
		});
		
		lowInvBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//submitBtnAction(event);
				JOptionPane jPane = new JOptionPane();
				JOptionPane.showMessageDialog(jPane, "Show Low Inventory Items!");
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
	 */
	public static void main(String[] args) {
		PantryGUI gui = new PantryGUI();
		gui.setVisible(true);
		
	}
}