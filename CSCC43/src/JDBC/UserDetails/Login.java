package JDBC.UserDetails;

import java.util.Scanner;

public class Login {
	public int choice;
	public Login(){
		choice = 0;
	}
	
	public int requestLogin(Scanner in) {
		System.out.println("Welcome to Jeremy's BnB service: [L]ogin, [C]reate Account, [E]xit Application");
		while (true) {
			String s = in.nextLine();
			if (s.charAt(0) == 'L' || s.charAt(0) == 'l') {
				return 1;
			}
			else if (s.charAt(0) == 'C' || s.charAt(0) == 'c') {
				return 2;
			}
			else if (s.charAt(0) == 'E' || s.charAt(0) == 'e') {
				return 3;
			}
			else {
				System.out.println("Please enter a valid character: [L]ogin, [C]reate Account, [E]xit Application");
			}
		}
	}
	
	public String getEmail(Scanner in) {
		while (true) {
			System.out.println("Please enter your email ([G]o Back):");
			String email = in.nextLine();
			if (email.equals("g")|| email.equals("G")) {
				return null;
			}
			return email;
		}
	}
	public String getPassword(Scanner in) {
		System.out.println("Please enter your password ([G]o Back):");
		String pword = in.nextLine();
		return pword;
	}
}
