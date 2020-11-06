import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Connection connection = null;
		String url = "jdbc:mariadb://localhost:3306/foodordering";
		String user;
        String pwd;
        String ans;
        Statement statement;
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
	        
	        boolean continueQuery = true;
			while(continueQuery) {
				executeQuery(scan, statement);
				System.out.print("Would you like to enter another query? [y/n]  ");
				ans = scan.nextLine();
				if (ans.equalsIgnoreCase("n")) {
					System.out.println("Program Terminated.");
					continueQuery = false;
				}
			}
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void executeQuery(Scanner scan, Statement statement) {
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

}
