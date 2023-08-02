package JDBC.Listings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.util.Scanner;

import JDBC.Bookings.Booking;
import JDBC.UserDetails.User;

import java.util.ArrayList;

public class ListingHub {
	User user;
	Connection con;
	ArrayList<Listing> listings;
	int numListings;
	
	public ListingHub(User user, Connection con) {
		this.user = user;
		this.con = con;
		this.listings = new ArrayList<Listing>();
		this.numListings = 0;
	}
	
	public void runHub(Scanner in) throws SQLException {
		while (true) {
			System.out.println("MY LISTINGS:");
			
			// retrieve my listings
			Statement statement = null;
			try {
				statement = con.createStatement();
			}catch (SQLException e) {
				e.printStackTrace();
			}
			String query = "select * from listing where host = '" + user.SIN + "';";
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(query);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			if (rs.next() == true) {
				ListingPrinter printer = new ListingPrinter(this.user, this.con);
				Listing ls = new Listing();
				listings.add(ls.setListing(rs));
				System.out.println("[" + Integer.toString(numListings) + "]");
				ls.printListing();
				System.out.println("");
				numListings++;
				numListings += printer.printMyListings(listings, rs);
				
				// control given to user
				System.out.println("[C]reate New Listing, [E]dit a Listing, [D]elete a Listing, [V]iew Bookings, [G]o Back");
				String input = in.nextLine();
				
				if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
					Listing listing = new Listing();
					listing.makeListing(in, user, con);
					QueryForAvailability(listing, in);
				}
				else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
					QueryEditListing(in);
					break;
				}
				else if (input.charAt(0) == 'D' || input.charAt(0) == 'd') {
					QueryDeleteListing(in);
					break;
				}
				else if (input.charAt(0) == 'G' || input.charAt(0) == 'g') {
					break;
				}
				else if (input.charAt(0) == 'V' || input.charAt(0) == 'v') {
					// print all the bookings for the listing
					viewBookings(in);
					break;
				}
			}
			else {
				// control given to user
				System.out.println("[C]reate New Listing, [G]o Back");
				String input = in.nextLine();
				
				if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
					Listing listing = new Listing();
					listing.makeListing(in, user, con);
					QueryForAvailability(listing, in);
				}
				else if (input.charAt(0) == 'G' || input.charAt(0) == 'g') {
					break;
				}
			}
		}
	}
	
	public void QueryDeleteListing(Scanner in) throws SQLException {
		while (true) {
			System.out.println("Enter the number of the listing you wish to delete, or [E]xit");
			if (in.hasNextInt()) {
				int listingNum = in.nextInt();
				in.nextLine();
				if (listingNum < 0 || listingNum > numListings - 1) {
					System.out.println("Please enter a valid listing number");
					continue;
				}
				System.out.println("Chosen listing:");
				Listing chosen = listings.get(listingNum);
				chosen.printListing();
				while (true) {
					System.out.println("ARE YOU SURE YOU WANT TO DELETE THIS LISTING? Y/N");
						String input = in.nextLine();
						if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
							Statement deleter = con.createStatement();
							String deleteQuery = "delete from listing where latitude = " + Float.toString(chosen.latitude) 
													+ " and longitude = " + Float.toString(chosen.longitude) + ";";
							try {
								deleter.execute(deleteQuery);
								numListings--;
							}catch (SQLException e) {
								e.printStackTrace();
							}
							if (numListings == 0) {
								String removeHost = "delete from host where h_sin = '" + user.SIN + "';";
								try {
									deleter.execute(removeHost);
								}catch (SQLException e) {
									e.printStackTrace();
								}
							}
							deleteQuery = "delete from availability where l_latitude = " + Float.toString(chosen.latitude) 
							+ " and l_longitude = " + Float.toString(chosen.longitude) + ";";
							try {
								deleter.execute(deleteQuery);
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
	
	public void QueryEditListing(Scanner in) throws SQLException {
		while (true) {
			System.out.println("Enter the number of the listing you wish to edit, or [E]xit");
			if (in.hasNextInt()) {
				int listingNum = in.nextInt();
				in.nextLine();
				if (listingNum < 0 || listingNum > numListings - 1) {
					System.out.println("Please enter a valid listing number");
					continue;
				}
				System.out.println("Chosen listing:");
				Listing chosen = listings.get(listingNum);
				chosen.printListing();
				while (true) {
					System.out.println("SELECT INFORMATION TO EDIT\n[T]YPE: " + chosen.type + "\n"
							+ "[A]VAILABILITY OR PRICE: \n[E]DIT AMENITIES: \n[D]ONE");
						String input = in.nextLine();
						if (input.charAt(0) == 'T' || input.charAt(0) == 't') {
							updateType(chosen, in);
						}
						else if (input.charAt(0) == 'A' || input.charAt(0) == 'a') {
							updateAvailability(chosen, in);
						}
						else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
							updateAmenities(chosen, in);
							break;
						}
						else if (input.charAt(0) == 'D' || input.charAt(0) == 'd') {
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
	
	public void viewBookings(Scanner in) throws SQLException {
		while (true) {
			System.out.println("Enter the number of the listing you wish to view bookings for, or [E]xit");
			if (in.hasNextInt()) {
				int listingNum = in.nextInt();
				in.nextLine();
				if (listingNum < 0 || listingNum > numListings - 1) {
					System.out.println("Please enter a valid listing number");
					continue;
				}
				System.out.println("Chosen listing:");
				Listing chosen = listings.get(listingNum);
				chosen.printListing();
				Statement statement = con.createStatement();
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String formattedNow = LocalDate.now().format(dateTimeFormatter);
				String listingQuery = "select * from booking, user where booking.user = user.sin and l_latitude = " + Float.toString(chosen.latitude) + " and l_longitude = " 
										+ Float.toString(chosen.longitude) + " and date >= '" + formattedNow + "';";
				ResultSet rs = null;
				try {
					rs = statement.executeQuery(listingQuery);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Bookings for this listing:");
				ArrayList<Booking> bookings = new ArrayList<Booking>();
				ArrayList<User> users = new ArrayList<User>();
				int numBookings = 0;
				while (rs != null && rs.next() != false) {
					Booking ls = new Booking();
					User use = new User("", "", rs.getString("f_name"), rs.getString("l_name"), LocalDate.now(), "", "", rs.getString("SIN"));
					bookings.add(ls.SetBooking(rs));
					users.add(use);
					System.out.println("[" + Integer.toString(numBookings) + "]: " + use.f_name + " " + use.l_name);
					System.out.println("Staying from " + ls.date + " to " + ls.e_date);
					System.out.println("---------------------");
					numBookings++;
				}
				
				
				
				while (true) {
					System.out.println("[C]ancel Booking, [G]o Back");
						String input = in.nextLine();
						if (input.charAt(0) == 'C' || input.charAt(0) == 'c') {
							while (true) {
								System.out.println("Enter the number of the booking you wish to cancel, or [E]xit");
								if (in.hasNextInt()) {
									int listingNu = in.nextInt();
									in.nextLine();
									if (listingNu < 0 || listingNum > numListings - 1) {
										System.out.println("Please enter a valid listing number");
										continue;
									}
									System.out.println("Chosen listing:");
									Booking chose = bookings.get(listingNu);
									User usage = users.get(listingNu);
									System.out.println("[" + Integer.toString(numBookings) + "]: " + usage.f_name + " " + usage.l_name);
									System.out.println("Staying from " + chose.date + " to " + chose.e_date);
									System.out.println("---------------------");
									while (true) {
										System.out.println("ARE YOU SURE YOU WANT TO CANCEL THIS BOOKING? Y/N");
											input = in.nextLine();
											if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
												Statement deleter = con.createStatement();
												String deleteQuery = "delete from booking where l_latitude = " + Float.toString(chose.latitude) 
																		+ " and l_longitude = " + Float.toString(chose.longitude) + " and user = '" + usage.SIN 
																		+ "' and date = '" + chose.date + "';";
												String insertQuery = "insert into cancel_booking values ("+ Float.toString(chose.latitude) 
																		+ ", " + Float.toString(chose.longitude) + ", '" + chose.date 
																		+ "', '" + usage.SIN + "', '" + chose.e_date + "');";
												System.out.println(deleteQuery);
												try {
													deleter.execute(deleteQuery);
													numBookings--;
												}catch (SQLException e) {
													e.printStackTrace();
												}
												try {
													System.out.println(insertQuery);
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
									input = in.nextLine();
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
						else if (input.charAt(0) == 'G' || input.charAt(0) == 'g') {
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
	
	public void updateAmenities(Listing listing, Scanner in) throws SQLException{
		listing.amenities.setAmenities(in, listing);
		String update = "update listing set washer = " + listing.amenities.boolToString(listing.amenities.washer) 
											+ ", dryer = " + listing.amenities.boolToString(listing.amenities.dryer)
											+ ", ac = " + listing.amenities.boolToString(listing.amenities.ac)
											+ ", wifi = " + listing.amenities.boolToString(listing.amenities.wifi)
											+ ", tv = " + listing.amenities.boolToString(listing.amenities.tv)
											+ ", stove = " + listing.amenities.boolToString(listing.amenities.stove)
											+ ", oven = " + listing.amenities.boolToString(listing.amenities.oven)
											+ ", basics = " + listing.amenities.boolToString(listing.amenities.basics)
											+ ", dishes = " + listing.amenities.boolToString(listing.amenities.dishes)
											+ ", fridge = " + listing.amenities.boolToString(listing.amenities.fridge)
											+ ", coffee = " + listing.amenities.boolToString(listing.amenities.coffeeMaker)
											+ ", microwave = " + listing.amenities.boolToString(listing.amenities.microwave)
											+ ", parking = " + Integer.toString(listing.amenities.parking) 
											+ " where latitude = " + Float.toString(listing.latitude) 
											+ "and longitude = " + Float.toString(listing.longitude) + ";";
		Statement statement = null;
		try{
			statement = con.createStatement();
			statement.execute(update);
			System.out.println("Amenities Updated!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		listing.printListing();
		
		
	}
	
	public void updateAvailability(Listing listing, Scanner in) throws SQLException {
		while(true) {
			System.out.println("Update [A]vailability or [P]rice, [E]xit:");
			String input = in.nextLine();
			if (input.charAt(0) == 'A' || input.charAt(0) == 'a') {
				while(true) {
					System.out.println("[A]dd availability, [R]emove availability, [S]tart fresh, [E]xit");
					input = in.nextLine();
					if (input.charAt(0) == 'A' || input.charAt(0) == 'a') {
						queryAvailManual(listing, in);
					}
					else if (input.charAt(0) == 'R' || input.charAt(0) == 'r') {
						deleteAvailability(listing, in);
					}
					else if (input.charAt(0) == 'S' || input.charAt(0) == 's') {
						String delete = "delete from availability where l_latitude = " + Float.toString(listing.latitude) 
											+ " and l_longitude = " + Float.toString(listing.longitude) + ";";
						Statement statement = con.createStatement();
						try {
							statement.execute(delete);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						QueryForAvailability(listing, in);
					}
					else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
						break;
					}
					else {
						System.out.println("Please enter a valid input");
					}
				}
			}
			else if (input.charAt(0) == 'P' || input.charAt(0) == 'p') {
				updatePrice(listing, in);
			}
			else if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
	}
	
	public void updateType(Listing listing, Scanner in) throws SQLException {
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
		Statement update = con.createStatement();
		String query = "update listing set type = '" + listing.type + "' where latitude = " + Float.toString(listing.latitude) 
							+ " and longitude = " + Float.toString(listing.longitude) + ";";
		try{
			update.execute(query);
			System.out.println("Listing type updated!");
		}catch (SQLException e ) {
			e.printStackTrace();
		}
	}
	
	void updatePrice(Listing listing, Scanner in) throws SQLException {
		while (true) {
			System.out.println("SHOULD LISTING PRICES BE CREATED BY: [W]EEKDAY, [M]ANUALLY");
			char input = in.nextLine().charAt(0);
			if (input == 'W' || input == 'w') {
				queryPriceWeekday(listing, in);
				break;
			}
			else if (input == 'M' || input == 'm') {
				queryPriceManual(listing, in);
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
	}
	
	void QueryForAvailability(Listing listing, Scanner in) throws SQLException{
		while (true) {
			System.out.println("SHOULD LISTING AVAILABILITIES BE SCHEDULED BY: [W]EEKDAY, [M]ANUALLY");
			char input = in.nextLine().charAt(0);
			if (input == 'W' || input == 'w') {
				queryAvailWeekday(listing, in);
				break;
			}
			else if (input == 'M' || input == 'm') {
				queryAvailManual(listing, in);
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
	}
	
	void deleteAvailability(Listing listing, Scanner in) throws SQLException{
		while (true) {
			System.out.println("SHOULD LISTING AVAILABILITIES BE DELETED BY: [W]EEKDAY, [M]ANUALLY");
			char input = in.nextLine().charAt(0);
			if (input == 'W' || input == 'w') {
				deleteAvailWeekday(listing, in);
				break;
			}
			else if (input == 'M' || input == 'm') {
				deleteAvailManual(listing, in);
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
	}
	
	void queryPriceWeekday(Listing listing, Scanner in) throws SQLException {
		String[] dayStrings = {"MONDAYS", "TUESDAYS", "WEDNESDAYS", "THURSDAYS", "FRIDAYS", "SATURDAYS", "SUNDAYS"};
		int[] days = {0, 0, 0, 0, 0, 0, 0, 0};
		int day = 0;
		int exit = 0;
		while (true) {
			System.out.println("CHOOSE A DAY OF THE WEEK THAT YOU WISH TO UPDATE ALL PRICES FOR OR [E]XIT:");
			for (int i = 0; i < 7; i++) {
				System.out.println("[" + Integer.toString(i) + "]: " + dayStrings[i]);
			}
			if (in.hasNextInt()) {
				day = in.nextInt();
				in.nextLine();
				if (day < 0 || day > 6) {
					System.out.println("Please enter a valid number");
					continue;
				}
				days[day] = 1;
				break;
			}
			else {
				String input = in.nextLine();
				if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
					exit = 1;
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
		}
		if (exit == 0) {
			double priceInput = 0.00;
			while (true) {
				System.out.println("ENTER YOUR PRICE FOR ONE NIGHT ON " + dayStrings[day] + " (XXX.XX):");
				try {
					priceInput = in.nextDouble();
					System.out.println(priceInput);
					in.nextLine();
					break;
				} catch (NumberFormatException e) {
					System.out.println("Please enter a correct answer");
					continue;
				}
			}
			
			LocalDate today = LocalDate.now();
			DayOfWeek dayofweek = today.getDayOfWeek();
			int startIndex = dayofweek.getValue() - 1;
			String startOfQuery = "update availability set price = " + Double.toString(priceInput) + " where l_latitude =  "
									+ Float.toString(listing.latitude) + " and l_longitude = " + Float.toString(listing.longitude) 
									+ " and date = '";
			Statement statement = null;
			try {
				statement = con.createStatement();
			}catch (SQLException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 365; i++) {
				if (days[startIndex] != 0) {
					String query = startOfQuery + today.plusDays(i).toString() + "';";
					try{
						statement.execute(query);
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
				startIndex++;
				startIndex = startIndex % 7;
			}
		}
	}
	void queryAvailWeekday(Listing listing, Scanner in) throws SQLException{
		// ENTERING AVAILABILITY BY WEEKDAY
		String[] dayStrings = {"MONDAYS", "TUESDAYS", "WEDNESDAYS", "THURSDAYS", "FRIDAYS", "SATURDAYS", "SUNDAYS"};
		int[] days = {0, 0, 0, 0, 0, 0, 0, 0};
		double[] prices = {0, 0, 0, 0, 0, 0, 0, 0};
		for (int i = 0; i < 7; i++) {
			while (true) {
				System.out.println("IS YOUR LISTING AVAILABLE ON " + dayStrings[i] + ": Y/N");
				String charInput = in.nextLine();
				if (charInput.charAt(0) == 'Y' || charInput.charAt(0) == 'y') {
					days[i] = 1;
					break;
				}
				else if (charInput.charAt(0) == 'N' || charInput.charAt(0) == 'n') {
					days[i] = 0;
					break;
				}
				else {
					System.out.println("Please enter a correct answer\n");
				}
			}
			if (days[i] == 1) {
				while (true) {
					System.out.println("ENTER YOUR PRICE FOR ONE NIGHT ON " + dayStrings[i] + " (XXX.XX):");
					double priceInput = 0.00;
					try {
						priceInput = in.nextDouble();
						in.nextLine();
						prices[i] = priceInput;
						break;
					}catch (NumberFormatException e) {
						System.out.println("Please enter a correct answer");
						continue;
					}
				}
			}
		}
		
		// get the day of the week in number, monday is 1. subtract 1 from that so the number matches the array. then do a loop of mod 7
		// that adds an availability to the system for each day that is set to 1 in days array
		LocalDate today = LocalDate.now();
		DayOfWeek dayofweek = today.getDayOfWeek();
		int startIndex = dayofweek.getValue() - 1;
		String startOfQuery = "INSERT INTO availability VALUES (" + Float.toString(listing.latitude) + ", " + Float.toString(listing.longitude) + ", '";
		for (int i = 0; i < 365; i++) {
			if (days[startIndex] != 0) {
				Statement statement = null;
				try {
					statement = con.createStatement();
				}catch (SQLException e) {
					e.printStackTrace();
				}
				String query = startOfQuery + today.plusDays(i).toString() + "', " + prices[startIndex] + ");";
				try{
					statement.execute(query);
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			startIndex++;
			startIndex = startIndex % 7;
		}
	}
	
	void deleteAvailWeekday(Listing listing, Scanner in) throws SQLException{
		String[] dayStrings = {"MONDAYS", "TUESDAYS", "WEDNESDAYS", "THURSDAYS", "FRIDAYS", "SATURDAYS", "SUNDAYS"};
		int[] days = {0, 0, 0, 0, 0, 0, 0, 0};
		int exit = 0;
		while (true) {
			System.out.println("CHOOSE A DAY OF THE WEEK THAT YOU WISH TO REMOVE ALL AVAILABILITY FOR OR [E]XIT:");
			for (int i = 0; i < 7; i++) {
				System.out.println("[" + Integer.toString(i) + "]: " + dayStrings[i]);
			}
			if (in.hasNextInt()) {
				int day = in.nextInt();
				in.nextLine();
				if (day < 0 || day > 6) {
					System.out.println("Please enter a valid number");
					continue;
				}
				days[day] = 1;
				break;
			}
			else {
				String input = in.nextLine();
				if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
					exit = 1;
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
		}
		if (exit == 0) {
			LocalDate today = LocalDate.now();
			DayOfWeek dayofweek = today.getDayOfWeek();
			int startIndex = dayofweek.getValue() - 1;
			String startOfQuery = "delete from availability where l_latitude = " + Float.toString(listing.latitude) 
									+ " and l_longitude = " + Float.toString(listing.longitude) + " and date = '";
			Statement statement = null;
			try {
				statement = con.createStatement();
			}catch (SQLException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 365; i++) {
				if (days[startIndex] != 0) {
					String query = startOfQuery + today.plusDays(i).toString() + "';";
					try{
						statement.execute(query);
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
				startIndex++;
				startIndex = startIndex % 7;
			}
		}
	}
	
	void queryPriceManual(Listing listing, Scanner in) throws SQLException {
		int finished = 0;
		while (finished == 0) {
			System.out.println("ENTER A DATE TO UPDATE PRICE FOR");
			int year = 0;
			int month = 0;
			int day = 0;
			LocalDate today = LocalDate.now();
			while (true) {
				System.out.println("Year:");
				try {
					year = Integer.parseInt(in.nextLine());
					if (year < today.getYear()) {
						System.out.println("Please enter a valid year");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			while (true) {
				System.out.println("Month (1-12):");
				try {
					month = Integer.parseInt(in.nextLine());
					if (month < 1 || month > 12 || (year == today.getYear() && month < today.getMonthValue())) {
						System.out.println("Please enter a valid month");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			while (true) {
				System.out.println("Day:");
				try {
					day = Integer.parseInt(in.nextLine());
					if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day < 1 || day > 31)) {
						System.out.println("Please enter a valid day");
						continue;
					}
					else if ((month == 4 || month == 6 || month == 9 || month == 11) && (day < 1 || day > 30)) {
						System.out.println("Please enter a valid day");
						continue;
					}
					else if ((month == 2) && (day < 1 || day > 28)) {
						if (year % 4 == 0 && day == 29) {
							break;
						}
						else {
							System.out.println("Please enter a valid day");
							continue;
						}
					}
					else if (year == today.getYear() && month == today.getMonthValue() && day < today.getDayOfMonth()) {
						System.out.println("Please enter a valid day");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			
			LocalDate newAvailability = LocalDate.of(year, month, day);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = newAvailability.format(dateTimeFormatter);
			double priceInput = 0.00;
			
			while (true) {
				System.out.println("ENTER YOUR PRICE FOR ONE NIGHT ON " + formattedDate + " (XXX.XX):");
				try {
					priceInput = in.nextDouble();
					in.nextLine();
					break;
				}catch (NumberFormatException e) {
					System.out.println("Please enter a correct answer");
					continue;
				}
			}
			int delete = 0;
			while(true) {
				System.out.println("Are you sure you want to update price on " + formattedDate + "? Y/N");
				String input = in.nextLine();
				if (input.charAt(0) == 'y' || input.charAt(0) == 'Y') {
					delete = 1;
					break;
				}
				else if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			if (delete == 1) {
				Statement statement = null;
				try{
					statement = con.createStatement();
				}catch (SQLException e) {
					e.printStackTrace();
				}
				String alreadyExists = "update availability set price = " + Double.toString(priceInput) + " where l_latitude = " + Float.toString(listing.latitude) 
										+ " and l_longitude = " + Float.toString(listing.longitude) + " and date = '" + formattedDate + "';";
				try{
					statement.execute(alreadyExists);
					System.out.println("Updated price on " + formattedDate);
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			while (true) {
				System.out.println("[D]ONE, [M]ORE DATES:");
				String doneInput = in.nextLine();
				if (doneInput.charAt(0) == 'D' || doneInput.charAt(0) == 'd') {
					finished = 1;
					break;
				}
				else if (doneInput.charAt(0) == 'M' || doneInput.charAt(0) == 'm') {
					break;
				}
				else {
					System.out.println("Please enter a correct answer\n");
				}
			}
		}
	}
	
	void queryAvailManual(Listing listing, Scanner in) throws SQLException {
		// ENTERING AVAILABILITY MANUALLY
		int finished = 0;
		while (finished == 0) {
			System.out.println("ENTER A DATE THIS LISTING IS AVAILABLE");
			int year = 0;
			int month = 0;
			int day = 0;
			LocalDate today = LocalDate.now();
			while (true) {
				System.out.println("Year:");
				try {
					year = Integer.parseInt(in.nextLine());
					if (year < today.getYear()) {
						System.out.println("Please enter a valid year");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			while (true) {
				System.out.println("Month (1-12):");
				try {
					month = Integer.parseInt(in.nextLine());
					if (month < 1 || month > 12 || (year == today.getYear() && month < today.getMonthValue())) {
						System.out.println("Please enter a valid month");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			while (true) {
				System.out.println("Day:");
				try {
					day = Integer.parseInt(in.nextLine());
					if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day < 1 || day > 31)) {
						System.out.println("Please enter a valid day");
						continue;
					}
					else if ((month == 4 || month == 6 || month == 9 || month == 11) && (day < 1 || day > 30)) {
						System.out.println("Please enter a valid day");
						continue;
					}
					else if ((month == 2) && (day < 1 || day > 28)) {
						if (year % 4 == 0 && day == 29) {
							break;
						}
						else {
							System.out.println("Please enter a valid day");
							continue;
						}
					}
					else if (year == today.getYear() && month == today.getMonthValue() && day < today.getDayOfMonth()) {
						System.out.println("Please enter a valid day");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			
			LocalDate newAvailability = LocalDate.of(year, month, day);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = newAvailability.format(dateTimeFormatter);
			double priceInput = 0.00;
			
			while (true) {
				System.out.println("ENTER YOUR PRICE FOR ONE NIGHT ON " + formattedDate + " (XXX.XX):");
				try {
					priceInput = in.nextDouble();
					in.nextLine();
					break;
				}catch (NumberFormatException e) {
					System.out.println("Please enter a correct answer");
					continue;
				}
			}
			
			Statement statement = null;
			try{
				statement = con.createStatement();
			}catch (SQLException e) {
				e.printStackTrace();
			}
			String alreadyExists = "select * from availability where l_latitude = " + Float.toString(listing.latitude) + " and l_longitude = " 
									+ Float.toString(listing.longitude) + " and date = '" + formattedDate + "';";
			ResultSet rs = null;
			try{
				rs = statement.executeQuery(alreadyExists);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			if (rs.next() == false) {
				String availabilityQuery = "INSERT INTO availability VALUES (" + Float.toString(listing.latitude) + ", " 
											+ Float.toString(listing.longitude) + ", '" + formattedDate + "'," + priceInput + ");";
				try{
					statement.execute(availabilityQuery);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				while (true) {
					System.out.println("Availability added!");
					System.out.println("[D]ONE, [M]ORE DATES:");
					String doneInput = in.nextLine();
					if (doneInput.charAt(0) == 'D' || doneInput.charAt(0) == 'd') {
						finished = 1;
						break;
					}
					else if (doneInput.charAt(0) == 'M' || doneInput.charAt(0) == 'm') {
						break;
					}
					else {
						System.out.println("Please enter a correct answer\n");
					}
				}
			}
			else {
				System.out.println("Your listing is already available that day! Please enter another date");
			}
		}
	}
	
	void deleteAvailManual(Listing listing, Scanner in) throws SQLException {
		// ENTERING AVAILABILITY MANUALLY
		int finished = 0;
		while (finished == 0) {
			System.out.println("ENTER A DATE TO REMOVE AVAILABILITY");
			int year = 0;
			int month = 0;
			int day = 0;
			LocalDate today = LocalDate.now();
			while (true) {
				System.out.println("Year:");
				try {
					year = Integer.parseInt(in.nextLine());
					if (year < today.getYear()) {
						System.out.println("Please enter a valid year");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			while (true) {
				System.out.println("Month (1-12):");
				try {
					month = Integer.parseInt(in.nextLine());
					if (month < 1 || month > 12 || (year == today.getYear() && month < today.getMonthValue())) {
						System.out.println("Please enter a valid month");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			while (true) {
				System.out.println("Day:");
				try {
					day = Integer.parseInt(in.nextLine());
					if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day < 1 || day > 31)) {
						System.out.println("Please enter a valid day");
						continue;
					}
					else if ((month == 4 || month == 6 || month == 9 || month == 11) && (day < 1 || day > 30)) {
						System.out.println("Please enter a valid day");
						continue;
					}
					else if ((month == 2) && (day < 1 || day > 28)) {
						if (year % 4 == 0 && day == 29) {
							break;
						}
						else {
							System.out.println("Please enter a valid day");
							continue;
						}
					}
					else if (year == today.getYear() && month == today.getMonthValue() && day < today.getDayOfMonth()) {
						System.out.println("Please enter a valid day");
						continue;
					}
					break;
				} catch(Exception e) {
					System.out.println("Please enter a number");
				}
			}
			
			LocalDate newAvailability = LocalDate.of(year, month, day);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = newAvailability.format(dateTimeFormatter);
			int delete = 0;
			while(true) {
				System.out.println("Are you sure you want to remove availability on " + formattedDate + "? Y/N");
				String input = in.nextLine();
				if (input.charAt(0) == 'y' || input.charAt(0) == 'Y') {
					delete = 1;
					break;
				}
				else if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			if (delete == 1) {
				Statement statement = null;
				try{
					statement = con.createStatement();
				}catch (SQLException e) {
					e.printStackTrace();
				}
				String alreadyExists = "delete from availability where l_latitude = " + Float.toString(listing.latitude) 
										+ " and l_longitude = " + Float.toString(listing.longitude) + " and date = '" + formattedDate + "';";
				try{
					statement.execute(alreadyExists);
					System.out.println("Removed availability on " + formattedDate);
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			while (true) {
				System.out.println("[D]ONE, [M]ORE DATES:");
				String doneInput = in.nextLine();
				if (doneInput.charAt(0) == 'D' || doneInput.charAt(0) == 'd') {
					finished = 1;
					break;
				}
				else if (doneInput.charAt(0) == 'M' || doneInput.charAt(0) == 'm') {
					break;
				}
				else {
					System.out.println("Please enter a correct answer\n");
				}
			}
		}
	}
}

