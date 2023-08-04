package Reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import JDBC.Bookings.Booking;

public class ReportHub {
	Connection con;
	Scanner in;
	
	
	public ReportHub(Connection con, Scanner in) {
		this.con = con;
		this.in = in;
	}
	
	public void runHub() throws SQLException{
		// reports to include:
		String[] reports = {"Total Bookings by City", "Total Bookings by Postal Code", "Total Listings", 
				"Rank Hosts", "Rank Guests", "Check for Commercial Hosts", "Largest Cancellers", "Noun Phrases"};
		int finished = 0;
		while(finished == 0) {
			System.out.println("Select Report to Perform: ");
			for (int i = 1; i <= reports.length; i++) {
				System.out.println("[" + i + "]: " + reports[i - 1]);
			}
			while(true) {
				System.out.println("Enter the number of the report to perform:");
				if (in.hasNextInt()) {
					int choice = in.nextInt();
					in.nextLine();
					if (choice == 1) {
						cityDateRange();
					}
					else if (choice == 2) {
						cityCodeDateRange();
					}
					else if (choice == 3) {
						chooseListingReport();
					}
					else if (choice == 4) {
						chooseHostListingCount();
					}
					else if (choice == 5) {
						chooseGuestBookingCount();
					}
					else if (choice == 6) {
						
					}
					else if (choice == 7) {
						countCancellers();
					}
					else if (choice == 8) {
						
					}
				}
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
				break;
			}
			while(true) {
				System.out.println("Perform another report? Y/N");
				String input = in.nextLine();
				if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
					break;
				}
				else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
					finished = 1;
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
		}
	}
	
	void chooseGuestBookingCount() throws SQLException {
		while(true) {
			System.out.println("How would you like to count host listings?\n[1]: Time Period\n[2]: Time Period and City");
			if (in.hasNextInt()) {
				int choice = in.nextInt();
				in.nextLine();
				if (choice == 1) {
					guestBookingPeriod();
				}
				else if (choice == 2) {
					guestBookingPeriodCity();
				}
			}
			else {
				System.out.println("Please enter a valid input");
				continue;
			}
			break;
		}
	}
	
