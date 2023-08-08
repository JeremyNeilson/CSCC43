package JDBC.Bookings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import JDBC.UserDetails.User;
import JDBC.Printer;
import JDBC.Listings.Listing;

public class BookingPrinter implements Printer{
	User user;
	Connection con;
	
	public BookingPrinter(User user, Connection con){
		this.user = user;
		this.con = con;
	}
	
	public int printMyBookings(ArrayList<Booking> bookings, ArrayList<Listing> listings, ResultSet rs) throws SQLException {
		int numBookings = 1;
		while (rs != null && rs.next() != false) {
			Booking ls = new Booking();
			Listing list = new Listing();
			bookings.add(ls.SetBooking(rs));
			listings.add(list.setListing(rs));
			System.out.println("[" + Integer.toString(numBookings) + "]");
			ls.PrintBooking();
			list.printListing();
			System.out.println("");
			numBookings++;
		}
		System.out.println("---------------------\n");
		return numBookings;
	}
}
