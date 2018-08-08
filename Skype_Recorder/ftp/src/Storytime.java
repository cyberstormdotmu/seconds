import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class Storytime {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("create file with this data in it:\n");
			return;
		}
		Connection conn = null;
		String host = null;
		String user = null;
		String password = null;
		String database = null;
		Properties pro = null;
		try {
			pro = new Properties();
			pro.load(new FileInputStream(args[0]));
			host = pro.getProperty("HOST", null);
			user = pro.getProperty("USER", null);
			password = pro.getProperty("PASSWORD", null);
			database = pro.getProperty("DATABASE", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String url = "jdbc:mysql://" + host + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Database connection established");
			new Scheduler(args[0], conn).start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
