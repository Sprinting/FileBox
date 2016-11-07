package security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.Part;

import security.InsufficientPermissionsException;

public class UploadManager {
	final String DRIVER="com.mysql.jdbc.Driver";
	String DB="jdbc:mysql://localhost/";
	
	
	
	Connection con;
	PreparedStatement stmt;
	Part filepart;
	String username,uploadLocation,dbuser,dbpass;
	File uploadDir;
	ResultSet rs;
	
	
	public UploadManager(Part part,String username,String uploadLocation,String db,String dbuser,String dbpass)
	{
		this.dbpass=dbpass;
		this.dbuser=dbuser;
		filepart=part;
		DB+=db;
		this.username=username;
		this.uploadLocation=uploadLocation+File.separator+username;
	}
	public boolean connect() throws ClassNotFoundException, SQLException
	{
		Class.forName(DRIVER);
		con=DriverManager.getConnection(DB,dbuser,dbpass);
		return (con!=null);
		
	}
	public boolean upload(String userFilesTable,String fileTable,String userTable) throws InsufficientPermissionsException, 
	IOException
	{
		boolean success=true;
		try
		{
			String filename=filepart.getSubmittedFileName();
			filename=Paths.get(filename).getFileName().toString(); // fix for buggy .getSubmittedFilename()
			if(filename.equals(""))
				return false;
			String filepath=uploadLocation+File.separator+filename;
			System.out.println(filepath);
			filepath=filepath.replace("\\", "/");
			System.out.println(filepath);
			
			//update file table
			/*
			 * change filepath values here
			 */
			
			//TODO needs its own method
			String statement="insert into " +fileTable+ "(filepath) values(?)";
			System.out.println(statement);
			stmt=con.prepareStatement(statement);
			con.setAutoCommit(true);
			stmt.setEscapeProcessing(true);
			//stmt.setString(1, fileTable);
			//stmt.setString(2, fileColumn);
			stmt.setString(1, filepath);
			success= success && stmt.executeUpdate()==1;
			stmt.close();
			//update m-n relationship table for file-user relationship 
			/*
			 * Retrieve 'uid' from 'user' table, 'fid' from 'files' table, insert ('fid','uid') into 'user_files'
			 */
			
			//TODO needs its own method
			statement="select uid from "+userTable+" where username like '"+username+"'";
			System.out.println(statement);
			stmt=con.prepareStatement(statement);
			stmt.setEscapeProcessing(true);
			rs=stmt.executeQuery();
			success=success && rs.first();
			
			int uid=rs.getInt(1);
			stmt.close();
			rs.close();
			
			//TODO needs its own method
			statement="select fid from "+fileTable+" where filepath like ?";
			System.out.println(statement);
			stmt=con.prepareStatement(statement);
			//stmt.setEscapeProcessing(true);
			stmt.setString(1, filepath);
			rs=stmt.executeQuery();
			success=success && rs.first();
			int fid=rs.getInt(1);
			stmt.close();
			rs.close();
			
			//TODO needs its own method
			statement="insert into " +userFilesTable+ " values(?,?)";
			System.out.println(statement);
			stmt=con.prepareStatement(statement);
			stmt.setEscapeProcessing(true);
			stmt.setInt(1, fid);
			stmt.setInt(2, uid);
			success= success && stmt.executeUpdate()==1;
			stmt.close();
			
			
			uploadDir=new File(uploadLocation);
			if(!uploadDir.exists())
			{
				if(!uploadDir.mkdirs())
					throw new InsufficientPermissionsException();
			}
			
			System.out.println(filepath);
			filepart.write(filepath);
			
			/*
			  if(success)
				con.commit();
			else
				con.rollback();
			*/
			return success;
			
		}
		catch(NullPointerException|FileNotFoundException f)
		{
			f.printStackTrace();
			try {
				this.destruct();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			try {
				this.destruct();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		
	}
	public void destruct() throws SQLException
	{
		if(stmt!=null)
			stmt.close();
		if(rs!=null)
			rs.close();
		if(con!=null)
			con.close();
		
	}

}
