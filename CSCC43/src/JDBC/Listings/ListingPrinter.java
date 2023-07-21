package JDBC.Listings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import JDBC.UserDetails.User;

public class ListingPrinter {
	User user;
	Connection con;
	
	public ListingPrinter(User user, Connection con){
		this.user = user;
		this.con = con;
	}
	
	public void printMyListings(ArrayList<Listing> listings, ResultSet rs) throws SQLException {
		int numListings = 0;
		while (rs != null && rs.next() != false) {
			Listing ls = new Listing();
			listings.add(ls.setListing(rs));
			System.out.println("[" + Integer.toString(numListings) + "]");
			ls.printListing();
			System.out.println("");
			numListings++;
		}
		System.out.println("---------------------\n");
	}
}
