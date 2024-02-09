<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.*"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<% Class.forName("com.mysql.cj.jdbc.Driver");%>
<!DOCTYPE html>
<html>
<head>
<title>Authentication</title>
</head>
<body>
   <% 
   String id=request.getRemoteUser();
   out.print(id);
   session.setAttribute("EmpID",id);
   %>
   <script  src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
   <script type="text/javascript">
   
   $(document).ready(function(){
    $('#frm').on('submit', function(e){ 
      e.preventDefault();
      var code = document.getElementById("code").value;
      $.ajax({
        url: 'TOTP',
        method: 'get',
        data: { code: code},
        success:
        function (response) {
          console.log("response->"+response);
         if(response==1){
          alert('Success  You are Authenticated !!!!');
          window.location.href = "welcome.jsp";
         }
         else if(response==0){
          alert('Error !!! Invalid Code , You will Loggeg out if You Enter Wrong code for 3 Times');
         window.location.href = "index.jsp";
         }
  
   },
  
        error: function (httpRequest, textStatus, errorThrown) {  
                  alert("Error: " + textStatus + " " + errorThrown + " " + httpRequest);
                  
              }
        });
   });     
});
</script>
<h3>------ Authenticate USER ------</h3>
<form id="frm">
<table>

<tr>
<td>Enter the Authentication code :</td>
<td><input type="text" class="form-control" name="code" id="code" required></td></tr>
</TABLE><br>
<input type="submit" value="submit">
</form>	

</body>
</html>