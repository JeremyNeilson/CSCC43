package JDBC.Listings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JDBC.UserDetails.User;

public class Listing {
	public float latitude;
	public float longitude;
	public String host_SIN; // 9
	public String type; // 25
	public String str_addr; // 100
	public String p_code; // 7
	public String city; // 50
	public String country; // 50
	public Amenities amenities; // 250
	public int bedrooms;
	public int bathrooms;
	
	public Listing(){
		latitude = 0.00f;
		longitude = 0.00f;
		host_SIN = ""; // 9
		type = ""; // 25
		str_addr = ""; // 100
		p_code = ""; // 7
		city = ""; // 50
		country = ""; // 50
		amenities = new Amenities();; // 250
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
		amenities.values(rs);
		return this;
	}
	
	public void printListing() {
		System.out.println("---------------------");
		System.out.println(str_addr);
		System.out.println(city + " " + country + " " + p_code);
		System.out.println("Type of stay: " + type);
		amenities.printAmenities();
		System.out.println("---------------------");
	}
	
	public void makeListing(Scanner in, User user, Connection con) throws SQLException{
		// coordinates NEEDS ERROR HANDLING
		System.out.println("LISTING CREATION");
		while (true) {
			System.out.println("Latitude: ");
			if (in.hasNextFloat()) {
				latitude = in.nextFloat();
				in.nextLine();
				if (latitude >= -90f && latitude <= 90f) {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		while (true) {
			System.out.println("Longitude: ");
			if (in.hasNextFloat()) {
				longitude = in.nextFloat();
				in.nextLine();
				if (longitude >= -180f && longitude <= 180f) {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		
		// 
		System.out.println("Address of Listing: ");
		this.str_addr = in.nextLine();
		Pattern pattern = Pattern.compile("[abceghjklmnprstvxy][0-9][abceghjklmnprstvwxyz]\s?[0-9][abceghjklmnprstvwxyz][0-9]", Pattern.CASE_INSENSITIVE);
		while(true) {
			System.out.println("Postal Code:");
			String input = in.nextLine();
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				p_code = input;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		
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
				this.bedrooms = 1;
				break;
			}
			else if (input == 'H' || input == 'h') {
				this.type = "Hotel Room";
				this.bedrooms = 1;
				break;
			}
			else if (input == 'S' || input == 's') {
				this.type = "Shared Room";
				this.bedrooms = 1;
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
		
		amenities.setAmenities(in, this);
				
		String listingQuery = "INSERT INTO listing VALUES (" + Float.toString(this.latitude) + ", " + Float.toString(this.longitude) 
															+ ", '" +  user.SIN + "', '" + this.type + "', '" + this.str_addr + "', '" 
															+ this.p_code + "', '" + this.city + "', '" + this.country + "', " 
															+ amenities.boolToString(amenities.washer) + ", " + amenities.boolToString(amenities.dryer) + ", "
															+ amenities.boolToString(amenities.tv) + ", " + amenities.boolToString(amenities.ac) + ", "
															+ amenities.boolToString(amenities.wifi) + ", " + amenities.boolToString(amenities.stove) + ", "
															+ amenities.boolToString(amenities.oven) + ", " + amenities.boolToString(amenities.basics) + ", "
															+ amenities.boolToString(amenities.dishes) + ", " + amenities.boolToString(amenities.fridge) + ", "
															+ amenities.boolToString(amenities.coffeeMaker) + ", " + amenities.boolToString(amenities.microwave) + ", "
															+ Integer.toString(amenities.parking)
															+ ", 0);";
		String isHost = "select * from host where h_sin = '" + user.SIN + "';";
		Statement update = null;
		ResultSet rs = null;
		try {
			update = con.createStatement();
		}catch (SQLException e ) {
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
		try {
			rs = update.executeQuery("select * from listing where latitude = " + Float.toString(latitude) + " and longitude = " + Float.toString(longitude) + ";");
			if (rs.next() == false) {
				try {
					update.execute(listingQuery);
					System.out.println("New Listing Created!\n\n");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else {
				String newHost = rs.getString("host");
				Boolean removed = rs.getBoolean("removed");
				if (host_SIN == newHost) {
					if (removed == true) {
						while(true) {
							System.out.println("You have previously removed this listing, would you like to restore it? Y/N"
								+ "\nIf you do, it will retain the details of it's previous posting."
								+ "\nYou can change these at any time in the 'Create or View Listings' tab.");
							String input = in.nextLine();
							if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
								try{
									update.execute("update listing set removed = 0 where latitude = " + Float.toString(latitude) + " and longitude = " + Float.toString(longitude) + ";");
								} catch (SQLException e) {
									e.printStackTrace();
								}
								System.out.println("Listing restored!");
								break;
							}
							else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
								break;
							}
						}
					}
					else {
						System.out.println("You already have a listing at these coordinates");
					}
				}
				else if (removed == false) {
					System.out.println("There is already a listing with those coordinates, please try again");
				}
				else {
					try{
						update.execute("delete from listing where latitude = " + Float.toString(latitude) + " and longitude = " + Float.toString(longitude) + ";");
						update.execute(listingQuery);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		update.close();
	}
}
