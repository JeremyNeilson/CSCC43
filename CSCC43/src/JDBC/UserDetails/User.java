package JDBC.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {
	public String email;
	public String password;
	public String SIN;
	public String f_name;
	public String l_name;
	public String str_addr;
	public LocalDate birthday;
	public String occupation;
	public Boolean loggedIn;
	
	public User(){
		email = "";
		password = "";
		SIN = "";
		f_name = "";
		l_name = "";
		str_addr = "";
		birthday = LocalDate.of(2000, 12, 31);
		occupation = "";
	}
	
	public User(String email, String pWord, String f_name, String l_name, LocalDate birthday, String str_addr, String occupation, String SIN){
		this.email = email;
		password = pWord;
		this.f_name = f_name;
		this.l_name = l_name;
		this.birthday = birthday;
		this.str_addr = str_addr;
		this.occupation = occupation;
		this.SIN = SIN;
		this.loggedIn = true;
	}
	
	public String createInsert() {
		String query = "";
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = birthday.format(dateTimeFormatter);
		query = "INSERT INTO user VALUES ('" + SIN + "', '" + f_name + "', '" +  l_name + "', '" + str_addr + "', '" + occupation + "', '" + email + "', '" + password + "', '" + formattedDate + "');";
		
		return query;
	}
}
