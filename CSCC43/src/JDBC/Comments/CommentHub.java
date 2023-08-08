package JDBC.Comments;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import JDBC.Bookings.BookingHub;
import JDBC.Bookings.BrowseListings;
import JDBC.Listings.ListingHub;
import JDBC.UserDetails.Profile;
import JDBC.UserDetails.User;

public class CommentHub {
	User user;
	Connection con;
	
	public CommentHub(User user, Connection con) {
		this.user = user;
		this.con = con;
	}
	
	
	public void runHub(Scanner in) throws SQLException {
		while (true) {
			System.out.println("[R]atings for me, [H]ost Ratings, [G]uest Ratings, [C]reate Rating, [E]xit");
			String input = in.nextLine();
			if (input.equals("R")|| input.equals("r")) {
				// see the comments left for me
				printRatings("select * from h_review, user where host = " + user.SIN + " and h_review.user = user.SIN;", "From ");
			}
			else if (input.equals("H")|| input.equals("h")) {
				// see the comments I left for hosts
				printRatings("select * from h_review, user where user = " + user.SIN + " and h_review.host = user.SIN;", "For ");
			}
			else if (input.equals("G")|| input.equals("g")) {
				// see the comments I left for guests
				printRatings("select * from u_review, user where host = " + user.SIN + " and u_review.user = user.SIN;", "For ");
			}
			else if (input.equals("C")|| input.equals("c")) {
				// create a comment
				System.out.println("Create a review for: [G]uest of your listing, [H]ost of your stay, [E]xit");
				input = in.nextLine();
				if (input.equals("E")|| input.equals("e")) {
					break;
				}
				if (input.equals("H")|| input.equals("h")) {
					// create a review for a host
					createReview(in);
				}
				else if (input.equals("G")|| input.equals("g")) {
					// create a review for your guest
					createGuestReview(in);
				}
			}
			else if (input.equals("E")|| input.equals("e")) {
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
	}
	
	public void createReview(Scanner in) throws SQLException{
		// list all hosts that this person had in the last 30 days
		
		// calculate the date of 30 days ago
		LocalDate today = LocalDate.now();
		LocalDate thirty = today.minusDays(30);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedThirty = thirty.format(dateTimeFormatter);
		String formattedNow = today.format(dateTimeFormatter);
		
		// grab all the bookings during that time, pair it with the listings and user, and return the host of the listing
		String query = "select f_name, l_name, L.latitude, L.longitude, L.str_addr, U.str_addr, date, e_date, sin from booking, listing L, user U where booking.e_date > '" + formattedThirty 
				+ "' and booking.e_date < '" + formattedNow + "' and L.host = U.sin and booking.user = '" + user.SIN + "' and booking.user != U.sin and booking.l_latitude = latitude and booking.l_longitude = longitude;";
		Statement statement = con.createStatement();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(query);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		// print those hosts and their listings out
		int numHosts = 0;
		ArrayList<String> hosts = new ArrayList<String>();
		ArrayList<String> latitudes = new ArrayList<String>(); 
		ArrayList<String> longitudes = new ArrayList<String>(); 
		while(rs.next() == true) {
			numHosts++;
			System.out.println("--------------------");
			System.out.println("[" + Integer.toString(numHosts) + "]");
			System.out.println("Host: " + rs.getString("f_name") + " " + rs.getString("l_name"));
			System.out.println("Stayed at " + rs.getString("str_addr") + " from " + rs.getString("date") + " to " + rs.getString("e_date"));
			hosts.add(rs.getString("sin"));
			latitudes.add(rs.getString("latitude"));
			longitudes.add(rs.getString("longitude"));
		}
		System.out.println("--------------------");
		// give the user a choice
		if (numHosts > 0) {
			while(true) {
				System.out.println("Enter the number of the Host to Review:");
				if (in.hasNextInt()) {
					int hostNum = in.nextInt();
					in.nextLine();
					if (hostNum < 0 || hostNum > numHosts) {
						System.out.println("Please enter a valid listing number");
						continue;
					}
					String chosen = hosts.get(hostNum - 1);
					String chosenLat = latitudes.get(hostNum - 1);
					String chosenLon = longitudes.get(hostNum - 1);
					String hostQuery = "select f_name, l_name from user where sin = " + chosen + ";";
					try {
						rs = statement.executeQuery(hostQuery);
					}catch (SQLException e) {
						e.printStackTrace();
					}
					rs.next();
					System.out.println("Chosen Host: " + rs.getString("f_name") + " " + rs.getString("l_name"));
					while(true) {
						System.out.println("Review this Host? Y/N");
						String input = in.nextLine();
						if (input.equals("Y")|| input.equals("y")) {
							// create the review for that host
							hostReview(in, chosen, chosenLat, chosenLon);
							break;
						}
						if (input.equals("N")|| input.equals("n")) {
							break;
						}
						else {
							System.out.println("Please enter a valid input");
							continue;
						}
					}
					break;
				}
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
			}
		}
	}
	
	public void hostReview(Scanner in, String host, String lat, String lon) throws SQLException{
		while (true) {
			System.out.println("How many stars do you give this host? (1-5)");
			if (in.hasNextInt()) {
				int stars = in.nextInt();
				in.nextLine();
				if (stars < 1 || stars > 5) {
					System.out.println("Please enter a valid rating number");
					continue;
				}
				System.out.println("Leave your comments about the host here:");
				String comments = in.nextLine();
				String query = "insert into h_review values ('" + host + "', " + Integer.toString(stars) + ", '" + user.SIN + "', '" + comments + "', "
						+ lat + ", " + lon + ");";
				Statement statement = con.createStatement();
				try {
					statement.execute(query);
					System.out.println("Review Created!");
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			}
			else {
				System.out.println("Please enter a valid input");
				continue;
			}
		}
	}
	
	public void createGuestReview(Scanner in) throws SQLException{
		// list all guests that this person had in the last 30 days
	
		// calculate the date of 30 days ago
		LocalDate today = LocalDate.now();
		LocalDate thirty = today.minusDays(30);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedThirty = thirty.format(dateTimeFormatter);
		String formattedNow = today.format(dateTimeFormatter);
		
		// grab all the bookings during that time, pair it with the listings and user, and return the guests of the listing
		String query = "select f_name, l_name, L.latitude, L.longitude, L.str_addr, U.str_addr, date, e_date, sin from booking, listing L, user U "
				+ "where booking.e_date > '" + formattedThirty  + "' and booking.e_date < '" + formattedNow 
				+ "' and L.host = " + user.SIN + " and booking.user = U.sin and booking.l_latitude = L.latitude and booking.l_longitude = L.longitude;";
		Statement statement = con.createStatement();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(query);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		// print those guests and their listings out
		int numGuests= 0;
		ArrayList<String> guests = new ArrayList<String>();
		ArrayList<String> latitudes = new ArrayList<String>(); 
		ArrayList<String> longitudes = new ArrayList<String>(); 
		while(rs.next() == true) {
			numGuests++;
			System.out.println("--------------------");
			System.out.println("[" + Integer.toString(numGuests) + "]");
			System.out.println("Guest: " + rs.getString("f_name") + " " + rs.getString("l_name"));
			System.out.println("Stayed at " + rs.getString("L.str_addr") + " from " + rs.getString("date") + " to " + rs.getString("e_date"));
			guests.add(rs.getString("sin"));
			latitudes.add(rs.getString("latitude"));
			longitudes.add(rs.getString("longitude"));
		}
		System.out.println("--------------------");
		// give the user a choice
		if (numGuests > 0) {
			while(true) {
				System.out.println("Enter the number of the Guest to Review:");
				if (in.hasNextInt()) {
					int guestNum = in.nextInt();
					in.nextLine();
					if (guestNum < 0 || guestNum > numGuests) {
						System.out.println("Please enter a valid listing number");
						continue;
					}
					String chosen = guests.get(guestNum - 1);
					String chosenLat = latitudes.get(guestNum - 1);
					String chosenLon = longitudes.get(guestNum - 1);
					String guestQuery = "select f_name, l_name from user where sin = " + chosen + ";";
					try {
						rs = statement.executeQuery(guestQuery);
					}catch (SQLException e) {
						e.printStackTrace();
					}
					rs.next();
					System.out.println("Chosen Guest: " + rs.getString("f_name") + " " + rs.getString("l_name"));
					while(true) {
						System.out.println("Review this Guest? Y/N");
						String input = in.nextLine();
						if (input.equals("Y")|| input.equals("y")) {
							// create the review for that host
							guestReview(in, chosen, chosenLat, chosenLon);
							break;
						}
						if (input.equals("N")|| input.equals("n")) {
							break;
						}
						else {
							System.out.println("Please enter a valid input");
							continue;
						}
					}
					break;
				}
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
			}
		}
	}
	public void guestReview(Scanner in, String guest, String lat, String lon) throws SQLException{
		while (true) {
			System.out.println("How many stars do you give this guest? (1-5)");
			if (in.hasNextInt()) {
				int stars = in.nextInt();
				in.nextLine();
				if (stars < 1 || stars > 5) {
					System.out.println("Please enter a valid rating number");
					continue;
				}
				System.out.println("Leave your comments about the host here:");
				String comments = in.nextLine();
				String query = "insert into u_review values ('" + guest + "', " + Integer.toString(stars) + ", '" + user.SIN + "', '" + comments + "', "
						+ lat + ", " + lon + ");";
				Statement statement = con.createStatement();
				try {
					statement.execute(query);
					System.out.println("Review Created!");
				}catch(SQLException e) {
					e.printStackTrace();
				}
				break;
			}
			else {
				System.out.println("Please enter a valid input");
				continue;
			}
		}
	}
	
	public void printRatings(String query, String from) throws SQLException{
		Statement statement = con.createStatement();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(query);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		int numCom = 0;
		while (rs.next() != false) {
			numCom++;
			System.out.println("-------------------");
			System.out.println("[" + Integer.toString(numCom) + "]");
			System.out.println(from + rs.getString("f_name") + " " + rs.getString("l_name") + ":");
			System.out.println("Stars: " + rs.getInt("stars") + "/5");
			System.out.println("Comments: \n" + rs.getString("comments"));
		}
		System.out.println("-------------------");
		statement.close();
		rs.close();
	}
}
