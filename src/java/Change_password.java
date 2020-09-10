/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Umesh
 */
public class Change_password extends HttpServlet 
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            String old_pass=request.getParameter("old_pass");
            String new_pass=request.getParameter("new_pass");
            HttpSession session = request.getSession();
            String user =(String)session.getAttribute("user");
            out.println("<h2>Hello "+user+"</h2>"); 
          
            //Step 1: Driver Manager(url,username,pass) 
            //Step 2: Connection
            Connection con;
            try 
            {
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Validate.class.getName()).log(Level.SEVERE, null, ex);
                }
                con = DriverManager.getConnection("jdbc:oracle:thin:@student.cf3d3qpjgd4i.us-east-1.rds.amazonaws.com:1521:ORCL","admin","admin123");
                //Step 3: Create Statement         
                Statement st=con.createStatement();
                System.out.println("Connected with database");
                //Step 4:prepare query
                String qry="SELECT PASSWORD FROM stud";
                //Step 5: pass the query to statement
                ResultSet rs=st.executeQuery(qry);
                //Step 6: Display records 
                if(rs.next()==false)
                {
                    System.out.println("Empty table");
                }
                else
                {
                    do
                    {
                        System.out.print(rs.getString(1) +" ");                                                                       
                        if(old_pass.equals(rs.getString(1)))
                        {
                            //Step 4:prepare query
                            String qry2="UPDATE stud SET PASSWORD='"+new_pass+"' WHERE USERNAME='"+user+"'";
                            //Step 5:execute query
                            int x=st.executeUpdate(qry2);
                            if(x>0)
                            {
                                System.out.println("Successfully changed");
                                response.sendRedirect("index.html");
                            }
                            else
                            {
                                System.out.println("Change failed");
                                out.println("<font color='red'><b>Change failed</b></font>");
                                RequestDispatcher rd=request.getRequestDispatcher("index.html");
                                rd.include(request,response);
                            }
                
                        }
                        else
                        {
                            out.println("<font color='red'><b>Invalid Password </b></font>");
                            RequestDispatcher rd=request.getRequestDispatcher("index.html");
                            rd.include(request,response);
                        }
                    }while(rs.next());
                }
                con.commit();
                con.close();                    
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(Validate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
