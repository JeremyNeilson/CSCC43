package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BnBHub {
	User user;
	Connection con;
	
	BnBHub(User user, Connection con) {
		this.user = user;
		this.con = con;
	}
	
	public void runHub(Scanner in) throws SQLException {
		while (true) {
			System.out.println("Jeremy's BnB:\n[B]rowse Listings, [C]reate Listing, [P]rofile, [L]ogout");
			String input = in.nextLine();
			
			if (input.charAt(0) == 'B' || input.charAt(0) == 'b') {
				// Browse listings
			}
			else if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
				// Create a listing
			}
			else if (input.charAt(0) == 'P' || input.charAt(0) == 'p') {
				// Load profile info
				Profile profile = new Profile(this.user, this.con);
				profile.ProfileHub(in);
				
			}
			else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
				// Logout
				break;
			}
		}
	}
}
