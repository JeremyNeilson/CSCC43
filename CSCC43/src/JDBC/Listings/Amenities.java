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
			System.out.println("Does your listing have a free washer? Y/N");
			String input = in.nextLine();
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				washer = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				dryer = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				tv = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				ac = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				wifi = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				fridge = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				stove = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				oven = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				basics = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				dishes = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				coffeeMaker = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
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
			if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
				microwave = true;
				break;
			}
			else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
				microwave = false;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
	}
}
