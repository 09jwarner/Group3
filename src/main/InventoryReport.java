package main;

/** File: 		InventoryReport.java 
 ** Author: 	Group 3 Heather, John and MC
 ** Date: 		10/10/2021
 ** Purpose: 	This class contains methods to retrieve all rows from the Inventory table
 **				
 **				
 ** Revisions:
 *	1.0 	09/21/2021		Heather		Started coding based on pseudocode 
 *	1.1		09/25/2021 		Heather		Updated table column names to match table
 *	1.2 	09/26/2021		Heather		Added Javadoc and comments
 *	1.3		10/03/2021		Heather		Updated column names for inventory report
 *
 **/

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class InventoryReport {

	static String[] columnNames = { "Item name", "Expiration Date", "Quantity in stock", "Minimum Quantity", "Maximum Quantity" };
	
	/**
	 * displayInvReport: A report with relevant rows is displayed based on the user selection
	 * @param con: The active JDBC connection to the database
	 * @param sql: The SQL statement to be used based on the type of report requested
	 * @param reportName: Report title based on type of report
	 */
	public static void displayInvReport(Connection con, String sql, String reportName) {
		try {
			DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String name = rs.getString("item_name");
				Date date = Date.valueOf(rs.getString("exp_date"));
				int qty = rs.getInt("qty_in_stock");
				int minQty = rs.getInt("min_qty");
				int maxQty = rs.getInt("max_qty");
				
				dtm.addRow(new Object[] { name, date, qty, minQty, maxQty });

			}

			JFrame frame = new JFrame(reportName);
			JTable table = new JTable(dtm);
			JScrollPane scrollPane = new JScrollPane(table);
			table.setFillsViewportHeight(true);

			DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
			rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
			table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
			table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
			table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

			frame.setSize(850, 400);
			frame.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
