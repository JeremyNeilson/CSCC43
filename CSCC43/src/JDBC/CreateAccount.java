package JDBC;

import java.util.Scanner;

public class CreateAccount {
	public String SIN;
	public String f_name;
	public String l_name;
	public String str_addr;
	public int age;
	public String occupation;
	
	
	CreateAccount() {
		
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
		
		int age = 0;
		while (true) {
			System.out.println("Age:");
			age = 0;
			try {
				age = Integer.parseInt(in.nextLine());
				break;
			} catch(Exception e) {
				System.out.println("Please enter a number");
			}
		}
			
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
		User newUser = new User(email, pWord, f_name, l_name, age, str_addr, occupation, SIN);
		return newUser;
	}
}
