package JDBC.Bookings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
		String query;
		while (true) {
			System.out.println("[A]dd Filters, [B]rowse");
			// add a print filter functionality
			String input = in.nextLine();
			
			if (input.equals("A")|| input.equals("a")) {
				Filter filters = new Filter(user);
				query = filters.chooseFilters(in);
				break;
			}
			
			else if (input.equals("B")|| input.equals("b")) {
				query = "select * from listing, host where removed = 0 and listing.host != '" + user.SIN + "' and host.frozen = 0 and host.h_sin = listing.host;";
				break;
			}
		}
		System.out.println("AVAILABLE LISTINGS:");
		this.printListings(query);
		
		// retrieve the desired listing
		while (true) {
			System.out.println("Enter the number of your desired listing, or [E]xit");
			if (in.hasNextInt()) {
				int listingNum = in.nextInt();
				in.nextLine();
				if (listingNum < 1 || listingNum > numListings) {
					System.out.println("Please enter a valid listing number");
					continue;
				}
				System.out.println("Chosen listing:");
				Listing chosen = listings.get(listingNum - 1);
				chosen.printListing();
				while (true) {
					System.out.println("Create booking for this listing? Y/N");
					String input = in.nextLine();
					if (input.equals("Y")|| input.equals("y")) {
						Booking newBooking = new Booking(chosen.latitude, chosen.longitude, LocalDate.now().toString(), this.user);
						newBooking.MakeBooking(in, con);
						break;
					}
					else if (input.equals("N")|| input.equals("n")) {
						break;
					}
					else {
						System.out.println("Please enter a correct answer");
					}
				}
				
			}
			else {
				String input = in.nextLine();
				if (input.equals("E")|| input.equals("e")) {
					break;
				}
				
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
			}
		}
	}
	
	void printListings(String query) throws SQLException {
		Statement querible = con.createStatement();
		ResultSet rs = null;
		try {
			rs = querible.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ListingPrinter printer = new ListingPrinter(this.user, this.con);
		numListings = printer.printMyListings(listings, rs);
	}
}
