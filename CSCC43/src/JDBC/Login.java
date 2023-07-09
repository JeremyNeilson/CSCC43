package JDBC;

import java.util.Scanner;

public class Login {
	public int choice;
	Login(){
		choice = 0;
	}
	
	public int requestLogin(Scanner in) {
		System.out.println("Welcome to Jeremy's BnB service: [L]ogin, [C]reate Account");
		while (true) {
			String s = in.nextLine();
			if (s.charAt(0) == 'L' || s.charAt(0) == 'l') {
				return 1;
			}
			else if (s.charAt(0) == 'C' || s.charAt(0) == 'c') {
				return 2;
			}
			else {
				System.out.println("Please enter a valid character: [L]ogin, [C]reate Account");
			}
		}
	}
	
	public String getEmail(Scanner in) {
		System.out.println("Please enter your email:");
		String email = in.nextLine();
		return email;
	}
	public String getPassword(Scanner in) {
		System.out.println("Please enter your password:");
		String pword = in.nextLine();
		return pword;
	}
}
