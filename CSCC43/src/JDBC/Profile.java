package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Profile {
	User user;
	Connection con;
	
	Profile(User user, Connection con){
		this.user = user;
		this.con = con;
	}
	public void ProfileHub(Scanner in) throws SQLException {
		while (true) {
			System.out.println("PROFILE OPTIONS\n[U]PDATE INFORMATION, [E]XIT PROFILE PAGE");
			String input = in.nextLine();
			if (input.charAt(0) == 'U' || input.charAt(0) == 'u') {
				// Update information
				while (true) {
					System.out.println("SELECT INFORMATION TO EDIT\n[F]IRST NAME: " + user.f_name + "\n"
						+ "[L]AST NAME: " + user.l_name + "\n[S]OCIAL INSURANCE #: " + user.SIN + "\n[A]DDRESS: " + user.str_addr
						+ "\n[O]CCUPATION: " + user.occupation + "\n[G]O BACK");
					input = in.nextLine();
					if (input.charAt(0) == 'G' || input.charAt(0) == 'g') {
						break;
					}
					else if (input.charAt(0) == 'F' || input.charAt(0) == 'f') {
						user.f_name = updateField(input, in, "f_name");
					}
					else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
						user.l_name = updateField(input, in, "l_name");
					}
					else if (input.charAt(0) == 'S' || input.charAt(0) == 's') {
						user.SIN = updateField(input, in, "SIN");
					}
					else if (input.charAt(0) == 'A' || input.charAt(0) == 'a') {
						user.str_addr = updateField(input, in, "str_addr");
					}
					else if (input.charAt(0) == 'O' || input.charAt(0) == 'o') {
						user.occupation = updateField(input, in, "occupation");
					}
				}
				
			}
			if (input.charAt(0) == 'E' || input.charAt(0) == 'e') {
				break;
			}
		}
	}
	
	public String updateField(String input, Scanner in, String column) throws SQLException {
		System.out.println("ENTER YOUR NEW FIRST NAME:");
		input = in.nextLine();
		String nameUpdate = "update user set " + column + " = '" + input + "' where email = '" + user.email + "';";
		Statement update = con.createStatement();
		try {
			update.execute(nameUpdate);
			System.out.println("Updated!\n\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		update.close();
		return input;
	}
}
