package JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import JDBC.Bookings.BookingHub;
import JDBC.Bookings.BrowseListings;
import JDBC.Comments.CommentHub;
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
			System.out.println("Jeremy's BnB:\n[B]rowse Listings, [C]reate or View My Listings, [R]eviews, [M]y Bookings, [P]rofile, [L]ogout");
			String input = in.nextLine();
			
			if (input.equals("B")|| input.equals("b")) {
				// Browse listings
				BrowseListings browsing = new BrowseListings(this.user, this.con);
				browsing.browse(in);
			}
			else if (input.equals("C")|| input.equals("c")) {
				// Create a listing
				ListingHub listings = new ListingHub(this.user, this.con);
				listings.runHub(in);
			}
			else if (input.equals("M")|| input.equals("m")) {
				// Create a listing
				BookingHub bookings = new BookingHub(this.con, this.user);
				bookings.RunHub(in);
			}
			else if (input.equals("P")|| input.equals("p")) {
				// Load profile info
				Profile profile = new Profile(this.user, this.con);
				profile.ProfileHub(in);
				if (user.loggedIn == false) {
					return;
				}
				
			}
			else if (input.equals("R")|| input.equals("r")) {
				// Load profile info
				CommentHub comments = new CommentHub(this.user, this.con);
				comments.runHub(in);
				
			}
			else if (input.equals("L")|| input.equals("l")) {
				// Logout
				user.loggedIn = false;
				break;
			}
		}
	}
}
