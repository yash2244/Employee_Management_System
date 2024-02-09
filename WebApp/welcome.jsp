<!DOCTYPE html>
<html>
<head>
<title>Employee Management</title>
</head>
<body>

<%	int j=0;
	out.print(request.getRemoteUser());
      String pg=null;
           try{
                 if(request.isUserInRole("1")){
                    pg="/SuperAdmin.jsp";
                 }
                 else if(request.isUserInRole("2")){
                    pg="/Admin.jsp";
                 }
                 else if(request.isUserInRole("3")){
                    pg="/User.jsp";
                 } 
           }			
           catch(Exception e){
              System.out.println(e);
           }
           //res.sendRedirect(pg);
           request.getRequestDispatcher(pg).include(request, response);
       %>

</body>
</html>