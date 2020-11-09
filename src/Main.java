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
	        
	        
	        
	        while(option != 6) {
	        	System.out.println("Select one of the following:" );
                System.out.println("[1]  Execute SQL Query");
                System.out.println("[2]  Place an Order");
                System.out.println("[3]  Create new Customer Account");
                System.out.println("[4]  Create new Driver Account");
                System.out.println("[5]  Review Past orders");
                System.out.println("[6]  Quit");
                System.out.print("Enter choice:  ");
                option = scan.nextInt();
                System.out.println();
                
                
                switch(option) {
                    case 1:
                        executeQuery(scan, statement);
                        break;
                    case 2:
                    	placeOrder(scan, statement);
                        break;
                    case 3:
                    	newCustomer(scan, statement);
                    	break;
                    case 4:
                    	newDriver(scan, statement);
                    	break;
                    case 5:
                    	reviewOrder(scan, statement);
                    	break;
                    case 6:
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
	
	private static void reviewOrder(Scanner scan, Statement statement) {
		
	}
	
	private static void newCustomer(Scanner scan, Statement statement) {
		
	}
	
	private static void newDriver(Scanner scan, Statement statement) {
		
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
	
	private static void placeOrder(Scanner scan, Statement statement) throws IOException, SQLException{
		int customerID = selectCustomer(scan, statement);
		// Get driver for order
		ResultSet rs = statement.executeQuery("select car_num from car order by rand()");
        rs.next();
        int carNum = rs.getInt(1);
        // Order ID should be the last order ID + 1
        rs = statement.executeQuery("select max(order_id) from orders");
        rs.next();
        int orderID = rs.getInt(1) + 1;
        
        // Get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();  
		String currDate = dtf.format(now);  
        
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
