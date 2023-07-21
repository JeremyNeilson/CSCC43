package JDBC.Bookings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

import JDBC.Listings.Listing;
import JDBC.Listings.ListingPrinter;
import JDBC.UserDetails.User;

import java.util.ArrayList;

public class BrowseListings {
	User user;
	Connection con;
	ArrayList<Listing> listings;
	int numListings;
	
	public BrowseListings(User user, Connection con){
		this.user = user;
		this.con = con;
		this.numListings = 0;
		this.listings = new ArrayList<Listing>();
	}
	
	public void browse(Scanner in) throws SQLException {
		while (true) {
			System.out.println("AVAILABLE LISTINGS:");
			this.printListings();
			
			System.out.println("[C]reate New Listing, [E]xit My Listings");
			String input = in.nextLine();
			
			if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
				Listing listing = new Listing();
				listing.makeListing(in, user, con);
			}
			
			else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
				break;
			}
		}
		while (true) {
			System.out.println("AVAILABLE LISTINGS:");
			this.printListings();
			
			System.out.println("[C]reate New Listing, [E]xit My Listings");
			String input = in.nextLine();
			
			if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
				Listing listing = new Listing();
				listing.makeListing(in, user, con);
			}
			
			else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
				break;
			}
		}
	}
	
	void printListings() throws SQLException {
		Statement query = con.createStatement();
		ResultSet rs = null;
		try {
			rs = query.executeQuery("select * from listing where host != '" + user.SIN + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ListingPrinter printer = new ListingPrinter(this.user, this.con);
		printer.printMyListings(listings, rs);
	}
}
