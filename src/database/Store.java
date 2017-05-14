package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Store {
	private static Store store = null;
	HashMap<String, List<String>> myMap;

	private Store() throws SQLException {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		HashMap<String, List<String>> myMap = new HashMap<>();

		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://158.108.140.34:3306/Terrarium", "root", "1234");

			// 2. Create a statement
			myStmt = myConn.createStatement();

			// 3. Execute SQL query
			myRs = myStmt.executeQuery("select * from products");

			// 4. Process the result set
			while (myRs.next()) {
				System.out.println(
						myRs.getString("product_id") + ", " + myRs.getString("name") + ", " + myRs.getString("price"));
				myMap.put(myRs.getString("product_id"),
						new ArrayList<String>(Arrays.asList(myRs.getString("name"), myRs.getString("price"))));
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (myRs != null) {
				myRs.close();
			}

			if (myStmt != null) {
				myStmt.close();
			}

			if (myConn != null) {
				myConn.close();
			}
		}
	}

	public static Store getInstance() {
		if (store == null)
			try {
				store = new Store();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return store;

	}

	public HashMap<String, List<String>> getMyMap() {
		return myMap;
	}

}