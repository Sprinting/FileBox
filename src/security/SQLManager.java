package security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import security.BCrypt;



public class SQLManager {
	static final String DRIVER="com.mysql.jdbc.Driver";
	
	
	static int workload=14; //do not change this,will break password hash-checks in future;
	//10-31 is valid value;
	//14 was the initial default, if it ever changes
	
	String DB="jdbc:mysql://localhost/";
	PreparedStatement stmt;
	Connection con;
	ResultSet userSet;
	String username,password,dbhash,dbuser,dbpass;
	
	
	public SQLManager(String username,String password,final String dbuser,
			final String dbpass)
	{
		this.username=username;
		this.password=password;
		this.dbuser=dbuser;
		this.dbpass=dbpass;
	}
	public  boolean attemptConnection(String dbname) throws LoginException, SQLException
	{
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(DB+dbname, dbuser, dbpass);
			return true;
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage()+"Class was not found :: "+DRIVER);
			e.printStackTrace();
			throw new LoginException();
		} catch (SQLException e) {
			System.err.println(e.getMessage()+"Attempt to get db connection failed:: "+DRIVER);
			e.printStackTrace();
			throw new SQLException();
		}
		
	}
	public void getUserDetails(String table) throws LoginException
	{
		String statement="SELECT username,password,uid from "+
							table+" where username like ?;";
		System.out.println(statement);
		try {
			stmt=con.prepareStatement(statement);
			stmt.setEscapeProcessing(true);
			stmt.setString(1, username);
			userSet=stmt.executeQuery();
		} catch (SQLException e) {
			
			System.err.println(e.getMessage()+"\nAttempt to retrieve resultset failed:: "+DRIVER);
			e.printStackTrace();
			throw new LoginException();
		}
		
		
	}
	public boolean validateUser() throws SQLException
	{
		return checkIfUserExists() && matchPassword();
	}
	 boolean checkIfUserExists() throws SQLException
	{
		return userSet.isBeforeFirst();
		//isBeforeFirst() returns false if the cursor isn't before the first row
		//or if the result set has no rows;
		//userSet has only 1 row;
	}
	 boolean matchPassword() throws SQLException
	{
		 
		if(userSet.first())
		{
			//hash=userSet.getString("hash");
			username=userSet.getString("username");
			dbhash=userSet.getString("password");
			System.out.println(userSet.getInt("uid"));
			
		}
		if(null == dbhash || !dbhash.startsWith("$2a$"))
		{
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
		}
			
		
		return BCrypt.checkpw(password, dbhash);
		
	}
	public String getUsername() {
		return username;
	}
	 @SuppressWarnings("unused")
	private String getPassword() {
		return password;
	}
	public String getDbhash() {
		return dbhash;
	}
	public void destruct() {
		try
		{
			if(stmt!=null)
				stmt.close();
			if(userSet!=null)
				userSet.close();
			if(con!=null)
				con.close();
			password="";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
