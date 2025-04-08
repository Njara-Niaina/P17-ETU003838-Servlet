package main.java.com.helloWorld;

 
import java.io.IOException; 
import java.io.PrintWriter; 

import jakarta.servlet.http.*; 
import jakarta.servlet.ServletException; 
 
public class HelloServlet extends HttpServlet { 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { 
        res.setContentType("text/html"); 
        PrintWriter result = res.getWriter(); 

        result.println("<HTML>"); 
        result.println("<HEAD><TITLE>Bonjour</TITLE></HEAD>"); 
        result.println("<BODY>"); 
        result.println("<BIG>Hello World </BIG>"); 
        result.println("</BODY>"); 
        result.println("</HTML>"); 
    } 
} 
