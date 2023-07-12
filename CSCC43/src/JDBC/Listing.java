package JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Listing {
	float latitude;
	float longitude;
	String host_SIN; // 9
	String type; // 25
	String str_addr; // 100
	String p_code; // 7
	String city; // 50
	String country; // 50
	String amenities; // 250
	int bedrooms;
	int bathrooms;
	
	Listing(){
		latitude = 0.00f;
		longitude = 0.00f;
		host_SIN = ""; // 9
		type = ""; // 25
		str_addr = ""; // 100
		p_code = ""; // 7
		city = ""; // 50
		country = ""; // 50
		amenities = ""; // 250
		bedrooms = 0;
		bathrooms = 0;
	}
	
	
	// Requires a ResultSet of a query to the listing table
	public Listing setListing(ResultSet rs) throws SQLException {
		this.host_SIN = rs.getString("host");
		this.latitude = Float.parseFloat(rs.getString("latitude"));
		this.longitude = Float.parseFloat(rs.getString("longitude"));
		this.type = rs.getString("type");
		this.city = rs.getString("city");
		this.country = rs.getString("country");
		this.str_addr = rs.getString("str_addr");
		this.p_code = rs.getString("p_code");
		this.amenities = rs.getString("amenities");
		return this;
	}
	
	public void printListing() {
		System.out.println("---------------------");
		System.out.println(str_addr);
		System.out.println(city + " " + country + " " + p_code);
		System.out.println("Type of stay" + type);
		if (amenities != "") {
			System.out.println("Notes from the host:\nABC" + amenities + "DEF");
		}
		System.out.println("---------------------");
	}
}
