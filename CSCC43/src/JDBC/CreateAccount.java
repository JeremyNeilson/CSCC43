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
		System.out.println("ACCOUNT CREATION\n\n");
		System.out.println("Enter your email: ");
		String email = in.nextLine();
		System.out.println("Create a password: ");
		String pWord = in.nextLine();
		
		// account details
		System.out.println("ENTER ACCOUNT DETAILS BELOW\n");
		
		System.out.println("First Name:");
		String f_name = in.nextLine();
		
		System.out.println("Last Name:");
		String l_name = in.nextLine();
		
		System.out.println("Age:");
		int age = Integer.parseInt(in.nextLine());
		
		System.out.println("Street Address:");
		String str_addr = in.nextLine();
		
		System.out.println("Enter your occupation:");
		String occupation = in.nextLine();
		
		System.out.println("Enter SIN:");
		String SIN = in.nextLine();
		
		// create the user object
		User newUser = new User(email, pWord, f_name, l_name, age, str_addr, occupation, SIN);
		return newUser;
	}
}
