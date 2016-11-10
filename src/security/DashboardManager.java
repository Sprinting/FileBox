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
	String fileDetails,username,dbuser="root", dbpass="parth@kartik123";
	JsonWebToken jt=null;
	public DashboardManager(String username)
	{
		this.username=username;
		this.jt=new JsonWebToken();
	}
	
	public  String prepareFileListJson(String database) throws SQLException
	{
		PreparedStatement stmt = null;
		Connection con=null;
		ResultSet rs=null;
		
		final String driver="com.mysql.jdbc.Driver";
		String db="jdbc:mysql://localhost/"+database+"?useSSL=false";
		ArrayList<String> userFiles=new ArrayList<String>();
		ArrayList<String> shareFiles=new ArrayList<String>();
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
			rs.close();
			stmt.close();
			query="select sample_users.username,sample_shares.sfid as shared_file_id,sample_files.filepath from "
					+ "sample_users inner join sample_shares on "
					+ "sample_users.uid=sample_shares.uid "
					+ "inner join sample_files on "
					+ "sample_files.fid=sample_shares.sfid where username like ?";
			
			stmt=con.prepareStatement(query);
			stmt.setEscapeProcessing(true);
			stmt.setString(1, username);
			rs=stmt.executeQuery();
			System.out.println(username);
			//System.out.println(rs.getMetaData().getColumnCount());
			while(rs.next())
			{
				String fp=rs.getString("filepath");
				//System.out.println(fp);
				shareFiles.add(fp);
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
		
		fileDetails=jt.makeJson(username,userFiles,shareFiles);
		return fileDetails;
		
	}
	public static boolean checkFileOwnership(String username,String filepath)
	{
		
		return true;
	}
	
	public static void main(String args[])
	{	
		DashboardManager newManager=new DashboardManager("Lakshay");
		
		try {
		System.out.println(newManager.prepareFileListJson("samples"));
			
			//System.out.println(newManager.prepareFileListJson("samples"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
