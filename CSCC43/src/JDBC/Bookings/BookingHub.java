package JDBC.Bookings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import JDBC.Bookings.BrowseListings;
import JDBC.Listings.Listing;
import JDBC.Listings.ListingHub;
import JDBC.Listings.ListingPrinter;
import JDBC.UserDetails.Profile;
import JDBC.UserDetails.User;

public class BookingHub {
	User user;
	Connection con;
	ArrayList<Booking> bookings;
	ArrayList<Listing> listings;
	int numBookings;
	int numListings;
	
	public BookingHub(Connection con, User user){
		this.con = con;
		this.user = user;
		this.bookings = new ArrayList<Booking>();
		this.listings = new ArrayList<Listing>();
		numBookings = 0;
		numListings = 0;
	}
	
	public void RunHub(Scanner in) throws SQLException {
		while (true) {
			numBookings = 0;
			numListings = 0;
			System.out.println("MY BOOKINGS:");
			
			// retrieve my bookings
			Statement statement = null;
			try {
				statement = con.createStatement();
			}catch (SQLException e) {
				e.printStackTrace();
			}
			LocalDate now = LocalDate.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedNow = now.format(dateTimeFormatter);
			String query = "select * from booking, listing where user = '" + user.SIN + "' and date >= '" + formattedNow + "' and booking.l_latitude"
					+ " = listing.latitude and booking.l_longitude = listing.longitude;";
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(query);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			if (rs.next() == true) {
				BookingPrinter printer = new BookingPrinter(this.user, this.con);
				Booking ls = new Booking();
				Listing list = new Listing();
				bookings.add(ls.SetBooking(rs));
				listings.add(list.setListing(rs));
				System.out.println("[" + Integer.toString(numBookings) + "]");
				ls.PrintBooking();
				list.printListing();
				System.out.println("");
				numBookings++;
				numBookings += printer.printMyBookings(bookings, listings, rs);
				
				// control given to user
				System.out.println("[V]iew Past Bookings, [C]ancel a Booking, [G]o Back");
				String input = in.nextLine();
				
				if (input.charAt(0) == 'V' || input.charAt(0) == 'v') {
					ViewPastBookings(in, con, formattedNow);
				}
				else if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
					QueryDeleteBooking(in);
					break;
				}
				else if (input.charAt(0) == 'G' || input.charAt(0) == 'g') {
					break;
				}
			}
			else {
				// control given to user
				System.out.println("You currently have no bookings, [V]iew Past Bookings, [G]o Back");
				String input = in.nextLine();
				
				if (input.charAt(0) == 'G' || input.charAt(0) == 'g') {
					break;
				}
				if (input.charAt(0) == 'V' || input.charAt(0) == 'v') {
					ViewPastBookings(in, con, formattedNow);
				}
			}
		}
	}
	
	public void ViewPastBookings(Scanner in, Connection con, String date) throws SQLException {
		System.out.println(date);
		System.out.println(user.SIN);
		String query = "select * from booking, listing where user = '" + user.SIN + "' and e_date <= '" + date + "' and booking.l_latitude"
				+ " = listing.latitude and booking.l_longitude = listing.longitude;" ;
		Statement statement = con.createStatement();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(query);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		if (rs.next() == true) {
			BookingPrinter printer = new BookingPrinter(this.user, this.con);
			Booking ls = new Booking();
			Listing list = new Listing();
			bookings.add(ls.SetBooking(rs));
			listings.add(list.setListing(rs));
			System.out.println("[" + Integer.toString(numBookings) + "]");
			ls.PrintBooking();
			list.printListing();
			System.out.println("");
			numBookings++;
			numBookings += printer.printMyBookings(bookings, listings, rs);
		}
		else {
			System.out.println("No past bookings");
		}
	}
	
	public void QueryDeleteBooking(Scanner in) throws SQLException {
		while (true) {
			System.out.println("Enter the number of the booking you wish to cancel, or [E]xit");
			if (in.hasNextInt()) {
				int bookingNum = in.nextInt();
				in.nextLine();
				if (bookingNum < 0 || bookingNum > numBookings - 1) {
					System.out.println("Please enter a valid booking number");
					continue;
				}
				System.out.println("Chosen booking:");
				Booking chosen = bookings.get(bookingNum);
				Listing chose = listings.get(bookingNum);
				chosen.PrintBooking();
				chose.printListing();
				while (true) {
					System.out.println("ARE YOU SURE YOU WANT TO CANCEL THIS BOOKING? Y/N");
						String input = in.nextLine();
						if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
							Statement deleter = con.createStatement();
							String deleteQuery = "delete from booking where l_latitude = " + Float.toString(chosen.latitude) 
													+ " and l_longitude = " + Float.toString(chosen.longitude) + " and user = '" + user.SIN 
													+ "' and date = '" + chosen.date + "';";
							String insertQuery = "insert into cancel_booking values ("+ Float.toString(chosen.latitude) 
													+ ", " + Float.toString(chosen.longitude) + ", '" + chosen.date 
													+ "', '" + user.SIN + "', '" + chosen.e_date + "', 1);";
							try {
								deleter.execute(deleteQuery);
								numBookings--;
							}catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								deleter.execute(insertQuery);
							}catch (SQLException e) {
								e.printStackTrace();
							}
							break;
						}
						else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
							break;
						}
						else {
							System.out.println("Please enter a valid input");
							continue;
						}
				}
				
			}
			else {
				String input = in.nextLine();
				if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
					break;
				}
				
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
			}
		}
	}
}
