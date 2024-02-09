import java.util.*;  
import javax.servlet.http.HttpSession;  
import java.io.IOException;
import java.sql.*;
import java.io.PrintWriter;     
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import javax.servlet.http.*;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import java.math.BigInteger;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
 
public class TOTP extends HttpServlet{

    static Connection con=null;
    static String in=null;
    static String id=null;
    static int error;
    static int id2;
    static String secret=null;
    public static void connection()  {
        try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           con= DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedb","root","Vimal@2002");
             
       } catch (Exception e) {
           System.out.println(e);
       }
  }
    public void doGet(HttpServletRequest req,HttpServletResponse res){
        HttpSession session=req.getSession();
        id = (String) session.getAttribute("EmpID");
        id2=Integer.parseInt(id);
        in=req.getParameter("code");
        int cnt=0;
        getSecret();
        String lastCode = null;
        while (true) {
        String code = getTOTPCode(secret);
       //  if (!code.equals(lastCode)) {
       // //System.out.println(code);
       //  }
        lastCode = code;
       if(in.equals(lastCode)){
           System.out.println("-- You are Authenticated --");
           cnt=1;
           break;
       }
        else{
            System.out.println("!!! Sorry , You are Not Authenticated !!!");
            cnt=0;
            error++;
            break;           
        }
    }
    try{
    if(cnt==1){    
             res.setContentType("text/plain");
             PrintWriter out = res.getWriter();
             out.print(1);
    }
    else{   if(error==3){
            error=0;
            req.logout();
        }   
             System.out.println(error);
             res.setContentType("text/plain");
             PrintWriter out = res.getWriter();
             out.print(0);
    }
}catch(Exception e){}
}

public static String getSecret(){
     PreparedStatement smt=null;
    try{
    connection();
    String query="SELECT secret FROM EMPLOYEEDB WHERE EmpID="+"?";
            smt=con.prepareStatement(query);
            smt.setInt(1, id2);
            
            ResultSet rs=smt.executeQuery();
            rs.next();
            secret=rs.getString("secret");
        }
        catch (Exception e) {
             System.out.println(e);
        }
        return secret;
}

private static byte[] hexStr2Bytes(final String hex) {
        // Adding one byte to get the right conversion
        // values starting with "0" can be converted
        final byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        final byte[] ret = new byte[bArray.length - 1];

        // Copy all the REAL bytes, not the "first"
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }
    private static long getStep() {
        // 30 seconds StepSize (ID TOTP)
        return System.currentTimeMillis() / 30000;
    }
public static String getTOTPCode(String secretKey) {
    Base32 base32 = new Base32();   
    byte[] bytes = base32.decode(secretKey);
    String hexKey = Hex.encodeHexString(bytes);
    return getOTP(getStep(),hexKey);
}

    private static String getOTP(final long step,final String key) {
        String steps = Long.toHexString(step).toUpperCase();
        while (steps.length() < 16) {
            steps = "0" + steps;
        }

        // Get the HEX in a Byte[]
        final byte[] msg = hexStr2Bytes(steps);
        final byte[] k = hexStr2Bytes(key);
        final byte[] hash = hmac_sha1(k, msg);
        final int offset = hash[hash.length - 1] & 0xf;
        final int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
       //   System.out.println("binary -> "+binary);
        final int otp = binary % 1000000;

        String result = Integer.toString(otp);
        while (result.length() < 6) {
            result = "0" + result;
        }
        return result;
    }
    private static byte[] hmac_sha1(final byte[] keyBytes, final byte[] text) {
        try {
            final Mac hmac = Mac.getInstance("HmacSHA1");
            final SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
