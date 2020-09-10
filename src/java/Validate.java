/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;  
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
public class Validate extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, ClassNotFoundException 
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            String username=request.getParameter("user");
            String password=request.getParameter("pass");
            
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
                String qry="SELECT * FROM stud";
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
                        System.out.print(rs.getString(3) +" ");
                        System.out.println(rs.getString(4)+" ");
                        
                        
                        if(username.equals(rs.getString(3)) && password.equals(rs.getString(4)))
                        {
                            HttpSession session = request.getSession();
                            session.setAttribute("user",username);                            
                            response.sendRedirect("Welcome");
                        }
                        else
                        {
                            out.println("<font color='red'><b>Invalid Password </b></font>");
                            RequestDispatcher rd=request.getRequestDispatcher("index.html");
                            rd.include(request,response);
                        }
                    }while(rs.next());
                }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Validate.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Validate.class.getName()).log(Level.SEVERE, null, ex);
        }
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
