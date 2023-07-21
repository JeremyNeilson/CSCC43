package JDBC.Listings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import JDBC.UserDetails.User;

public class Listing {
	float latitude;
	float longitude;
	String host_SIN; // 9
	String type; // 25
	String str_addr; // 100
	String p_code; // 7
	String city; // 50
	String country; // 50
	String amenities; // 250
	int bedrooms;
	int bathrooms;
	
	public Listing(){
		latitude = 0.00f;
		longitude = 0.00f;
		host_SIN = ""; // 9
		type = ""; // 25
		str_addr = ""; // 100
		p_code = ""; // 7
		city = ""; // 50
		country = ""; // 50
		amenities = ""; // 250
		bedrooms = 0;
		bathrooms = 0;
	}
	
	
	// Requires a ResultSet of a query to the listing table
	public Listing setListing(ResultSet rs) throws SQLException {
		this.host_SIN = rs.getString("host");
		this.latitude = Float.parseFloat(rs.getString("latitude"));
		this.longitude = Float.parseFloat(rs.getString("longitude"));
		this.type = rs.getString("type");
		this.city = rs.getString("city");
		this.country = rs.getString("country");
		this.str_addr = rs.getString("str_addr");
		this.p_code = rs.getString("p_code");
		this.amenities = rs.getString("amenities");
		return this;
	}
	
	public void printListing() {
		System.out.println("---------------------");
		System.out.println(str_addr);
		System.out.println(city + " " + country + " " + p_code);
		System.out.println("Type of stay: " + type);
		if (amenities != "") {
			System.out.println("Notes from the host:\nABC" + amenities + "DEF");
		}
		System.out.println("---------------------");
	}
	
	public void makeListing(Scanner in, User user, Connection con) throws SQLException{
		// coordinates
		System.out.println("LISTING CREATION\n\n");
		System.out.println("Enter the latitude: ");
		this.latitude = Float.parseFloat(in.nextLine());
		System.out.println("Enter the longitude: ");
		this.longitude = Float.parseFloat(in.nextLine());
		
		// 
		System.out.println("Address of Listing: ");
		this.str_addr = in.nextLine();
				
		System.out.println("Postal Code:");
		this.p_code = in.nextLine();
				
		System.out.println("City: ");
		this.city = in.nextLine();
				
		System.out.println("Country: ");
		this.country = in.nextLine();
			
		while (true) {
			System.out.println("WHICH TYPE OF BNB IS YOUR LISTING:\n [E]ntire Place, [P]rivate Room, [H]otel Room, [S]hared Room");
			char input = in.nextLine().charAt(0);
			if (input == 'E' || input == 'e') {
				this.type = "Entire Place";
				break;
			}
			else if (input == 'P' || input == 'p') {
				this.type = "Private Room";
				break;
			}
			else if (input == 'H' || input == 'h') {
				this.type = "Hotel Room";
				break;
			}
			else if (input == 'S' || input == 's') {
				this.type = "Shared Room";
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
		
		System.out.println("Number of Bedrooms in this Listing: ");
		this.bedrooms = Integer.parseInt(in.nextLine());
		
		System.out.println("Number of Bathrooms in this Listing: ");
		this.bathrooms = Integer.parseInt(in.nextLine());
		
		System.out.println("Please enter additional amentities: ");
		this.amenities = in.nextLine();
				
		String listingQuery = "INSERT INTO listing VALUES (" + Float.toString(this.latitude) + ", " + Float.toString(this.longitude) + ", '" +  user.SIN + "', '" + this.type + "', '" + this.str_addr + "', '" + this.p_code + "', '" + this.city + "', '" + this.country + "', '" + this.amenities + "');";
		String isHost = "select * from host where h_sin = '" + user.SIN + "';";
		System.out.println(listingQuery);
		Statement update = null;
		ResultSet rs = null;
		try {
			update = con.createStatement();
		}catch (SQLException e ) {
			e.printStackTrace();
		}
		try {
			update.execute(listingQuery);
			System.out.println("New Listing Created!\n\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = update.executeQuery(isHost);
			if (rs.next() == false) {
				try {
					update.execute("INSERT INTO host VALUES ('" + user.SIN +"');");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		update.close();
	}
}