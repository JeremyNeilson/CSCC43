package JDBC.Bookings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.Duration;

import JDBC.UserDetails.Payment;
import JDBC.UserDetails.User;

public class Booking {
	float latitude;
	float longitude;
	public String date;
	public String e_date;
	String SIN;
	User user;
	
	public Booking() {
		latitude = 0.00f;
		longitude = 0.00f;
		date = "";
		user = null;
		SIN = "";
	}
	
	public Booking(float latitude, float longitude, String date, User user) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
		this.user = user;
		this.SIN = user.SIN;
	}
	
	public Booking SetBooking(ResultSet rs) throws SQLException {
		this.latitude = Float.parseFloat(rs.getString("l_latitude"));
		this.longitude = Float.parseFloat(rs.getString("l_longitude"));
		this.date = rs.getString("date");
		this.SIN = rs.getString("user");
		this.e_date = rs.getString("e_date");
		return this;
	}
	
	public void PrintBooking() {
		System.out.println("---------------------");
		// print the booking, need to retrieve listing info methinks
		System.out.println("Staying from " + date + " to " + e_date + " at:");
	}
	
	public void MakeBooking(Scanner in, Connection con) throws SQLException{
		while(true) {
			// ask for a date range
			System.out.println("\n\nBOOKING CREATION");
			System.out.println("What day would you like to start your booking?");
			LocalDateTime startDate = GetDate(in);
			System.out.println("What day would you like to stay until?");
			LocalDateTime endDate = GetDate(in);
			System.out.println("Start date: " + startDate.toString());
			System.out.println("End date: " + endDate.toString());
			
			// check for that date range
			long numDays = Duration.between(startDate, endDate).toDays();
			
			// format the dates
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedStartDate = startDate.format(dateTimeFormatter);
			String formattedEndDate = endDate.format(dateTimeFormatter);
			
			// form and execute the query to check that the number of available days matches the number of wanted days
			String query = "select COUNT(*), SUM(price) from availability where l_latitude = " + Float.toString(latitude) + " and l_longitude = " + Float.toString(longitude)
				+ " and date >= '" + formattedStartDate + "' and date <= '" + formattedEndDate + "';";

			Statement querible = con.createStatement();
			ResultSet rs = null;
			rs = querible.executeQuery(query);
			rs.next();
			long actualDays = Long.parseLong(rs.getString("count(*)"));
			if (numDays != actualDays - 1)
			{
				System.out.println("Apologies, but this booking is not offered for the entirety of that period, please try again");
				continue;
			}
			else {
				// booking is available, now check if it's already booked
				String price = rs.getString("sum(price)");
				query = "select * from booking where l_latitude = " + Float.toString(latitude) + " and l_longitude = " + Float.toString(longitude)
						+ " and (date >= '" + formattedStartDate + "' and date <= '" + formattedEndDate + "' or "
								+ "e_date >= '" + formattedStartDate + "' and e_date <= '" + formattedEndDate + "');";
				rs = querible.executeQuery(query);
				
				// either no booking exists, or one does so can't book it
				if (rs.next() != false) {
					System.out.println("There is already a booking from " + rs.getString("date") + " to " + rs.getString("e_date"));
					System.out.println("Please enter another date range");
					continue;
				}
				else {
					// Getting here means that the booking is valid
					System.out.println("Total price for this booking is: " + price);
					System.out.println("Do you want to book this? Y/N");
					char input = in.nextLine().charAt(0);
					
					// integer that cancels the booking if the user doesn't enter payment information
					int wantBook = 1;
					if (input == 'Y' || input == 'y') {
						
						// Retrieve payment information
						String getInfo = "select c_card from user where sin = '" + user.SIN + "';";
						String paymentCard = null;
						try{
							rs = querible.executeQuery(getInfo);
							rs.next();
							paymentCard= rs.getString("c_card");
						}catch (SQLException e) {
							e.printStackTrace();
						}
						
						// Get payment information if it doesn't exist yet
						if (paymentCard == null) {
							while (true) {
								System.out.println("No payment information is saved for this user, would you like to add one? Y/N");
								input = in.nextLine().charAt(0);
								if (input == 'Y' || input == 'y') {
									Payment newPayment = new Payment();
									newPayment.makePayment(in, user, con);
									paymentCard = newPayment.card;
									break;
								}
								else if (input == 'N' || input == 'n') {
									wantBook = 0;
									break;
								}
							}
						}
						if (wantBook == 1) {
							while (true) {
								System.out.println("Would you like to pay with the card ending in " 
										+ paymentCard.charAt(paymentCard.length() - 4) 
										+ paymentCard.charAt(paymentCard.length() - 3) 
										+ paymentCard.charAt(paymentCard.length() - 2) 
										+ paymentCard.charAt(paymentCard.length() - 1) 
										+ "? Y/N");
								input = in.nextLine().charAt(0);
								if (input == 'Y' || input == 'y') {
									break;
								}
								else if (input == 'N' || input == 'n'){
									while(true) {
										System.out.println("Would you like to replace it with a new payment card? Y/N");
										if (input == 'Y' || input == 'y') {
											Payment newPayment = new Payment();
											newPayment.makePayment(in, user, con);
											
											break;
										}
										else if (input == 'N' || input == 'n') {
											wantBook = 0;
											break;
										}
									}
									break;
								}
							}	
							if (wantBook == 1) {
								// Insert the booking
								query = "insert into booking values (" + Float.toString(latitude) + ", " + Float.toString(longitude) + ", '"
										+ formattedStartDate + "', '" + user.SIN + "', '" + formattedEndDate + "');";
								querible.execute(query);
								System.out.println("Successfully booked!");
							}
						}
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
		}
		
		
		
		
		
		// handle the result
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
}
