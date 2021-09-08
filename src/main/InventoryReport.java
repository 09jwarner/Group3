package main;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class InventoryReport {
	JMenuBar menuBar;
	JMenu file;
	JMenuItem openMenu;
	JTextArea textArea;
	static String[] columnNames = { "Bread", "Dough", "Cuisine", "Prooftime", "Temp" };
	

	public static void displayInvReport(Connection con, String sql, String reportName) {
		try {
			DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String name = rs.getString("Name");
				String dough = rs.getString("DOUGHTYPE");
				String cuisine = rs.getString("Cuisine");
				int proof = rs.getInt("ProofTime");
				int temp = rs.getInt("BakeTemp");
				
				dtm.addRow(new Object[] { name, dough, cuisine, proof, temp });

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
