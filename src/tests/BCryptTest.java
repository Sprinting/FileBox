package tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import security.BCrypt;
public class BCryptTest {

	public static void main(String[] args) {
		String filepath="C:/AJ/Projects/website/SampleData/filebox_users.txt";
		String filepath2="C:/AJ/Projects/website/SampleData/filebox.txt";
		try (
				BufferedReader fr=new BufferedReader(new FileReader(filepath));
				PrintWriter pr=new PrintWriter(new FileWriter(filepath2));
				BufferedReader fr1=new BufferedReader(new FileReader(filepath2));
				
			)
		{
			int workload=14;
			int i=0;
			String text=null;
			String usr,pwd,hash;
			ArrayList<String> pList=new ArrayList<String>();
			while((text=fr.readLine())!=null)
			{
				usr=text.split(" ")[0];
				pwd=text.split(" ")[1];
				pList.add(pwd);
				hash=BCrypt.hashpw(pwd, BCrypt.gensalt(workload));
				hash=usr+" "+hash;
				System.out.println(hash);
				pr.write(hash+System.lineSeparator());
				
			}
			pr.close();
			while((text=fr1.readLine())!=null)
			{
				usr=text.split(" ")[0];
				pwd=text.split(" ")[1];
				boolean status=
						BCrypt.checkpw(pList.get(i), pwd);
				i++;
				usr=usr+" : "+status;
				System.out.println(usr);
			}
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
 