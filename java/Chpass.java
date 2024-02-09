import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.*;  
import java.io.*;  
import java.net.URL;
import java.io.IOException;  
import java.io.PrintWriter;     
import javax.servlet.http.HttpSession;  
import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.Properties;  
import javax.mail.*;  
import javax.mail.internet.*;  
import java.time.format.DateTimeFormatter;  
import java.time.LocalTime;    
import javax.mail.Authenticator;

 
public class Chpass extends HttpServlet{


    static Connection con=null;
    static boolean tst=false;
    static String emailid=null;
    static String pass=null;
    static String id = null;
    static int id2;
    static ArrayList<String> em=new ArrayList<String>();
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    static LocalTime localTime = LocalTime.now();
    public static void connection()  {
        try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           con= DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedb","root","Vimal@2002");
             
       } catch (Exception e) {
           System.out.println(e);
       }
  }

    public void doPost(HttpServletRequest req,HttpServletResponse res){
        HttpSession session=req.getSession();
        id = (String) session.getAttribute("EmpID");
        id2=Integer.parseInt(id);
        pass=req.getParameter("pass");
        int j=getUrlContents(encryptThisString(pass));
   if(j==0){
        chpass(res);
    }
    else{try
    {     res.setContentType("text/plain");
         PrintWriter out = res.getWriter();
         out.print(0);
         // out.println("<h3>The PassWord is not Safe !!!....!!!! </h3> ");
         // out.println(" <h4>To Retry!!!</h4><form action=\"Chpass.jsp\" method=\"post\"><input type=\"submit\" value=\"Retry\"></form> ");
     }
     catch(Exception e){
        
     }
    }
}

    public static String encryptThisString(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }    
           return hashtext;  
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        
    }
    public static int getUrlContents(String hashtext)  
  {  
    String content = new String();  
    int res=0;
    try  
    {  String hashSub = (String)hashtext.substring(0,5).toUpperCase(); 
    System.out.println(hashtext);
    System.out.println(hashSub);
    System.out.println((hashtext).substring(5).toUpperCase());
    System.out.println((hashtext).substring(5,40).toUpperCase());
            URL URL = new URL("https://api.pwnedpasswords.com/range/" + hashSub); 
            BufferedReader br = new BufferedReader(new InputStreamReader(URL.openStream()));

            while ((content = br.readLine()) != null) {
                /* read each line */
                //System.out.println(content);
                 if(content.substring(0,35).equals((hashtext).substring(5,40).toUpperCase()))
                 {
                    res=1;
                    System.out.println("Oh Shot You were Breached !!!..");
                    break;
                }
            }
            br.close(); 
    }  
    catch(Exception e)  
    {   
     e.printStackTrace();  
    }  
    return res;  
  }  

static void chpass(HttpServletResponse res) {
        java.sql.Timestamp time=new java.sql.Timestamp(new java.util.Date().getTime());
        PreparedStatement smt=null;
        try {
            res.setContentType("text/plain");
            PrintWriter out = res.getWriter(); 
            connection();
            String query = "UPDATE EMPLOYEEDB SET PASS=?,Time=? WHERE EMPID=?";
            smt=con.prepareStatement(query);
            smt.setString(1, pass);
            smt.setTimestamp(2,time);
            smt.setInt(3, id2); 
            int cnt=0;
            cnt=smt.executeUpdate();
            if(cnt!=0){
                System.out.println("PassWord changed Successfully");
            }
        
            sendmail();
            // out.print("<form action=\"Redirect\" method=\"post\"><h1><center>Go Back</center>");
            // out.print("</h1><center>To Go Back to the Main Menu !!!<table><tr><td><input type=\"submit\" value=\"__\"></td>");
            // out.print("</tr></center></table></form>");
        out.println(1);

        }
        catch (Exception e) {
            System.out.println(e);
            }
    }
    static void sendmail(){
        PreparedStatement smt1=null;
        try{
            connection();
            String query="SELECT EmpEmailID FROM EMPLOYEEDB WHERE EmpID="+"?";
            smt1=con.prepareStatement(query);
            smt1.setInt(1, id2);
            
            ResultSet rs=smt1.executeQuery();
            rs.next();
            emailid=rs.getString("EmpEmailID");
            
        }
        catch (Exception e) {
             System.out.println(e);
         }
           
        final String user="vimal200302@gmail.com";  
        final String password="kwakpiiofekipyya"; 
        String to=emailid; 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(user,password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("PassWord Changed");
            message.setText("PassWord Changed Successfully !!!!");
            Transport.send(message);
            remail();
            System.out.println("message sent successfully"+dtf.format(localTime));
        } catch (Exception e) {
            System.out.println("Error -> SendMail");
         System.out.println(e);
         }

    }
    static void remail(){
        LocalTime lct = LocalTime.now();
//        String[] em=new String[5];
        Timer time=new Timer();
        
        TimerTask task = new TimerTask() {  
       
     public void run() { 
        tst=true; 
        
        int i=0;
        //System.out.println("Running Time : "+ m++);
        int tt=5;
         PreparedStatement smt1=null;
        try{
            connection();
            String query="SELECT EmpEmailID FROM employeedb Where TIMESTAMPDIFF(Minute, Time, now()) <  ? ";
            smt1=con.prepareStatement(query);
            smt1.setInt(1, tt);
            
            ResultSet rs=smt1.executeQuery();
            while(rs.next()){
                //em[i]=rs.getString("EmpEmailID");
                em.add(rs.getString("EmpEmailID"));
                i=i+1;
            }
           }
        catch (Exception e) {
            //System.out.println(" E-MailID  Get Error -> ");
             //System.out.println(e);
         }
             
         // final int k=i;
          
        final String user="vimal200302@gmail.com";  
        final String password="kwakpiiofekipyya";  
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(user,password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            for(int j=0;j<em.size();j++){
                System.out.println(em.get(j));
             message.addRecipient(Message.RecipientType.TO,new InternetAddress(em.get(j)));   
            System.out.println("Message to Rechange Password Sent Successfully !!"+dtf.format(lct));
            }
            message.setSubject("Can Rechange the PassWord");
            message.setText("The PassWord was changed 5 Minutes Ago , You can now Change your password Again !!");
            Transport.send(message);
        } catch (Exception e) {
            System.out.println("Error in -> Remail ");
         System.out.println(e);
         }

           
//        // time.cancel();    
    };  
};  
    // long delay = 300000;  
if(tst==true){
    System.out.println("Already Running -> Remail");
}
else{
    System.out.println("Starting NEW -> Remail");
     time.schedule(task, 0,120000); 
}
    
}
}
