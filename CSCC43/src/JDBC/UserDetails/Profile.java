package JDBC.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Profile {
	User user;
	Connection con;
	
	public Profile(User user, Connection con){
		this.user = user;
		this.con = con;
	}
	public void ProfileHub(Scanner in) throws SQLException {
		while (user.loggedIn == true) {
			System.out.println("FIRST NAME: " + user.f_name + "\n"
					+ "LAST NAME: " + user.l_name + "\nSOCIAL INSURANCE #: " + user.SIN + "\nADDRESS: " + user.str_addr
					+ "\nOCCUPATION: " + user.occupation);
			System.out.println("PROFILE OPTIONS\n[U]PDATE INFORMATION, [E]XIT PROFILE PAGE");
			String input = in.nextLine();
			if (input.equals("U")|| input.equals("u")) {
				// Update information
				while (true) {
					System.out.println("SELECT INFORMATION TO EDIT\n[F]IRST NAME: " + user.f_name + "\n"
						+ "[L]AST NAME: " + user.l_name +  "\n[A]DDRESS: " + user.str_addr
						+ "\n[O]CCUPATION: " + user.occupation + "\n[G]O BACK" + "\n[D]ELETE ACCOUNT");
					input = in.nextLine();
					if (input.equals("G")|| input.equals("g")) {
						break;
					}
					else if (input.equals("F")|| input.equals("f")) {
						user.f_name = updateField(input, in, "f_name", "FIRST NAME");
					}
					else if (input.equals("L")|| input.equals("l")) {
						user.l_name = updateField(input, in, "l_name", "LAST NAME");
					}
					else if (input.equals("A")|| input.equals("a")) {
						user.str_addr = updateField(input, in, "str_addr", "ADDRESS");
					}
					else if (input.equals("O")|| input.equals("o")) {
						user.occupation = updateField(input, in, "occupation", "OCCUPATION");
					}
					else if (input.equals("D")|| input.equals("d")) {
						while (true) {
							System.out.println("ARE YOU SURE YOU WANT TO DELETE YOUR ACCOUNT? (Y/N)");
							input = in.nextLine();
							if (input.equals("Y")|| input.equals("y")) {
								String listings = "select * from listing where host = '" + user.SIN + "';";
								Statement sql = con.createStatement();
								Statement listing = con.createStatement();
								try {
									ResultSet rs = listing.executeQuery(listings);
									while (rs.next() == true) {
										sql.execute("delete from availability where l_latitude = " + rs.getString("latitude") + " and l_longitude = " + rs.getString("longitude"));
										sql.execute("delete from cancel_booking where l_latitude = " + rs.getString("latitude") + " and l_longitude = " + rs.getString("longitude"));
										sql.execute("delete from booking where l_latitude = " + rs.getString("latitude") + " and l_longitude = " + rs.getString("longitude"));
									}
								}catch (SQLException e) {
									e.printStackTrace();
								}
								try {
									sql.execute("delete from booking where user = '" + user.SIN + "';");
									sql.execute("delete from cancel_booking where user = '" + user.SIN + "';");
									sql.execute("delete from h_review where host = '" + user.SIN + "';");
									sql.execute("delete from h_review where user = '" + user.SIN + "';");
									sql.execute("delete from u_review where host = '" + user.SIN + "';");
									sql.execute("delete from u_review where user = '" + user.SIN + "';");
									sql.execute("delete from listing where host = '" + user.SIN + "';");
									sql.execute("delete from host where h_sin = '" + user.SIN + "';");
									sql.execute("delete from user where sin = '" + user.SIN + "';");
								}catch (SQLException e) {
									e.printStackTrace();
								}
								user.loggedIn = false;
								return;
							}
							else if (input.equals("N")|| input.equals("n")) {
								break;
							}
							else {
								System.out.println("Please enter a valid input");
							}
						}
						
					}
				}
				
			}
			else if (input.equals("E")|| input.equals("e")) {
				break;
			}
		}
	}
	
	public String updateField(String input, Scanner in, String column, String col_string) throws SQLException {
		System.out.println("ENTER YOUR NEW " + col_string + ":");
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
