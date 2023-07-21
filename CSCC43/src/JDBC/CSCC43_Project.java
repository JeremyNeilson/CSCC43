package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

import JDBC.UserDetails.CreateAccount;
import JDBC.UserDetails.Login;
import JDBC.UserDetails.User;

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
				while (true) {
					// get login details
					String u_email = login.getEmail(in);
					if (u_email == null) {
						break;
					}
					String u_pword = login.getPassword(in);
					if (u_pword == null) {
						break;
					}
					
					// fetch login info
					String check = "select * from user where email = '" + u_email + "' and password = '" + u_pword + "';";
					Statement checkStatement = con.createStatement();
					ResultSet rs = checkStatement.executeQuery(check);
					
					// if there is a result, then run the hub
					if (rs.next() != false) {
						System.out.println("Welcome Back!");
						BnBHub hub = new BnBHub(new User(u_email, u_pword, rs.getString("f_name"), rs.getString("l_name"), LocalDate.parse(rs.getString("birthday")), 
										rs.getString("str_addr"), rs.getString("occupation"), rs.getString("SIN")), con);
						hub.runHub(in);
						break;
					}
					else {
						System.out.println("Incorrect user details, please re-enter");
					}
				}
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
					continue;
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
			if (login.choice == 3) {
				break;
			}
		}
		con.close();
	}
}
