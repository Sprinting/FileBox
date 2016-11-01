package tests;

import java.sql.SQLException;

import security.LoginException;
import security.SQLManager;
public class TestLoginManager {
	final static String[] loginDetails={"root","parth@kartik123"};

	public static void main(String[] args) {
		
		SQLManager newuser=new SQLManager("Kartik","Modgal",loginDetails[0],loginDetails[1]);
		try {
			boolean connected=newuser.attemptConnection("samples");
			newuser.getUserDetails("sample_users");
			boolean validated=newuser.validateUser();
			System.out.println("Connected :"+connected);
			System.out.println("Validated :"+validated);
			System.out.println("Details: ");
			System.out.println("Username: "+newuser.getUsername());
			//System.out.println("Supplied Password: "+newuser.getPassword());
			System.out.println("Stored Hash: "+newuser.getDbhash());
		} catch (LoginException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			newuser.destruct();
		}
	}

}
