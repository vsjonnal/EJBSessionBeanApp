package com.test.or.client;

import com.test.or.beans.TimeoutAnnotSLS;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ClientServletAnnot", urlPatterns = {"/ClientServletAnnot"})
public class ClientServletAnnot extends HttpServlet {
    
    @EJB
    private TimeoutAnnotSLS ts;

    String jndiName="java:global.EJBSessionBeanApp.TimeoutAnnotSLSBean!com.test.or.beans.TimeoutAnnotSLS";
    String userName = "system";
    String password = "gumby1234";
    String serverURL = "t3://100.111.143.183:9001";

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
            throws ServletException, IOException, NamingException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("JNDI Context look up for '" + jndiName + "' using user '" + userName + "' and password'"
                + password + "'." + "url:" + serverURL);

        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ClientServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h3>Servlet ClientServlet at " + request.getContextPath() + "</h3>");
            out.println("</body>");
            out.println("</html>");
            out.println("<p>TEST Started</p>");
            Context ctx = getContext(userName, password, serverURL);
            
            Object obj = ctx.lookup(jndiName);
            out.println("<p>Looked up the object -- TimeoutAnnotSLS\n" + obj.getClass() + "</p>");
            ts = (TimeoutAnnotSLS) obj;
            out.println("<p>Status Object before expiry : " + ts.getTimerStatus() + "</p>");

            String str = testTimerDI();
            out.println("<p>Response is " + str + "</p>");
            out.println("<p>TEST Completed</p>");
        } catch (Exception e) {
            Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, "Exception caugth", e);
        } finally {
            out.close();
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
        } catch (NamingException ex) {
            Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (NamingException ex) {
            Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Context getContext(String userName, String password, String serverURL) throws NamingException {
        Hashtable<String, String> h = new Hashtable<>();
        h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        h.put(Context.PROVIDER_URL, serverURL);
        h.put(Context.SECURITY_PRINCIPAL, userName);
        h.put(Context.SECURITY_CREDENTIALS, password);
        return new InitialContext(h);
    }

    private String testTimerDI() {
        String gotError;
        try {
            ts.createTimer();
            long startTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Start Sleeping for 12000 ms at " + startTime);
            Thread.sleep(12000);
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("After Sleeping for " + (endTime - startTime) + " ms at " + endTime);
            System.out.println("Status Object after expiry : " + ts.getTimerStatus());
            System.out.println("Tiemr Handle Info: " + ts.getTimerHandleInfo());
            return ("Timer did not expire after the specified interval");
        } catch (Exception e) {
            gotError = getStackTrace(e);
            Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, "testTimerDI threw an error : \n", gotError);
            return (gotError);
        }
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
