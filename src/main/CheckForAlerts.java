package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckForAlerts {

	public static boolean checkLowInv(Connection con, String sql) throws SQLException {
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
	
	public static boolean checkExpiredItems(Connection con, String sql) throws SQLException {
		boolean expiredInv = false;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int count = rs.getInt("count(*)");
        if (count >= 1) {
        	expiredInv = true;
        }
        return expiredInv;
	}
}
