import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;


public class Scheduler extends Thread {
	private Connection db;
	private String settings_file;
	private String ftp_host;
	private String ftp_user;
	private String ftp_pass;
	private int ftp_port;
	public String orders_list = null;
	public static int STATUS_WAITING = 1;
	public static int STATUS_RUNNING = 2;
	public static int STATUS_FINISHED = 3;

	public Scheduler(String settings_file, Connection db) {
		this.db = db;
		this.settings_file = settings_file;
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream(settings_file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.ftp_host = pro.getProperty("FTP_HOST", "localhost");
		this.ftp_port = Integer.parseInt(pro.getProperty("FTP_PORT", "21"));
		this.ftp_user = pro.getProperty("FTP_USER", "root");
		this.ftp_pass = pro.getProperty("FTP_PASS", "");
		System.out.println("ftp://" + this.ftp_user + ":****@" + this.ftp_host
				+ ":" + this.ftp_port);
	}

	public void run()
  {//label302:
    while (true)
    {
      Properties pro = new Properties();
      try
      {
        pro.load(new FileInputStream(this.settings_file));
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      String safe_stop = pro.getProperty("stop", null);
      if (safe_stop != null)
      {
        System.out.println("Quitting - bye");
        return;
      }

      System.out.println("-----------------------");
      Statement s;
      try
      {
        s = this.db.createStatement();
        //continue;
      }
      catch (SQLException e) {
        e.printStackTrace(); 
        return;
      }
      
      //Statement s;
      String query = "SELECT ftp_queue.id, ftp_queue.file, ftp_queue.status, ftp_queue.date_finished FROM ftp_queue WHERE ftp_queue.status = " + 
        STATUS_WAITING + " ORDER BY ftp_queue.id DESC" + " LIMIT 1";
      ResultSet result;
      try
      {
        result = s.executeQuery(query);
        //continue;
      }
      catch (SQLException e) {
        e.printStackTrace(); 
        return;
      }
      
//      ResultSet result;
      String file = null;
      int command_id = 0;
      try
      {
        if (result.next())
        {
          file = result.getString("file");
          if (File.separatorChar == '\\')
          {
            file = file.replace("/", "\\");
          }
          command_id = result.getInt("id");
          s.executeUpdate("UPDATE ftp_queue SET status = " + STATUS_RUNNING + ", date_executed = NOW() WHERE id =" + command_id); 
//          break label302;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()) + " nothink to do ");
      }
      catch (SQLException e)
      {
        e.printStackTrace(); 
        return;
      }
      //continue;

      if ((file == null) || (command_id == 0))
        break;
      Statement s1 = null;
      try
      {
        s1 = this.db.createStatement();
      }
      catch (SQLException e) {
        e.printStackTrace(); 
        return;
      }
//      continue;

      MyProcess proc = new MyProcess(this.ftp_host, this.ftp_port, this.ftp_user, this.ftp_pass, file);

      String query2 = "UPDATE ftp_queue SET status = " + STATUS_FINISHED + ", date_finished = NOW() WHERE id = " + command_id;
      if (proc.run() != 0)
      {
        query2 = "UPDATE ftp_queue SET status = " + STATUS_WAITING + ", date_executed = NULL WHERE id = " + command_id;
      }
      try
      {
        s1.executeUpdate(query2);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }

    }

    synchronized (this)
    {
      try
      {
        super.wait(10000L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
}
