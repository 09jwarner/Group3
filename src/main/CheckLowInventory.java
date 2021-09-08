package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckLowInventory {

	public static boolean checkInv(Connection con, String sql) throws SQLException {
		boolean lowInv = false;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("count(*)");
        if (count >= 1) {
        	lowInv = true;
        }
        return lowInv;
	}
	
}
