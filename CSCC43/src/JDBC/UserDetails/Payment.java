package JDBC.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Payment {
	User user;
	Connection con;
	
	public String card;
	String f_name;
	String l_name;
	String bill_addr;
	String p_code;
	String country;
	LocalDate expiry;
	String code;
	
	public Payment(){
		this.card = "";
		this.f_name = "";
		this.l_name = "";
		this.bill_addr = "";
		this.p_code = "";
		this.country = "";
		this.expiry = LocalDate.of(2000, 12, 31);;
		this.code = "";
	}
	
	public Payment(String card, String f_name,	String l_name, String bill_addr, String p_code,	String country,	LocalDate expiry, String code){
		this.card = card;
		this.f_name = f_name;
		this.l_name = l_name;
		this.bill_addr = bill_addr;
		this.p_code = p_code;
		this.country = country;
		this.expiry = expiry;
		this.code = code;
	}
	
	public void setPayment(ResultSet rs) throws SQLException {
		this.card = rs.getString("card");
		this.f_name = rs.getString("f_name");
		this.l_name = rs.getString("l_name");
		this.bill_addr = rs.getString("bill_addr");
		this.p_code = rs.getString("p_code");
		this.country = rs.getString("country");
		this.expiry = LocalDate.parse(rs.getString("expiry"));
		this.code = rs.getString("code");
	}
	
	
	public void makePayment(Scanner in, User user, Connection con) throws SQLException{
		// card details NEEDS ERROR HANDLING
		System.out.println("PAYMENT INFORMATION");
		System.out.println("First name: ");
		this.f_name = in.nextLine();
		System.out.println("Last name: ");
		this.l_name = in.nextLine();
		
		// need to check card length and that its all numerical
		System.out.println("Enter the card number: ");
		this.card = in.nextLine();
		
		// need to check proper date entry
		Pattern expiryPattern = Pattern.compile("[0-1][0-9]-20[2-3][0-9]", Pattern.CASE_INSENSITIVE);
		String input = "08-2025";
		while(true) {
			System.out.println("Enter the expiry (MM-YYYY): ");
			input = in.nextLine();
			Matcher matcher = expiryPattern.matcher(input);
			if (matcher.matches()) {
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		String expired = "01-" + input;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		this.expiry = LocalDate.parse(expired, formatter);
		
		// check that this is numerical and 3-digits
		Pattern pattern = Pattern.compile("[0-9][0-9][0-9]", Pattern.CASE_INSENSITIVE);
		while(true) {
			System.out.println("Enter the 3-digit code: ");
			input = in.nextLine();
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				this.p_code = input;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		
		System.out.println("Enter billing address: ");
		this.bill_addr = in.nextLine();
		
		// NEEDS ERROR HANDLING
		Pattern pPattern = Pattern.compile("[abceghjklmnprstvxy][0-9][abceghjklmnprstvwxyz]\s?[0-9][abceghjklmnprstvwxyz][0-9]", Pattern.CASE_INSENSITIVE);
		while(true) {
			System.out.println("Postal Code:");
			input = in.nextLine();
			Matcher matcher = pPattern.matcher(input);
			if (matcher.matches()) {
				this.p_code = input;
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		
		System.out.println("Country: ");
		this.country = in.nextLine();
				
		String paymentQuery = "INSERT INTO payment VALUES ('" + card + "', '" + f_name + "', '" +  l_name + "', '" + bill_addr + "', '" + p_code + "', '" + country + "', '" + expiry + "', '" + code + "');";
		String userUpdate = "update user set c_card = '" + card + "' where sin = '" + user.SIN + "';";
		Statement update = null;
		ResultSet rs = null;
		try {
			update = con.createStatement();
		}catch (SQLException e ) {
			e.printStackTrace();
		}
		try {
			update.execute(paymentQuery);
			update.execute(userUpdate);
			System.out.println("Payment Info Saved!\n\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		update.close();
	}
}
