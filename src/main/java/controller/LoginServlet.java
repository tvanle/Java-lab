package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    
    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        try {
            Account account = accountDAO.authenticate(username, password);
            
            if (account != null) {
                Customer customer = customerDAO.findById(account.getCustomerId());
                
                HttpSession session = request.getSession();
                session.setAttribute("account", account);
                session.setAttribute("customer", customer);
                session.setAttribute("username", username);
                session.setAttribute("role", account.getRole());
                
                if ("on".equals(remember)) {
                    Cookie userCookie = new Cookie("username", username);
                    userCookie.setMaxAge(7 * 24 * 60 * 60);
                    response.addCookie(userCookie);
                }
                
                if ("ADMIN".equals(account.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    String redirectURL = (String) session.getAttribute("redirectURL");
                    if (redirectURL != null) {
                        session.removeAttribute("redirectURL");
                        response.sendRedirect(redirectURL);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/books");
                    }
                }
            } else {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
    
    @Override
    public void destroy() {
        if (accountDAO != null) {
            accountDAO.closeConnection();
        }
        if (customerDAO != null) {
            customerDAO.closeConnection();
        }
    }
}