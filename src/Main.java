import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		
		Connection connection = null;
		String url = "jdbc:mariadb://localhost:3306/foodordering";
		String user;
        String pwd;
        String ans;
        Statement statement;
        int option = 0;
        Scanner scan = new Scanner(System.in);
        
        // Get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();  
		String currDate = dtf.format(now);
        
		try {
			
	        System.out.println("Opening a Connection to the Database");
	        System.out.print("Enter Username: ");
	        user = scan.nextLine();
	        System.out.print("Enter Password: ");
	        pwd = scan.nextLine();
	        System.out.println();

	        connection = DriverManager.getConnection(url, user, pwd);
	        statement = connection.createStatement();
	        
	        if (connection != null) {
                System.out.println("Successfully connected.");
                System.out.println();
            }
	        
	        
	        
	        while(option != 4) {
	        	System.out.println("***** Main Menu *****");
	        	System.out.println("Select one of the following:" );
                System.out.println("[1]  Execute SQL Query");
                System.out.println("[2]  Customer Menu");
                System.out.println("[3]  Driver Menu");
                System.out.println("[4]  Quit");
                System.out.print("Enter choice:  ");
                option = scan.nextInt();
                System.out.println();
                
                
                switch(option) {
                    case 1:
                        executeQuery(scan, statement);
                        break;
                    case 2:
                    	customerMenu(scan, statement, currDate);
                        break;
                    case 3:
                    	driverMenu(scan, statement, currDate);
                    	break;
                    case 4:
                    	System.out.println("Program Terminated.");
                        break;
                    default:
                        System.out.println("Please enter a valid choice!");
                        System.out.println();
                }
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void customerMenu(Scanner scan, Statement statement, String currDate) throws IOException, SQLException {
		int option = 0;
		while(option != 4) {
			System.out.println("***** Customer Menu *****");
        	System.out.println("Select one of the following:" );
            System.out.println("[1]  Place an Order");
            System.out.println("[2]  Create new Customer Account");
            System.out.println("[3]  Review Past orders");
            System.out.println("[4]  Back to previous menu");
            System.out.print("Enter choice:  ");
            option = scan.nextInt();
            System.out.println();
            switch(option) {
            case 1:
            	placeOrder(scan, statement, currDate);
                break;
            case 2:
            	newCustomer(scan, statement);
                break;
            case 3:
            	reviewOrder(scan, statement);
            	break;
            case 4:
            	return;
            default:
                System.out.println("Please enter a valid choice!");
                System.out.println();
            }
            
		}
	}
	
	private static void driverMenu(Scanner scan, Statement statement, String currDate) throws IOException, SQLException {
		int option = 0;
		while(option != 4) {
			System.out.println("***** Customer Menu *****");
        	System.out.println("Select one of the following:" );
            System.out.println("[1]  Create new Driver Account");
            System.out.println("[2]  Log hours worked");
            System.out.println("[3]  Back to previous menu");
            System.out.print("Enter choice:  ");
            option = scan.nextInt();
            System.out.println();
            switch(option) {
            case 1:
            	newDriver(scan, statement, currDate);
                break;
            case 2:
            	logHours(scan, statement);
                break;
            case 3:
            	return;
            default:
                System.out.println("Please enter a valid choice!");
                System.out.println();
            }
            
		}
	}
	
	private static void executeQuery(Scanner scan, Statement statement) {
		scan.nextLine();
		System.out.print("Enter SQL command: ");
		String query = scan.nextLine();
		System.out.println();
		try {
			boolean b = statement.execute(query);
			if(b) {
				ResultSet rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();
				int col = rsmd.getColumnCount();
				while(rs.next()) {
					for (int i = 0; i < col; i++) {
						if (i > 0) {
							System.out.print(", ");
						}
						System.out.print(rs.getString(i+1));
					}
					System.out.println("");
				}
				System.out.println("");
			}
			else {
				int count = statement.getUpdateCount();
				if (count > 0) {
					System.out.println(count + " rows affected");
				}
				else {
					System.out.println("No rows affected");
				}
			}
		}
		catch(Exception e) {
			System.out.println("Could not execute query");
		}
	}
	
	private static void reviewOrder(Scanner scan, Statement statement) throws IOException, SQLException {
		int customerID = selectCustomer(scan, statement);
		ResultSet rs = statement.executeQuery("select purchase_date, price from orders where customer="+customerID);
		while(rs.next()) {
			System.out.print("Order Date: " + rs.getString(1));
			System.out.print("     Order Price: $" + rs.getString(2));
			System.out.println("");
		}
		System.out.println("");
	}
	
	private static void newCustomer(Scanner scan, Statement statement) throws IOException, SQLException {
		scan.nextLine();
		System.out.print("Enter your Full Name:   ");
		String name = scan.nextLine();
		System.out.print("Enter your Address:   ");
		String address = scan.nextLine();
		System.out.print("Enter your Phone Number:   ");
		String phoneNum = scan.nextLine();
		System.out.println("***** Credit Card Information *****");
		System.out.print("Credit Card Number:   ");
		String cardNum = scan.nextLine();
		System.out.print("CVV:   ");
		int cvv = scan.nextInt();
		System.out.println("Expiration date (mm/yy):   ");
		String expDate = scan.nextLine();
		
		
		// Make the new customerID one above from the largest customerID
        ResultSet rs = statement.executeQuery("select max(customer_id) from customer");
        rs.next();
        int customerID = rs.getInt(1) + 1;
		
        // Add customer's credit card information into database
     	statement.executeUpdate("insert into credit_card(card_number, cvv, exp_date, cust_id) " 
             	+ "values ('" +cardNum+ "', "+cvv+", '" + expDate +"', " + customerID+")");
        
		// Adds new customer into the database
        statement.executeUpdate("insert into customer(customer_id, name, address, phone_number) " 
        		+ "values (" +customerID+ ", '"+name+"', '" + address +"', '" + phoneNum+"')");
        System.out.println();
		System.out.println("Customer successfully added.");
		System.out.println();
		System.out.println("Your Customer ID: " + customerID);
		System.out.println();
	}
	
	private static void newDriver(Scanner scan, Statement statement, String currDate) throws IOException, SQLException {
		scan.nextLine();
		System.out.print("Enter your Full Name:   ");
		String name = scan.nextLine();
				
		// Make the new customerID one above from the largest customerID
        ResultSet rs = statement.executeQuery("select max(driver_id) from driver");
        rs.next();
        int driverID = rs.getInt(1) + 1;
		
		// Adds new customer into the database
        statement.executeUpdate("insert into driver(driver_id, name, hours_worked, start_date) " 
        		+ "values (" +driverID+ ", '"+name+"', 0,'" + currDate+"')");
        System.out.println();
		System.out.println("Driver successfully added.");
		System.out.println("Your Driver ID: "+ driverID);
		System.out.println();
	}
	
	private static int selectCustomer(Scanner scan, Statement statement) throws IOException, SQLException {
		System.out.print("Enter customer ID:  ");
		int customerID = scan.nextInt();
		ResultSet rs = statement.executeQuery("Select * from customer where customer_id ="+customerID);
		if(rs.next()) {
            System.out.println("   Name:        " + rs.getString(2));
            System.out.println("   Address:     " + rs.getString(3));
            System.out.println("   Phone:       "+rs.getString(4));
            scan.nextLine();            
            System.out.print("Is this you? (Yes/No):  ");
            String ans = scan.nextLine();
            
            System.out.println();
            if(ans.equalsIgnoreCase("Yes")) {
                return customerID;
            }else {
            	System.out.println("Not the correct customer, try again.");
                return selectCustomer(scan, statement);
            }
        } else {
            System.out.println("Customer does not exist!");
            System.out.println();
            return selectCustomer(scan, statement);
        }
		
	}
	
	private static int selectDriver(Scanner scan, Statement statement) throws IOException, SQLException {
		System.out.print("Enter Driver ID:  ");
		int driverID = scan.nextInt();
		ResultSet rs = statement.executeQuery("Select * from driver where driver_id ="+driverID);
		if(rs.next()) {
            System.out.println("   Name:        " + rs.getString(2));
            scan.nextLine();            
            System.out.print("Is this you? (Yes/No):  ");
            String ans = scan.nextLine();
            
            System.out.println();
            if(ans.equalsIgnoreCase("Yes")) {
                return driverID;
            }else {
            	System.out.println("Not the correct driver, try again.");
                return selectDriver(scan, statement);
            }
        } else {
            System.out.println("Driver does not exist!");
            System.out.println();
            return selectDriver(scan, statement);
        }
		
        
	}
	
	private static void logHours(Scanner scan, Statement statement) throws IOException, SQLException{
		int driverID = selectDriver(scan, statement);
		System.out.print("How many hours have you worked?   ");
		int numHours = scan.nextInt();
		statement.executeQuery("set @intValue:="+numHours);
		statement.executeQuery("update driver set hours_worked=hours_worked+"+numHours+" where driver_id="+driverID);
		
		System.out.println();
		System.out.println("Hours successfully logged.");
		System.out.println();
	}
	
	private static double createMeal(Scanner scan, Statement statement) throws IOException, SQLException{
		double totalPrice = 0;
		boolean add = true;
		while(add) {
			System.out.println("Select your items: ");
	        ArrayList<String> food = new ArrayList<String>();
	        ResultSet rs = statement.executeQuery("select * from menu_item");
	        int i=0;
	        while(rs.next()) {
	            food.add(rs.getString(1));
	            System.out.println( " [" + rs.getString(1) + "]" + "\t$" + rs.getString(3) + "\t" + rs.getString(2));
	            
	            System.out.println();
	            i++;
	        }
	        System.out.print("Enter choice:  ");
	        int foodID = scan.nextInt();
	        rs = statement.executeQuery("select price from menu_item where food_id="+foodID);
	        rs.next();
	        totalPrice += rs.getDouble(1);
	        System.out.println();
	        scan.nextLine();
	        System.out.print("Would you like to add another item? [yes/no]   ");
	        String ans = scan.nextLine();
	        if (ans.equalsIgnoreCase("no")) {
	        	add = false;
	        }
		}
        return totalPrice;
	}
	
	private static void placeOrder(Scanner scan, Statement statement, String currDate) throws IOException, SQLException{
		int customerID = selectCustomer(scan, statement);
		// Get driver for order
		ResultSet rs = statement.executeQuery("select car_num from car order by rand()");
        rs.next();
        int carNum = rs.getInt(1);
        // Order ID should be the last order ID + 1
        rs = statement.executeQuery("select max(order_id) from orders");
        rs.next();
        int orderID = rs.getInt(1) + 1;
        
		// Get the credit card number of the customer
		rs = statement.executeQuery("select card_number from credit_card where cust_id="+customerID);
		rs.next();
		String cardNum = rs.getString(1);
		
		double price = createMeal(scan, statement);
		
        // Adds new order into the database
        statement.executeUpdate("insert into orders (order_id, purchase_date, customer, car_num, credit_card, price)"
        		+ "values (" +orderID+ ", '"+currDate+"', " + customerID +", " + carNum+ ", '"+cardNum+"', "+price+");");
        
        // Get the name of the customer
        rs = statement.executeQuery("select name from customer where customer_id="+customerID);
		rs.next();
		String customerName = rs.getString(1);
        
		// Get name of driver
		rs = statement.executeQuery("select driver.name from driver, car where car_num="+ carNum+ " and driver.driver_id=car.driver_id");
		rs.next();
		String driverName = rs.getString(1);
		
		// Print a receipt
		System.out.println();
        System.out.println("Thank you for your purchase! Here is your receipt:");
        System.out.println("***************************************");
        System.out.println("Customer ID: " + customerID);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Today's Date: " + currDate);
        System.out.println("Order number: "+ orderID);
        System.out.println("Total price of order: $" + price);
        System.out.println("Delivery Driver Name: " + driverName);
        System.out.println("***************************************");
        
	}

}
