# Employee Management System

-------------------------------------------------  ***Required***  --------------------------------------------------------------------

Copy and replace the config file with the file that is already present in Tomcat's directory -// for tomcat's JDBC realm and Tomcat authentication

---------------------------------------------------------------------------------------------------------------------------------------------------------

Then copy and paste the webapp files in the Tomcat's WebApp folder , start the Tomcat server, Open any browser and search for localhost:8080/[webapp_name].

The login is privelege based, so the menu screen will be based on user's role , the roles present are SuperAdmin, Admin, User. Of which the SuperAdmin has many options like adding, deleting, editing employee details, searching, exporting the details in a file, etc. Then the Admins will have fewer operations as compared to the SuperAdmin i.e, they don't have CRUd operations. The user role will have a simple interface with their details and have an option to change their Password.

Functions and Features :
 * Used JDBC realm
 * 2 Factor authentication (TOTP Based)
 * Email is sent to those who have changed their password
 * Used hibp API to check if the password is pwned
 * Export the details of the Employee in a txt file
 * Search -> based on Category, based on role, based on Employee Id
 * HttpSession is used to manage the session of the user
 * Tech stack:  
                -> Server - Apache Tomcat   
                -> Frontend & Backend - JSP, Java Servlets, HTML                                                                                  
                -> DataBase - MySQL

