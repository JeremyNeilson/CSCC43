package JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import JDBC.Bookings.BookingHub;
import JDBC.Bookings.BrowseListings;
import JDBC.Listings.ListingHub;
import JDBC.UserDetails.Profile;
import JDBC.UserDetails.User;

public class BnBHub {
	User user;
	Connection con;
	
	BnBHub(User user, Connection con) {
		this.user = user;
		this.con = con;
	}
	
	public void runHub(Scanner in) throws SQLException {
		while (true) {
			System.out.println("Jeremy's BnB:\n[B]rowse Listings, [C]reate or View My Listings, [M]y Bookings, [P]rofile, [L]ogout");
			String input = in.nextLine();
			
			if (input.charAt(0) == 'B' || input.charAt(0) == 'b') {
				// Browse listings
				BrowseListings browsing = new BrowseListings(this.user, this.con);
				browsing.browse(in);
			}
			else if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
				// Create a listing
				ListingHub listings = new ListingHub(this.user, this.con);
				listings.runHub(in);
			}
			else if (input.charAt(0) == 'M' || input.charAt(0) == 'm') {
				// Create a listing
				BookingHub bookings = new BookingHub(this.con, this.user);
				bookings.RunHub(in);
			}
			else if (input.charAt(0) == 'P' || input.charAt(0) == 'p') {
				// Load profile info
				Profile profile = new Profile(this.user, this.con);
				profile.ProfileHub(in);
				
			}
			else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
				// Logout
				user.loggedIn = false;
				break;
			}
		}
	}
}
