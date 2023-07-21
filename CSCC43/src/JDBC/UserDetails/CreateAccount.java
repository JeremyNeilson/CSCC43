package JDBC.UserDetails;

import java.util.Scanner;
import java.time.LocalDate;


public class CreateAccount {
	public String SIN;
	public String f_name;
	public String l_name;
	public String str_addr;
	public int age;
	public String occupation;
	
	
	public CreateAccount() {
		
	}
	
	
	public User makeUser(Scanner in) {
		
		// email and password
		System.out.println("ACCOUNT CREATION");
		System.out.println("Enter your email: ");
		String email = in.nextLine();
		String pWord = "";
		while (true) {
			System.out.println("Create a password: ");
			pWord = in.nextLine();
			if (pWord.length() >= 8) {
				break;
			}
			else {
				System.out.println("Please enter a longer password (min. 8 characters)");
			}
		}
		
		// account details
		System.out.println("\nENTER ACCOUNT DETAILS BELOW");
		
		System.out.println("First Name:");
		String f_name = in.nextLine();
		
		System.out.println("Last Name:");
		String l_name = in.nextLine();
		
		System.out.println("Date of Birth:");
		int year = 0;
		int month = 0;
		int day = 0;
		while (true) {
			System.out.println("Year:");
			try {
				year = Integer.parseInt(in.nextLine());
				if (year < 1907) {
					System.out.println("Please enter a valid year");
					continue;
				}
				else if (year > 2005) {
					System.out.println("You must be above 18 years of age to register");
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
				if (month < 1 || month > 12 ) {
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
		LocalDate birthday = LocalDate.of(year, month, day);
		System.out.println(birthday);
			
		System.out.println("Street Address:");
		String str_addr = in.nextLine();
		
		System.out.println("Enter your occupation:");
		String occupation = in.nextLine();
		
		String SIN = "";
		while (true) {
			System.out.println("Enter SIN (format: XXXXXXXXX):");
			SIN = in.nextLine();
			try {
				Integer.parseInt(SIN);
				if (SIN.length() == 9) {
					break;
				}
				else {
					System.out.println("Please follow the format provided");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a proper SIN");
			}
		}
		
		// create the user object
		User newUser = new User(email, pWord, f_name, l_name, birthday, str_addr, occupation, SIN);
		return newUser;
	}
}
