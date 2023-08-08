package JDBC.Listings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Amenities {
	public Boolean washer;
	public Boolean dryer;
	public Boolean tv;
	public Boolean ac;
	public Boolean wifi;
	public Boolean stove;
	public Boolean oven;
	public Boolean basics;
	public Boolean dishes;
	public Boolean fridge;
	public Boolean coffeeMaker;
	public Boolean microwave;
	public int parking;
	
	public Amenities() {
		washer = false;
		dryer = false;
		tv = false;
		ac = false;
		wifi = false;
		stove = false;
		oven = false;
		basics = false;
		dishes = false;
		fridge = false;
		coffeeMaker = false;
		microwave = false;
		parking = 0;
	}
	
	public String boolToString(Boolean value) {
		if (value == true) {
			return "1";
		}
		else if (value == false) {
			return "0";
		}
		return null;
	}
	
	public void values(ResultSet rs) throws SQLException {
		washer = rs.getBoolean("washer");
		dryer = rs.getBoolean("dryer");
		tv = rs.getBoolean("tv");
		ac = rs.getBoolean("ac");
		wifi = rs.getBoolean("wifi");
		stove = rs.getBoolean("stove");
		oven = rs.getBoolean("oven");
		basics = rs.getBoolean("basics");
		dishes = rs.getBoolean("dishes");
		fridge = rs.getBoolean("fridge");
		coffeeMaker = rs.getBoolean("coffee");
		microwave = rs.getBoolean("microwave");
		parking = rs.getInt("parking");
	}
	
	public void printAmenities() {
		System.out.println("Amenities provided:");
		String amenity = "";
		if (washer == true) {
			amenity = amenity + "Washer ";
		}
		if (dryer == true) {
			amenity = amenity + "Dryer ";
		}
		if (tv == true) {
			amenity = amenity + "TV ";
		}
		if (ac == true) {
			amenity = amenity + "Air Conditioning ";
		}
		if (wifi == true) {
			amenity = amenity + "Wifi ";
		}
		System.out.println(amenity);
		System.out.println("Kitchen Amenities:");
		amenity = "";
		if (stove == true) {
			amenity = amenity + "Stove ";
		}
		if (oven == true) {
			amenity = amenity + "Oven ";
		}
		if (fridge == true) {
			amenity = amenity + "Refridgerator ";
		}
		if (microwave == true) {
			amenity = amenity + "Microwave ";
		}
		if (basics == true) {
			amenity = amenity + "Cooking Basics ";
		}
		if (dishes == true) {
			amenity = amenity + "Dishes and Utensils ";
		}
		if (coffeeMaker == true) {
			amenity = amenity + "Coffee Maker ";
		}
		System.out.println(amenity);
		System.out.println("Number of parking spaces offered: " + Integer.toString(parking));
	}
	
	public void setAmenities(Scanner in, Listing listing) {
		System.out.println("Setting amenities for " + listing.str_addr);
		while(true) {
			System.out.println("Would you like us to suggest amenities? Y/N");
			String input = in.nextLine();
			if (input.equals("Y") || input.equals("y")) {
				suggestAmenities(in, listing);
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have a free washer? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				washer = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				washer = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have a free dryer? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				dryer = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				dryer = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have a TV? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				tv = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				tv = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have air conditioning? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				ac = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				ac = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have Wifi? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				wifi = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				wifi = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while (true) {
			System.out.println("How many parking spaces are available?");
			if (in.hasNextInt()) {
				int spaces = in.nextInt();
				in.nextLine();
				if (spaces < 0) {
					System.out.println("Please enter a valid number");
					continue;
				}
				parking = spaces;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
				in.nextLine();
			}
		}
		System.out.println("Kitchen Amenities:");
		while(true) {
			System.out.println("Does your listing have a refridgerator? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				fridge = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				fridge = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have a stove? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				stove = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				stove = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have an oven? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				oven = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				oven = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have cooking basics (pots, pans, oil, salt and pepper)?  Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				basics = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				basics = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have dishes and silverware? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				dishes = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				dishes = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have a coffee maker? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				coffeeMaker = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				coffeeMaker = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		while(true) {
			System.out.println("Does your listing have a microwave? Y/N");
			String input = in.nextLine();
			if (input.equals("Y")|| input.equals("y")) {
				microwave = true;
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				microwave = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
	}
	
	public void suggestAmenities(Scanner in, Listing listing) {
		System.out.println("We suggest providing your guest with most easily supplied amenities:"
				+ "\nTV, Wifi, at leaast 1 parking space, coffee maker\n These will make a longer stay more manageable for guests");
		while(true) {
			System.out.println("Do you expect guests to stay for longer than one week at your listing? Y/N");
			String input = in.nextLine();
			if (input.equals("Y") || input.equals("y")) {
				System.out.println("If a guest is going to be staying for a longer period,\n we suggest providing them access to a washer and dryer");
				break;
			}
			else if (input.equals("N") || input.equals("n")) {
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		if (listing.type.equals("Entire Place")) {
			while(true) {
				System.out.println("You are providing the entire property for the bookings\nDoes this property have a full kitchen? Y/N");
				String input = in.nextLine();
				if (input.equals("Y") || input.equals("y")) {
					System.out.println("Make sure to add all the kitchen amenities to your listing.\nWe suggest providing cooking supplies to your guest as well.");
					break;
				}
				else if (input.equals("N") || input.equals("n")) {
					System.out.println("We suggest adding as many kitchen amenities as you can manage\nGuests that require the entire property often want to host\n"
							+ "or have access to certain basics like a fridge and microwave for accessibility");
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
		}
		if (listing.type.equals("Hotel Room")) {
			System.out.println("Additionally, in Hotel Rooms, guests often\nexpect to be provided with a coffee maker and a microwave");
		}
		if (listing.type.equals("Private Room")) {
			System.out.println("Additionally, when providing a private room, we suggest \noffering access to a kitchen to be courteous for the guest");
		}
	}
}
