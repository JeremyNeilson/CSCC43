package mainPackage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class Writer {
	public static void main(String args[]) throws IOException, SQLException {
		String url = "jdbc:mysql://localhost:3306/bnb";
		Connection con = null;
		Scanner in = new Scanner(System.in);
		// create connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(url, "root", "DinJ4vla!#asd");
		} catch (Exception e){
			System.out.println("Couldn't make the connection");
			e.printStackTrace();
		}
		
		//Instantiating the File class
	      File file = new File("C:\\Users\\jerem\\OneDrive - University of Toronto\\Documents\\School Stuff\\Summer_2023\\CSCC43\\availabilities.txt");
      //Instantiating the PrintStream class
      PrintStream stream = new PrintStream(file);
      System.out.println("From now on "+file.getAbsolutePath()+" will be your console");
      System.setOut(stream);
		Statement statement = con.createStatement();
		Statement insert = con.createStatement();
		ResultSet rs = statement.executeQuery("select * from listing");
		LocalDate today = LocalDate.now();
		LocalDate start = today.minusDays(365);
		int num = in.nextInt();
		if (num == 1) {
			while (rs.next() == true) {
				String startOfQuery = "INSERT INTO availability VALUES (" + rs.getString("latitude") + ", " + rs.getString("longitude") + ", '";
				int days[] = {Math.round((float) Math.random()), Math.round((float) Math.random()), Math.round((float) Math.random()), Math.round((float) Math.random()), 
						Math.round((float) Math.random()), Math.round((float) Math.random()), Math.round((float) Math.random())};
				int startIndex = 0;
				int max = getRandomNumber(30, 200);
				float prices[] = {((float) getRandomNumber(30, max)) - 0.01f, ((float) getRandomNumber(30, max)) - 0.01f, ((float) getRandomNumber(30, max)) - 0.01f, 
						((float) getRandomNumber(30, max)) - 0.01f, ((float) getRandomNumber(30, max)) - 0.01f, ((float) getRandomNumber(30, max)) - 0.01f, ((float) getRandomNumber(30, max)) - 0.01f};
				for (int i = 0; i < 720; i++) {
					if (days[startIndex] != 0) {
						String query = startOfQuery + start.plusDays(i).toString() + "', " + prices[startIndex] + ");";
						System.out.println(query);
					}
					startIndex++;
					startIndex = startIndex % 7;
				}
			}
		}
		else if (num == 2){
			rs = statement.executeQuery("select * from booking");
			while (rs.next() == true) {
				Statement poster = con.createStatement();
				String startOfQuery = "select l_latitude, l_longitude, sum(price) from availability where l_latitude = " + rs.getString("l_latitude") + " and l_longitude = " + rs.getString("l_longitude")
					+ " and date >= '" + rs.getString("date") + "' and date <= '" + rs.getString("e_date") + "' group by l_latitude, l_longitude;";
				ResultSet priceRes = poster.executeQuery(startOfQuery);
				if (priceRes.next() == true) {
					String price = priceRes.getString("sum(price)");
					String update = "update booking set price = " + price + " where l_latitude = " + rs.getString("l_latitude") + " and l_longitude = " + rs.getString("l_longitude")
						+ " and user = " + rs.getString("user") + ";";
					System.out.println(update);
				}
			}
		}
		else if (num == 3){
			rs = statement.executeQuery("select user, host, latitude, longitude from booking, listing where latitude = l_latitude and longitude = l_longitude;");
			while (rs.next() == true) {
				Statement poster = con.createStatement();
				String startOfQuery = "insert into h_review values (" + rs.getString("host") + ", 0, " + rs.getString("user") + ", '', " + rs.getString("latitude") + ", "
										+ rs.getString("longitude") + ");";
				System.out.println(startOfQuery);
			}
		}
		else if (num == 4){
			rs = statement.executeQuery("select user, host, latitude, longitude from booking, listing where latitude = l_latitude and longitude = l_longitude;");
			while (rs.next() == true) {
				Statement poster = con.createStatement();
				String startOfQuery = "insert into u_review values (" + rs.getString("user") + ", 0, " + rs.getString("host") + ", '', " + rs.getString("latitude") + ", "
										+ rs.getString("longitude") + ");";
				System.out.println(startOfQuery);
			}
		}
	}
	
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
}
