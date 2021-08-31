package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // TODO code application logic here\
        String host = "jdbc:mysql://mysqltrial.c0gfjylwc1ro.us-east-2.rds.amazonaws.com:3306/mydb";
        String uname = "admin";
        String password = "pineapple";
        Connection con = null;
        try {
            //Connection con = DriverManager.getConnection(host, uname, password);
        	Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection(host, uname, password);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Breads";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs.getRow());
            System.out.println(rs.next());
            while (rs.next()) {
                String name = rs.getString("Name");
            String dough = rs.getString("DOUGHTYPE");
            System.out.println("Name\tDough");
            System.out.println(name + "\t" + dough);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }
    }
}
