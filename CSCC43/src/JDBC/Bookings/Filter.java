package JDBC.Bookings;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JDBC.UserDetails.User;

public class Filter {
	User user;
	
	public Filter(User user) {
		this.user = user;
	}
	
	public String chooseFilters(Scanner in) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String coords = "";
		String pCode = "";
		String address = "";
		String price = "";
		String date[] = {"", " min(availability.date) >= " + LocalDate.now().format(dateTimeFormatter)};
		String amenities = "";
		String output = " select * from listing where removed = 0 and host != '" + user.SIN + "' ";
		String putout = " select listing.* from listing join availability on listing.latitude = availability.l_latitude and listing.longitude = availability.l_longitude ";
		String putout2 = " group by listing.latitude, listing.longitude having ";
		int datePrice = 0;
		int more = 0;
		while (more == 0) {
			System.out.println("Filter by: \n[0]: Coordinates\n[1]: Postal Code\n[2]: Address\n[3]: Price\n[4]: Date Range\n[5]: Amenities\n[6]: None");
			if (in.hasNextInt()) {
				int choice = in.nextInt();
				in.nextLine();
				if (choice == 0) {
					// coords
					output = output + addCoordFilter(in);
				}
				else if (choice == 1) {
					// postal code
					output = output + addPostalFilter(in);
				}
				else if (choice == 2) {
					// address
					output = output + addAddressFilter(in);
				}
				else if (choice == 3) {
					price = addPriceFilter(in);
					datePrice = 1;
				}
				else if (choice == 4) {
					date = addDateFilter(in);
					datePrice = 1;
				}
				else if (choice == 5) {
					output = output + addAmenityFilter(in);
				}
				else if (choice == 6) {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
					continue;
				}
				while(true) {
					System.out.println("Add more filters? Y/N");
					String input = in.nextLine();
					if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
						break;
					}
					else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
						more = 1;
						break;
					}
					else {
						System.out.println("Please enter a valid input");
					}
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		output = output + coords + address + pCode + amenities + ";";
		if (datePrice == 1) {
			String endPut = putout + date[0] + putout2 + date[1] + price + " intersect ";
			endPut = endPut + output;
			return endPut;
		}
		return output;
	}
	
	public String addPriceFilter(Scanner in) {
		String output;
		Float price;
		while(true) {
			System.out.println("Enter your maximum price for one night:");
			if (in.hasNextFloat()) {
				price = in.nextFloat();
				in.nextLine();
				if (price >= 0f) {
					output = " and max(availability.price) <= " + Float.toString(price);
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		System.out.println("Added filter with maximum price of " + Float.toString(price));
		return output;
	}
	
	public String addAmenityFilter(Scanner in) {
		String output = "";
		String[] amenities = {"Washer", "Dryer", "TV", "Air Conditioning", "Wifi", "Stove", "Oven", 
				"Refridgerator", "Microwave", "Cooking Basics", "Dishes and Utensils", "Coffee Maker"};
		int[] chosen = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		System.out.println("Amenity Filter Choices:");
		int finished = 0;
		while(finished == 0) {
			for (int i = 0; i < amenities.length; i++) {
				System.out.println("[" + i + "]: " + amenities[i]);
			}
			int choice = -1;
			System.out.println("Enter the number of an amenity you wish to filter by:");
			if (in.hasNextInt()) {
				choice = in.nextInt();
				in.nextLine();
				if (choice >= 0 && choice < amenities.length) {
					if (chosen[choice] == 1) {
						System.out.println("You are already filtering by " + amenities[choice]);
						continue;
					}
					output = output + " and " + amenities[choice] + " = 1 ";
					chosen[choice] = 1;
					System.out.println("Added " + amenities[choice] + " to filter choices!");
					while(true) {
						System.out.println("Add more amenity filters? Y/N");
						String input = in.nextLine();
						if (input.charAt(0) == 'Y' || input.charAt(0) == 'y') {
							break;
						}
						else if(input.charAt(0) == 'N' || input.charAt(0) == 'n') {
							finished = 1;
							break;
						}
						else {
							System.out.println("Please enter a valid input");
						}
					}
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		
		System.out.println(output);
		return output;
	}
	
	public String[] addDateFilter(Scanner in) {
		Booking dateGetter = new Booking();
		LocalDateTime startDate;
		LocalDateTime endDate;
		long numDays = 0;
		while(true) {
			System.out.println("Enter the start of your date range:");
			startDate = dateGetter.GetDate(in);
			System.out.println("Enter the end of your date range:");
			endDate = dateGetter.GetDate(in);
			System.out.println("Start date: " + startDate.toString());
			System.out.println("End date: " + endDate.toString());
			
			// check for that date range
			numDays = Duration.between(startDate, endDate).toDays();
			if (numDays < 1) {
				System.out.println("Please enter a date range greater than 0");
			}
			else {
				break;
			}
		}
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedStartDate = startDate.format(dateTimeFormatter);
		String formattedEndDate = endDate.format(dateTimeFormatter);
		String output = " min(availability.date) >= '" + formattedStartDate + "' and max(availability.date) <= '" + formattedEndDate + "' ";
		String input = " where availability.date between '" + formattedStartDate + "' and '" + formattedEndDate + "' ";
		String[] end = {input, output};
		return end;
	}
	
	public String addAddressFilter(Scanner in) {
		String input;
		System.out.println("Enter the address you would like to filter for:");
		input = in.nextLine();
		String output = " and str_addr = '" + input + "' ";
		return output;
	}
	
	public String addPostalFilter(Scanner in) {
		String input;
		
		Pattern pattern = Pattern.compile("[abceghjklmnprstvxy][0-9][abceghjklmnprstvwxyz]\s?[0-9][abceghjklmnprstvwxyz][0-9]", Pattern.CASE_INSENSITIVE);
		while(true) {
			System.out.println("Enter the postal code you wish to search in:");
			input = in.nextLine();
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
		}
		System.out.println("Added filter for listings in postal codes " + input.substring(0, 3));
		return " and p_code LIKE '%" + input.substring(0, 3) + "%' ";
	}
	
	public String addCoordFilter(Scanner in) {
		Float latitude = 0f;
		Float longitude = 0f;
		Float radius = 0f;
		while (true) {
			System.out.println("Enter the latitude to search around: ");
			if (in.hasNextFloat()) {
				latitude = in.nextFloat();
				in.nextLine();
				if (latitude >= -90f && latitude <= 90f) {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		while (true) {
			System.out.println("Enter the longitude to search around: ");
			if (in.hasNextFloat()) {
				longitude = in.nextFloat();
				in.nextLine();
				if (longitude >= -180f && longitude <= 180f) {
					break;
				}
				else {
					System.out.println("Please enter a valid input");
				}
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		while (true) {
			System.out.println("Enter the degree of radius to search in: ");
			if (in.hasNextFloat()) {
				radius = Math.abs(in.nextFloat());
				in.nextLine();
				break;
			}
			else {
				in.nextLine();
				System.out.println("Please enter a valid input");
			}
		}
		Float maxLat = (latitude + radius) % 90f;
		Float minLat = (latitude - radius) % 90f;
		Float maxLon = (longitude + radius) % 180f;
		Float minLon = (longitude - radius) % 180f;
		
		String newQuery = " and latitude <= " + Float.toString(maxLat) + " and latitude >= " + Float.toString(minLat) + " and longitude <= " 
							+ Float.toString(maxLon) + " and longitude >= " + Float.toString(minLon);
		System.out.println("Added Coordinate Filter! " + newQuery);
		
		
		return newQuery;
	}
}
