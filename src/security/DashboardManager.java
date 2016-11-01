package security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import security.JsonWebToken;

public class DashboardManager 
{
	String username,dbuser="root", dbpass="parth@kartik123";
	public DashboardManager(String username)
	{
		this.username=username;
	}
	
	public  String prepareFileListJson(String database) throws SQLException
	{
		PreparedStatement stmt = null;
		Connection con=null;
		ResultSet rs=null;
		JsonWebToken jt=new JsonWebToken();
		final String driver="com.mysql.jdbc.Driver";
		String db="jdbc:mysql://localhost/"+database+"?useSSL=false";
		ArrayList<String> userFiles=new ArrayList<String>();
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(db,dbuser,dbpass);
			
			/*
			 * get all files a user owns
			 */
			String query="select sample_files.filepath,sample_users.username "
					+ "from sample_files inner join sample_user_files "
					+ "on sample_user_files.fid=sample_files.fid "
					+ "inner join sample_users "
					+ "on sample_user_files.uid=sample_users.uid "
					+ "where username like ?";
			stmt=con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setEscapeProcessing(true);
			rs=stmt.executeQuery();
			
			while(rs.next())
			{
				String fp=rs.getString("filepath");
				//fp="'"+fp+"'";
				userFiles.add(fp);
			}
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(stmt!=null)
			{
				stmt.close();
			}
			if(rs!=null)
			{
				rs.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
		}
		return jt.makeJson(username,userFiles);
		
	}
	public static void main(String args[])
	{	
		DashboardManager newManager=new DashboardManager("Lakshay");
		
		try {
		System.out.println(newManager.prepareFileListJson("samples"));
			newManager=new DashboardManager("Kartik");
			//System.out.println(newManager.prepareFileListJson("samples"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
