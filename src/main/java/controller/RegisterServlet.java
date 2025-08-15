package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    
    @Override
    public void init() throws ServletException {
        customerDAO = new CustomerDAO();
        accountDAO = new AccountDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        
        try {
            if (customerDAO.findByEmail(email) != null) {
                request.setAttribute("error", "Email already exists");
                request.setAttribute("name", name);
                request.setAttribute("phone", phone);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                return;
            }
            
            if (accountDAO.isUsernameExists(username)) {
                request.setAttribute("error", "Username already exists");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                return;
            }
            
            Customer customer = new Customer(name, email, phone);
            if (customerDAO.insert(customer)) {
                Account account = new Account(customer.getId(), username, password);
                account.setRole("CUSTOMER");
                
                if (accountDAO.insert(account)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("account", account);
                    session.setAttribute("customer", customer);
                    session.setAttribute("username", username);
                    session.setAttribute("role", account.getRole());
                    
                    request.setAttribute("success", "Registration successful!");
                    response.sendRedirect(request.getContextPath() + "/books");
                } else {
                    customerDAO.delete(customer.getId());
                    request.setAttribute("error", "Failed to create account");
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Failed to register customer");
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        }
    }
    
    @Override
    public void destroy() {
        if (customerDAO != null) {
            customerDAO.closeConnection();
        }
        if (accountDAO != null) {
            accountDAO.closeConnection();
        }
    }
}