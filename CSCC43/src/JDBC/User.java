package JDBC;

public class User {
	public String email;
	public String password;
	public String SIN;
	public String f_name;
	public String l_name;
	public String str_addr;
	public int age;
	public String occupation;
	User(){
		email = "";
		password = "";
		SIN = "";
		f_name = "";
		l_name = "";
		str_addr = "";
		age = 0;
		occupation = "";
	}
	
	User(String email, String pWord, String f_name, String l_name, int age, String str_addr, String occupation, String SIN){
		this.email = email;
		password = pWord;
		this.f_name = f_name;
		this.l_name = l_name;
		this.age = age;
		this.str_addr = str_addr;
		this.occupation = occupation;
		this.SIN = SIN;
	}
	
	public String createInsert() {
		String query = "";
		
		query = "INSERT INTO user VALUES ('" + SIN + "', '" + f_name + "', '" +  l_name + "', '" + str_addr + "', " + Integer.toString(age) + ", '" + occupation + "', '" + email + "', '" + password + "');";
		
		return query;
	}
}