	void guestBookingPeriod() throws SQLException {
		String[] dates = retrieveDates();
		String query = "select user.f_name, user.l_name, count(*) from user, booking, listing where "
				+ "booking.l_latitude = listing.latitude and booking.l_longitude = listing.longitude "
				+ "and booking.date >= '" + dates[0] + "' and booking.e_date <= '" + dates[1] + "' "
				+ "and user.sin = booking.user group by user.f_name, user.l_name order by count(*) desc;";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("f_name") + " " + rs.getString("l_name")+ ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void guestBookingPeriodCity () throws SQLException {
		String[] dates = retrieveDates();
		System.out.println("Enter the city you wish to report on:");
		String city = in.nextLine();
		String query = "select user.f_name, user.l_name, count(*) from user, booking, listing where "
				+ "booking.l_latitude = listing.latitude and booking.l_longitude = listing.longitude "
				+ "and booking.date >= '" + dates[0] + "' and booking.e_date <= '" + dates[1] + "' "
				+ "and listing.city = '" + city + "' "
				+ "and user.sin = booking.user group by user.f_name, user.l_name order by count(*) desc;";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("f_name") + " " + rs.getString("l_name")+ ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void chooseHostListingCount() throws SQLException {
		while(true) {
			System.out.println("How would you like to count host listings?\n[1]: Country\n[2]: Country and City");
			if (in.hasNextInt()) {
				int choice = in.nextInt();
				in.nextLine();
				if (choice == 1) {
					hostListingCountry();
				}
				else if (choice == 2) {
					hostListingCountryCity();
				}
			}
			else {
				System.out.println("Please enter a valid input");
				continue;
			}
			break;
		}
	}
	
	void hostListingCountry() throws SQLException {
		System.out.println("Enter the country you wish to report on:");
		String country = in.nextLine();
		String query = "select user.f_name, user.l_name, count(*) from user, listing where user.sin = listing.host and listing.country = '" + country + "' group by user.f_name, user.l_name order by count(*) desc;";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("f_name") + " " + rs.getString("l_name")+ ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void hostListingCountryCity() throws SQLException {
		System.out.println("Enter the country you wish to report on:");
		String country = in.nextLine();
		System.out.println("Enter the city you wish to report on:");
		String city = in.nextLine();
		String query = "select user.f_name, user.l_name, count(*) from user, listing where user.sin = listing.host and listing.country = '" + country + "' and listing.city = '" + city + "' group by user.f_name, user.l_name order by count(*) desc;";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("f_name") + " " + rs.getString("l_name")+ ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void countCancellers() throws SQLException {
		LocalDateTime startDate = GetDate();
		LocalDateTime endDate = startDate.plusYears(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedStartDate = startDate.format(dateTimeFormatter);
		String formattedEndDate = endDate.format(dateTimeFormatter);
		
		String userQuery = "select user.f_name, user.l_name, count(*) from cancel_booking, user where user.sin = "
				+ "cancel_booking.user and date >= '" + formattedStartDate + "' and date <= '" + formattedEndDate + "' and canceller = 1 "
				+ "group by user.f_name, user.l_name order by count(*) desc";
		String hostQuery = "select user.f_name, user.l_name, count(*) from cancel_booking, listing, user where "
				+ "cancel_booking.l_latitude = listing.latitude and listing.longitude = cancel_booking.l_longitude "
				+ "and user.sin = listing.host and date >= '" + formattedStartDate + "' and date <= '" + formattedEndDate + "' "
				+ "and canceller = 0 group by user.f_name, user.l_name order by count(*) desc";
		ResultSet userResults = runQuery(userQuery);
		ResultSet hostResults = runQuery(hostQuery);
		System.out.println("Report Results\nUser Results\n-------------------");
		while (userResults.next()) {
			System.out.println("| " + userResults.getString("f_name") + " " + userResults.getString("l_name")+ ": " + userResults.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
		System.out.println("Host Results\n-------------------");
		while (userResults.next()) {
			System.out.println("| " + hostResults.getString("f_name") + " " + hostResults.getString("l_name")+ ": " + hostResults.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void cityDateRange() throws SQLException {
		String[] dates = retrieveDates();
		String query = "select city, count(*) from booking, listing where booking.l_latitude = listing.latitude "
				+ "and booking.l_longitude = listing.longitude "
				+ "and booking.date >= '" + dates[0] + "' and booking.e_date <= '" + dates[1] + "' group by city;";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("city") + ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void cityCodeDateRange() throws SQLException {
		System.out.println("Enter the city you wish to report on:");
		String input = in.nextLine();
		String[] dates = retrieveDates();
		String query = "select p_code, count(*) from booking, listing where booking.l_latitude = listing.latitude "
				+ "and booking.l_longitude = listing.longitude and listing.city = '" + input + "' "
				+ "and booking.date >= '" + dates[0] + "' and booking.e_date <= '" + dates[1] + "' group by p_code;";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("p_code") + ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void chooseListingReport() throws SQLException {
		int finished = 0;
		while(finished == 0) {
			System.out.println("Get Total Listings By\n[1]: Country\n[2]: Country and City\n[3]: Country and City and Postal Code");
			while(true) {
				System.out.println("Enter the number of the report to perform:");
				if (in.hasNextInt()) {
					int choice = in.nextInt();
					in.nextLine();
					if (choice == 1) {
						totalListingsCountry();
					}
					else if (choice == 2) {
						totalListingsCountryCity();
					}
					else if (choice == 3) {
						totalListingsCountryCityCode();
					}
				}
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
				break;
			}
			while(true) {
				System.out.println("Perform another listing report? Y/N");
				String input = in.nextLine();
				if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
					break;
				}
				else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
					finished = 1;
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
		}
	}
	
	void totalListingsCountry() throws SQLException {
		String query = "select country, count(*) from listing where removed = 0 group by country";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("country") + ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void totalListingsCountryCity() throws SQLException {
		String query = "select country, city, count(*) from listing where removed = 0 group by country, city order by city desc";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("city") + " " + rs.getString("country") + ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	void totalListingsCountryCityCode() throws SQLException {
		String query = "select country, city, p_code, count(*) from listing where removed = 0 group by country, city, p_code order by city, p_code desc";
		ResultSet rs = runQuery(query);
		System.out.println("Report Results\n-------------------");
		while (rs.next()) {
			System.out.println("| " + rs.getString("city") + " " + rs.getString("p_code") + " " + rs.getString("country") + ": " + rs.getInt("count(*)") + " |");
		}
		System.out.println("-------------------");
	}
	
	public ResultSet runQuery(String query) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public String[] retrieveDates() {
		LocalDateTime startDate;
		LocalDateTime endDate;
		long numDays = 0;
		while(true) {
			System.out.println("Enter the start of your date range:");
			startDate = GetDate();
			System.out.println("Enter the end of your date range:");
			endDate = GetDate();
			numDays = Duration.between(startDate, endDate).toDays();
			if (numDays < 1) {
				System.out.println("Please enter a date range greater than 0");
			}
			else {
				break;
			}
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedStartDate = startDate.format(dateTimeFormatter);
		String formattedEndDate = endDate.format(dateTimeFormatter);
		
		
		String[] dates = {formattedStartDate, formattedEndDate};
		return dates;
	}
	
	
	public LocalDateTime GetDate() {
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
				if (month < 1 || month > 12) {
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
				break;
			} catch(Exception e) {
				System.out.println("Please enter a number");
			}
		}
		return LocalDateTime.of(year, month, day, 0, 0);
	}
}
