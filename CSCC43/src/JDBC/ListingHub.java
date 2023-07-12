package JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ListingHub {
	User user;
	Connection con;
	
	ListingHub(User user, Connection con) {
		this.user = user;
		this.con = con;
	}
	
	public void runHub(Scanner in) throws SQLException {
		while (true) {
			System.out.println("MY LISTINGS:");
			this.printMyListings();
			
			System.out.println("[C]reate New Listing, [E]xit My Listings");
			String input = in.nextLine();
			
			if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
				Listing listing = new Listing();
				makeListing(listing, in);
			}
			
			else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
				break;
			}
		}
	}
	
	public void printMyListings() {
		
	}
	
	public void makeListing(Listing listing, Scanner in) throws SQLException {
		// coordinates
		System.out.println("LISTING CREATION\n\n");
		System.out.println("Enter the latitude: ");
		listing.latitude = Float.parseFloat(in.nextLine());
		System.out.println("Enter the longitude: ");
		listing.longitude = Float.parseFloat(in.nextLine());
		
		// 
		System.out.println("Address of Listing: ");
		listing.str_addr = in.nextLine();
				
		System.out.println("Postal Code:");
		listing.p_code = in.nextLine();
				
		System.out.println("City: ");
		listing.city = in.nextLine();
				
		System.out.println("Country: ");
		listing.country = in.nextLine();
			
		while (true) {
			System.out.println("WHICH TYPE OF BNB IS YOUR LISTING:\n [E]ntire Place, [P]rivate Room, [H]otel Room, [S]hared Room");
			char input = in.nextLine().charAt(0);
			if (input == 'E' || input == 'e') {
				listing.type = "Entire Place";
				break;
			}
			else if (input == 'P' || input == 'p') {
				listing.type = "Private Room";
				break;
			}
			else if (input == 'H' || input == 'h') {
				listing.type = "Hotel Room";
				break;
			}
			else if (input == 'S' || input == 's') {
				listing.type = "Shared Room";
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
		
		System.out.println("Number of Bedrooms in this Listing: ");
		listing.bedrooms = Integer.parseInt(in.nextLine());
		
		System.out.println("Number of Bathrooms in this Listing: ");
		listing.bathrooms = Integer.parseInt(in.nextLine());
		
		System.out.println("Please enter additional amentities: ");
		listing.amenities = in.nextLine();
				
		String listingQuery = "INSERT INTO listing VALUES (" + Float.toString(listing.latitude) + ", " + Float.toString(listing.longitude) + ", '" +  user.SIN + "', '" + listing.type + "', '" + listing.str_addr + "', '" + listing.p_code + "', '" + listing.city + "', '" + listing.country + "', '" + listing.amenities + "');";
		System.out.println(listingQuery);
		Statement update = con.createStatement();
		try {
			update.execute(listingQuery);
			System.out.println("New Listing Created!\n\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		update.close();
	}
}
