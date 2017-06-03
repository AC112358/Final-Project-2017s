package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class SearchDatabase {
	private static String url = "jdbc:mysql://localhost:3306/refsnps";
	private static String user = "root";
	private static String pw = "ravenclaw1";
	
	public static void writeToDB(boolean verbose, File refFile){
		try{
			BufferedReader in = new BufferedReader(new FileReader(refFile));
			Connection myConn = DriverManager.getConnection(url, user, pw);
			Statement stmnt = (Statement) myConn.createStatement();
			String sql = "insert into refsnps.reftable " + 
			"(rs_id, chromosome, bp_position)"
			+ " values (";
			in.readLine();
			String line = in.readLine();
			String[] tokens;
			int index = 2;
			while (line != null){
				tokens = line.split("\\s+");
				try{
					String location = "'" + tokens[tokens.length-2] + "'" + ", '" + tokens[tokens.length-1] + "'";
					//lines.put(tokens[1], location);
					/*ADD TO DATABASE*/
					location = sql + "'" + tokens[1] + "', " + location + ")";
					/*if (index < 25){
						System.out.println("Will execute " +  location);
					}*/
					stmnt.executeUpdate(location);
					
				
				}catch(Exception e){
					if (verbose){
						System.out.println(lineInvalidMsg(index));
						e.printStackTrace();
						System.exit(0);
					}
				}
				line = in.readLine();
				index++;
			}
			in.close();
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
		
		private void writeToFile(File writeTo){
			
		}
	
	
	public static void readAndWriteFile(boolean verbose){
		
	}
	
	
	private static String lineInvalidMsg(int index){
		return "Line " + index + " invalid formatting! (2nd col rsID, 2nd to last col chromosome, last col base pair)";
	}
	
	private static String lineRsInvalidMsg(int index){
		return "Line " + index + " invalid formatting! (1st col rsID, 2nd to last col p val)";
	}
	
	public static void main(String[] args) throws SQLException{
		/*Connection myConn = DriverManager.getConnection("jdbc:msql://localhost:3306/refsnps", "root", "ravenclaw1");
		Statement myStat = (Statement) myConn.createStatement();
		ResultSet myRs = myStat.executeQuery("select * from reftable");
		while (myRs.next()){
			System.out.println(myRs.getString("last_name") + " " + myRs.getString("first_name"));
		}*/
		writeToDB(true, new File("../../REF.txt"));
		System.out.println("what have i done");
	}
}
