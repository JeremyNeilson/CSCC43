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
		while (true) {
			System.out.println("[A]dd Filters, [B]rowse");
			// add a print filter functionality
			String input = in.nextLine();
			
			if (input.charAt(0) == 'A' || input.charAt(0) == 'a') {
				// add filter functionality
			}
			
			else if (input.charAt(0) == 'B' || input.charAt(0) == 'b') {
				break;
			}
		}
		System.out.println("AVAILABLE LISTINGS:");
		this.printListings();
		
		// retrieve the desired listing
		while (true) {
			System.out.println("Enter the number of your desired listing, or [E]xit");
			if (in.hasNextInt()) {
				int listingNum = in.nextInt();
				in.nextLine();
				System.out.println(numListings);
				if (listingNum < 0 || listingNum > numListings - 1) {
					System.out.println("Please enter a valid listing number");
					continue;
				}
				System.out.println("Chosen listing:");
				Listing chosen = listings.get(listingNum);
				chosen.printListing();
				while (true) {
					System.out.println("Create booking for this listing? Y/N");
					char input = in.nextLine().charAt(0);
					if (input == 'Y' || input == 'y') {
						Booking newBooking = new Booking(chosen.latitude, chosen.longitude, LocalDate.now().toString(), this.user);
						newBooking.MakeBooking(in, con);
						break;
					}
					else if (input == 'N' || input == 'n') {
						break;
					}
					else {
						System.out.println("Please enter a correct answer");
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
	
	void printListings() throws SQLException {
		Statement query = con.createStatement();
		ResultSet rs = null;
		try {
			rs = query.executeQuery("select * from listing where host != '" + user.SIN + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ListingPrinter printer = new ListingPrinter(this.user, this.con);
		numListings = printer.printMyListings(listings, rs);
	}
}
