package examples;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import security.InsufficientPermissionsException;
import security.UploadManager;

/**
 * Servlet implementation class ExampleFileUploader
 */
@WebServlet(
		urlPatterns="/example/upload",
		initParams= @WebInitParam(name="uploadLocation",value="C:\\Kartik\\TomcatUploads")
		)
@MultipartConfig(
		location="C:\\Kartik\\tomcatTemp",
		fileSizeThreshold=1024*1024*10, //10 MB
		maxFileSize=1024*1024*512, // 512 MB
		maxRequestSize=1024*1024*600 //600 MB
		
		
				)
/*
 * from the servlet 3.0 docs
 *he @MultipartConfig Annotation

The @MultipartConfig annotation supports the following optional attributes.

location: An absolute path to a directory on the file system. 
The location attribute does not support a path relative to the application context. 
This location is used to store files temporarily while the parts are processed or when the 
size of the file exceeds the specified fileSizeThreshold setting. The default location is "".

fileSizeThreshold: The file size in bytes after which the file will be temporarily stored on disk.
The default size is 0 bytes.

MaxFileSize: The maximum size allowed for uploaded files, in bytes. 
If the size of any uploaded file is greater than this size, the web container will throw an exception 
(IllegalStateException). The default size is unlimited.

maxRequestSize: The maximum size allowed for a multipart/form-data request, in bytes. 
The web container will throw an exception if the overall size of all uploaded files exceeds this threshold. 
The default size is unlimited. 
 */

public class ExampleFileUploader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String[] sqldbDetails={"samples","root","parth@kartik123"};  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExampleFileUploader() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession loginSession;
		loginSession=request.getSession(false);
		if(loginSession==null)
		{
			response.sendRedirect("index.jsp");
		}
		String username=loginSession.getAttribute("username").toString();
		String uploadLocation=this.getInitParameter("uploadLocation");
		Collection<Part> fileparts=request.getParts();
		
		UploadManager fileManager=null;
		if(username==null||username.equals(""))
		{
			loginSession.invalidate();
			response.sendRedirect("index.jsp");
		}
		
		
		
		else
		{
			boolean uploaded=true;
			for(Part filepart:fileparts)
			{
				fileManager=new UploadManager(filepart,username,uploadLocation,sqldbDetails[0],
											sqldbDetails[1],sqldbDetails[2]);
				try {
					if(filepart.getSubmittedFileName().equals(""))
					{
						response.sendRedirect(this.getServletContext().getContextPath()+"/dashboard.jsp");
					}
					
					else if(fileManager.connect())
						{
							uploaded =uploaded && fileManager.upload("sample_user_files","sample_files", "sample_users");
							System.out.println("Uploaded "+filepart.getSubmittedFileName());
						}
					
				}
				catch(MySQLIntegrityConstraintViolationException e)
				{
					/*
					 * TODO javascript alert for this
					 */
					request.setAttribute("filenameIdentical", true);
					request.getRequestDispatcher("upload.html").forward(request, response);
				}

				catch (ClassNotFoundException | SQLException e) {
					
					e.printStackTrace();
				} catch (InsufficientPermissionsException e) {
					
					response.sendError(HttpServletResponse.SC_FORBIDDEN,"Access to file write denied!");
					e.printStackTrace();
				}
				finally
				{
					try {
						fileManager.destruct();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
								if(uploaded)
					response.getWriter().println("Success");
				else
					response.getWriter().println("Failure");
			}
		}
		
		
}

}
