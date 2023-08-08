package JDBC.Listings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
			numListings = 0;
			// retrieve my listings
			Statement statement = null;
			try {
				statement = con.createStatement();
			}catch (SQLException e) {
				e.printStackTrace();
			}
			String query = "select * from listing, host where removed = 0 and host = '" + user.SIN + "' and host.h_sin = listing.host;";
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(query);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("MY LISTINGS:");
			Boolean frozen = false;
			if (rs.next() == true) {
				frozen = rs.getBoolean("frozen");
				ListingPrinter printer = new ListingPrinter(this.user, this.con);
				Listing ls = new Listing();
				listings.add(ls.setListing(rs));
				System.out.println("[" + Integer.toString(numListings) + "]");
				ls.printListing();
				System.out.println("");
				numListings++;
				numListings += printer.printMyListings(listings, rs);
				
				// control given to user
				if (frozen == true) {
					System.out.println("YOUR ACCOUNT IS FROZEN FOR POTENTIALLY BEING COMMERCIAL\nTHESE LISTINGS WILL NOT APPEAR FOR OTHER USERS");
				}
				System.out.println("[C]reate New Listing, [E]dit a Listing, [D]elete a Listing, [V]iew Bookings, [G]o Back");
				String input = in.nextLine();
				
				if (input.equals("C") || input.equals("c")) {
					Listing listing = new Listing();
					listing.makeListing(in, user, con);
					QueryForAvailability(listing, in);
				}
				else if (input.equals("E") || input.equals("e")) {
					QueryEditListing(in);
					break;
				}
				else if (input.equals("D") || input.equals("d")) {
					QueryDeleteListing(in);
					break;
				}
				else if (input.equals("G") || input.equals("g")) {
					break;
				}
				else if (input.equals("V") || input.equals("v")) {
					// print all the bookings for the listing
					viewBookings(in);
					break;
				}
			}
			else {
				// control given to user
				System.out.println("[C]reate New Listing, [G]o Back");
				String input = in.nextLine();
				
				if (input.equals("C") || input.equals("c")) {
					Listing listing = new Listing();
					listing.makeListing(in, user, con);
					QueryForAvailability(listing, in);
				}
				else if (input.equals("g") || input.equals("g")) {
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
					System.out.println("ARE YOU SURE YOU WANT TO DELETE THIS LISTING? IT WILL CANCEL ALL BOOKINGS. Y/N");
						String input = in.nextLine();
						if (input.equals("Y") || input.equals("y")) {
							Statement deleter = con.createStatement();
							String deleteQuery = "delete from availability where l_latitude = " + chosen.latitude
													+ " and l_longitude = " + chosen.longitude + ";";
							String bookingFind = "select * from booking where l_latitude = " + chosen.latitude
											+ " and l_longitude = " + chosen.longitude + ";";
							String bookingDelete = "delete from booking where l_latitude = " + chosen.latitude
													+ " and l_longitude = " + chosen.longitude + ";";
							try {
								ResultSet rs = deleter.executeQuery(bookingFind);
								Statement hooey = con.createStatement();
								while (rs.next() == true) {
									String executer = "insert into cancel_booking values (" + chosen.latitude + ", " + chosen.longitude + ", '" + rs.getString("date") + "', "
											+ rs.getString("user") + ", '" + rs.getString("e_date") + "', 0);";
									hooey.execute(executer);
								}
								deleter.execute(bookingDelete);
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
							deleteQuery = "update listing set removed = 1 where latitude = " + chosen.latitude
							+ " and longitude = " + chosen.longitude + ";";
							try {
								deleter.execute(deleteQuery);
							}catch (SQLException e) {
								e.printStackTrace();
							}
							break;
						}
						else if (input.equals("N") || input.equals("n")) {
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
				if (input.equals("E") || input.equals("e")) {
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
						if (input.equals("T") || input.equals("t")) {
							updateType(chosen, in);
						}
						else if (input.equals("A") || input.equals("a")) {
							updateAvailability(chosen, in);
						}
						else if (input.equals("E") || input.equals("e")) {
							updateAmenities(chosen, in);
						}
						else if (input.equals("D") || input.equals("d")) {
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
				if (input.equals("E") || input.equals("e")) {
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
				String listingQuery = "select * from booking, user where booking.user = user.sin and l_latitude = " + chosen.latitude + " and l_longitude = " 
										+ chosen.longitude + " and date >= '" + formattedNow + "';";
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
				
				
				if (numBookings > 0) {
					while (true) {
						System.out.println("[C]ancel Booking, [G]o Back");
							String input = in.nextLine();
							if (input.equals("C") || input.equals("c")) {
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
												if (input.equals("Y") || input.equals("y")) {
													Statement deleter = con.createStatement();
													String deleteQuery = "delete from booking where l_latitude = " + chose.latitude 
																			+ " and l_longitude = " + chose.longitude + " and user = '" + usage.SIN 
																			+ "' and date = '" + chose.date + "';";
													String insertQuery = "insert into cancel_booking values ("+ chose.latitude
																			+ ", " + chose.longitude + ", '" + chose.date 
																			+ "', '" + usage.SIN + "', '" + chose.e_date + "', 0);";
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
												else if (input.equals("N") || input.equals("n")) {
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
										if (input.equals("E") || input.equals("e")) {
											break;
										}
										
										else {
											System.out.println("Please enter a valid input");
											continue;
										}
									}
								}
							}
							else if (input.equals("G") || input.equals("g")) {
								break;
							}
							else {
								System.out.println("Please enter a valid input");
								continue;
							}
					}
				}
				else {
					while(true) {
						System.out.println("This listing has no bookings, [G]o back");
						String input = in.nextLine();
						if (input.equals("G") || input.equals("g")) {
							break;
						}
						else {
							System.out.println("Please enter a valid input");
						}
					}
				}
			}
			else {
				String input = in.nextLine();
				if (input.equals("E") || input.equals("e")) {
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
											+ " where latitude = " + listing.latitude 
											+ "and longitude = " + listing.longitude + ";";
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
			if (input.equals("A") || input.equals("a")) {
				while(true) {
					System.out.println("[A]dd availability, [R]emove availability, [S]tart fresh, [E]xit");
					input = in.nextLine();
					if (input.equals("A") || input.equals("a")) {
						queryAvailManual(listing, in);
					}
					else if (input.equals("R") || input.equals("r")) {
						deleteAvailability(listing, in);
					}
					else if (input.equals("S") || input.equals("s")) {
						LocalDate today = LocalDate.now();
						DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formattedDate = today.format(dateTimeFormatter);
						try {
							Statement statement = con.createStatement();
							ResultSet rs = null;
							String checkBooking = "select * from booking where l_latitude = " + listing.latitude + " and l_longitude = " + listing.longitude
									+ " and e_date >= '" + formattedDate + "';";
							rs = statement.executeQuery(checkBooking);
							if (rs.next() == true) {
								System.out.println("Cannot start fresh because you have bookings for this listing");
								continue;
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						String delete = "delete from availability where l_latitude = " + listing.latitude
											+ " and l_longitude = " + listing.longitude + ";";
						Statement statement = con.createStatement();
						try {
							statement.execute(delete);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						QueryForAvailability(listing, in);
					}
					else if (input.equals("E") || input.equals("e")) {
						break;
					}
					else {
						System.out.println("Please enter a valid input");
					}
				}
			}
			else if (input.equals("P") || input.equals("p")) {
				updatePrice(listing, in);
			}
			else if (input.equals("E") || input.equals("e")) {
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
			String input = in.nextLine();
			if (input.equals("E") || input.equals("e")) {
				listing.type = "Entire Place";
				break;
			}
			else if (input.equals("P") || input.equals("p")) {
				listing.type = "Private Room";
				break;
			}
			else if (input.equals("H") || input.equals("h")) {
				listing.type = "Hotel Room";
				break;
			}
			else if (input.equals("s") || input.equals("S")) {
				listing.type = "Shared Room";
				break;
			}
			else {
				System.out.println("Please enter a correct answer\n");
			}
		}
		Statement update = con.createStatement();
		String query = "update listing set type = '" + listing.type + "' where latitude = " + listing.latitude 
							+ " and longitude = " + listing.longitude + ";";
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
			String input = in.nextLine();
			if (input.equals("W") || input.equals("w")) {
				queryPriceWeekday(listing, in);
				break;
			}
			else if (input.equals("Y") || input.equals("y")) {
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
			String input = in.nextLine();
			if (input.equals("W") || input.equals("w")) {
				queryAvailWeekday(listing, in);
				break;
			}
			else if (input.equals("M") || input.equals("m")) {
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
			String input = in.nextLine();
			if (input.equals("W") || input.equals("w")) {
				deleteAvailWeekday(listing, in);
				break;
			}
			else if (input.equals("M") || input.equals("m")) {
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
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = today.format(dateTimeFormatter);
		ResultSet rs = checkBooking(formattedDate, listing);
		if (rs.next() == true) {
			System.out.println("Cannot do weekday price updates because you have bookings for this listing");
			return;
		}
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
				if (input.equals("E") || input.equals("e")) {
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
			
			DayOfWeek dayofweek = today.getDayOfWeek();
			int startIndex = dayofweek.getValue() - 1;
			String startOfQuery = "update availability set price = " + Double.toString(priceInput) + " where l_latitude =  "
									+ listing.latitude + " and l_longitude = " + listing.longitude
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
				String input = in.nextLine();
				if (input.equals("Y") || input.equals("y")) {
					days[i] = 1;
					break;
				}
				else if (input.equals("N") || input.equals("n")) {
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
					Statement avgPrice = con.createStatement();
					String priceAvg = "select avg(price) from availability, listing where latitude = l_latitude and longitude = "
							+ "l_longitude group by type, date having type = '" + listing.type + "' and DAYOFWEEK(date) = " + (i + 1) + ";";
					try {
						ResultSet resultAvg = avgPrice.executeQuery(priceAvg);
						resultAvg.next();
						String avg = resultAvg.getString("avg(price)");
						System.out.println("The average price on " + dayStrings[i] + " for your type of listing is " + avg);
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
		String startOfQuery = "INSERT INTO availability VALUES (" + listing.latitude + ", " + listing.longitude + ", '";
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
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = today.format(dateTimeFormatter);
		try {
			Statement statement = con.createStatement();
			ResultSet rs = null;
			String checkBooking = "select * from booking where l_latitude = " + listing.latitude + " and l_longitude = " + listing.longitude
					+ " and e_date >= '" + formattedDate + "';";
			rs = statement.executeQuery(checkBooking);
			if (rs.next() == true) {
				System.out.println("Cannot do weekday availability updates because you have bookings for this listing");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
				if (input.equals("E") || input.equals("e")) {
					exit = 1;
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
		}
		if (exit == 0) {
			DayOfWeek dayofweek = today.getDayOfWeek();
			int startIndex = dayofweek.getValue() - 1;
			String startOfQuery = "delete from availability where l_latitude = " + listing.latitude 
									+ " and l_longitude = " + listing.longitude + " and date = '";
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
			LocalDateTime newAvailability = GetDate(in);
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
				if (input.equals("Y") || input.equals("y")) {
					delete = 1;
					break;
				}
				else if (input.equals("N") || input.equals("n")) {
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
					ResultSet rs = checkBooking(formattedDate, listing);
					if (rs.next() == false) {
						String alreadyExists = "update availability set price = " + Double.toString(priceInput) + " where l_latitude = " + listing.latitude 
											+ " and l_longitude = " + listing.longitude + " and date = '" + formattedDate + "';";
						statement.execute(alreadyExists);
						System.out.println("Updated price on " + formattedDate);
					}
					else {
						System.out.println("Cannot update price on this date, a booking is scheduled for it");
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			while (true) {
				System.out.println("[D]ONE, [M]ORE DATES:");
				String doneInput = in.nextLine();
				if (doneInput.equals("D") || doneInput.equals("d")) {
					finished = 1;
					break;
				}
				else if (doneInput.equals("M") || doneInput.equals("m")) {
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
			LocalDateTime newAvailability = GetDate(in);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = newAvailability.format(dateTimeFormatter);
			double priceInput = 0.00;
			
			while (true) {
				System.out.println("ENTER YOUR PRICE FOR ONE NIGHT ON " + formattedDate + " (XXX.XX):");
				Statement avgPrice = con.createStatement();
				String priceAvg = "select avg(price) from availability, listing where latitude = l_latitude and longitude = "
						+ "l_longitude group by type, date having type = '" + listing.type + "';";
				try {
					ResultSet resultAvg = avgPrice.executeQuery(priceAvg);
					resultAvg.next();
					String avg = resultAvg.getString("avg(price)");
					System.out.println("The average price for your type of listing is " + avg);
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
			String alreadyExists = "select * from availability where l_latitude = " + listing.latitude + " and l_longitude = " 
									+ listing.longitude + " and date = '" + formattedDate + "';";
			ResultSet rs = null;
			try{
				rs = statement.executeQuery(alreadyExists);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			if (rs.next() == false) {
				String availabilityQuery = "INSERT INTO availability VALUES (" + listing.latitude + ", " 
											+ listing.longitude + ", '" + formattedDate + "'," + priceInput + ");";
				try{
					statement.execute(availabilityQuery);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				while (true) {
					System.out.println("Availability added!");
					System.out.println("[D]ONE, [M]ORE DATES:");
					String doneInput = in.nextLine();
					if (doneInput.equals("D") || doneInput.equals("d")) {
						finished = 1;
						break;
					}
					else if (doneInput.equals("M") || doneInput.equals("m")) {
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
			LocalDateTime newAvailability = GetDate(in);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = newAvailability.format(dateTimeFormatter);
			int delete = 0;
			while(true) {
				System.out.println("Are you sure you want to remove availability on " + formattedDate + "? Y/N");
				String input = in.nextLine();
				if (input.equals("Y") || input.equals("y")) {
					delete = 1;
					break;
				}
				else if (input.equals("N") || input.equals("n")) {
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
					ResultSet rs = checkBooking(formattedDate, listing);
					if (rs.next() == false) {
						String alreadyExists = "delete from availability where l_latitude = " + listing.latitude
											+ " and l_longitude = " + listing.longitude + " and date = '" + formattedDate + "';";
						statement.execute(alreadyExists);
						System.out.println("Removed availability on " + formattedDate);
					}
					else {
						System.out.println("Cannot remove availability on this date, there is a booking scheduled");
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			while (true) {
				System.out.println("[D]ONE, [M]ORE DATES:");
				String input = in.nextLine();
				if (input.equals("D") || input.equals("d")) {
					finished = 1;
					break;
				}
				else if (input.equals("M") || input.equals("m")) {
					break;
				}
				else {
					System.out.println("Please enter a correct answer\n");
				}
			}
		}
	}
	
	public LocalDateTime GetDate(Scanner in) {
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
		return LocalDateTime.of(year, month, day, 0, 0);
	}
	
	ResultSet checkBooking(String formattedDate, Listing listing) {
		try {
			Statement statement = con.createStatement();
			ResultSet rs = null;
			String checkBooking = "select * from booking where l_latitude = " + listing.latitude + " and l_longitude = " + listing.longitude
					+ " and date <= '" + formattedDate + "' and e_date >= '" + formattedDate + "';";
			rs = statement.executeQuery(checkBooking);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

