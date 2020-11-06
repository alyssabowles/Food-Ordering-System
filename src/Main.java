import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Connection connection = null;
		String url = "jdbc:mariadb://localhost:3306/foodordering";
		String user;
        String pwd;
        Statement statement;
        Scanner scan = new Scanner(System.in);
        		
		try {
			
	        System.out.println("Opening a Connection to the Database");
	        System.out.print("Enter Username:  ");
	        user = scan.nextLine();
	        System.out.print("Enter Password:  ");
	        pwd = scan.nextLine();
	        System.out.println();

	        connection = DriverManager.getConnection(url, user, pwd);
	        statement = connection.createStatement();
	        
	        if (connection != null) {
                System.out.println("Successfully connected.");
                System.out.println();
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		

	}

}
