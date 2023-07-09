package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CSCC43_Project {
	public static void main(String [] args) throws SQLException{
		
		String url = "jdbc:mysql://localhost:3306/bnb";
		Connection con = null;
		Scanner in = new Scanner(System.in);
		
		// create connection
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		try {
			con = DriverManager.getConnection(url, "root", "DinJ4vla!#asd");
		} catch (Exception e){
			System.out.println("Couldn't make the connection");
			e.printStackTrace();
		}
		Login login = new Login();
		while(true) {
			// get login or sign up info
			login.choice = login.requestLogin(in);
			
			// user has an account, wants to log in
			if (login.choice == 1) {
				String u_email = login.getEmail(in);
				String u_pword = login.getPassword(in);
				System.out.println(u_email + " " + u_pword + "\n");
			}
			
			// user wants to create an account
			if (login.choice == 2) {
				CreateAccount newAccount = new CreateAccount();
				User newUser = newAccount.makeUser(in);
				
				// check if an account of that email exists already
				Statement checkStatement = con.createStatement();
				String check = "select * from user where email = '" + newUser.email + "'";
				ResultSet rs = checkStatement.executeQuery(check);
				if (rs.next() != false) {
					System.out.println("Boss we found someone with that email");
					break;
				}
				
				// put newUser in the database
				Statement statement = con.createStatement();
				String query = newUser.createInsert();
				System.out.println(query);
				statement.execute(query);
				if (statement != null) {
					statement.close();
				}
			}
		}
		con.close();
	}
}
