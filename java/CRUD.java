import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import java.security.SecureRandom;
import java.util.*;


public class CRUD extends HttpServlet{
    
    static int id;
	static String pass;
	static String FirstName;
	static String LastName;												
	static String mobNum;
	static String email_id;
	static String cat;
	static String Age;
	static String city;
	static String Role;
    static int cid;
    static int rid;
    static String secret;
    static Connection con;
    public static void connection()  {
        try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           con= DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedb","root","Vimal@2002");
             
       } catch (Exception e) {
           System.out.println(e);
       }
  }



    protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException , IOException{
		//EmployeeDB db=new EmployeeDB();
        res.setContentType("text/html");
			PrintWriter out = res.getWriter();
        String id2=null;
		int j=0;
        int flag=0;
		id2=req.getParameter("Employee_ID");
        FirstName=req.getParameter("FirstName");
        LastName=req.getParameter("LastName");
        Age=req.getParameter("Age");
        city=req.getParameter("city");
        mobNum=req.getParameter("Mobile");
        email_id=req.getParameter("Email");
        pass=req.getParameter("password");
        cat=req.getParameter("cat");
		Role=req.getParameter("rol");
        cid=Integer.parseInt(cat);
        rid=Integer.parseInt(Role);
        id=Integer.parseInt(id2);
        secret=generateSecretKey();
        PreparedStatement smt=null;
		String query;
        if(!isalid(id)){
		try{
		connection();
		query="INSERT INTO EMPLOYEEDB (EmpID,EmpFirstName,EmpLastName,Age,City,EmpMobno,EmpEmailID,Pass,Cid,Rid,secret) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		smt=con.prepareStatement(query);
		smt.setInt(1, id);
		smt.setString(2, FirstName);
		smt.setString(3, LastName);
		smt.setString(4, Age);
		smt.setString(5, city);
		smt.setString(6, mobNum);
		smt.setString(7, email_id);
		smt.setString(8, pass);
		smt.setInt(9, cid);
		smt.setInt(10, rid);
        smt.setString(11,secret);
		int cnt = smt.executeUpdate();
        if(cnt==0) {
            
            out.print("\nNo !!!!");
        }
        else {
           
            out.println("\nDone ");
        }
	}
		catch(Exception e){
			System.out.println(e);
		}
        }
        else{
            out.print("<h1>Sorry ID Already Exists</h1>");
            out.print("<br><form>");
            out.print("<input type=\"submit\" value=\"Retry\" formaction=\"SuperAdmin.jsp\">");
            out.print("</form>");
            
        }


        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println(session);
        res.sendRedirect("login.jsp");
        } else {
        out.print("<form action=\"SuperAdmin.jsp\" method=\"post\"><h1><center>Go Back</center>");
        out.print("</h1><center>To Go Back to the Main Menu !!!<table><tr><td><input type=\"submit\" value=\"__\"></td>");
        out.print("</tr></center></table></form>");
    }
}
    public static String generateSecretKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);
    //System.out.println(" Bytes -> "+bytes + " Random -> " + random);
    Base32 base32 = new Base32(3);
    //System.out.println(" Encoded -> "+ base32.encodeToString(bytes));
    return base32.encodeToString(bytes);
}

    static boolean isalid(int eid) {
        PreparedStatement smt=null;
        try{
            connection();
            String query="SELECT * FROM EMPLOYEEDB WHERE EmpID="+"?";
            smt=con.prepareStatement(query);
            smt.setInt(1, eid);
            
            ResultSet rs=smt.executeQuery();
            int j=0;
            while(rs.next()) {
                j=1;
            }
            if(j!=0) {
                return true;
            }
            
        }
        catch (Exception e) {
             System.out.println(e);
         }
        return false;
        }

}
